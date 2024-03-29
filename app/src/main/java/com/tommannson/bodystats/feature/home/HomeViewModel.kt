package com.tommannson.bodystats.feature.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.base.BaseViewmodel
import com.tommannson.bodystats.infrastructure.ApplicationUser
import com.tommannson.bodystats.infrastructure.SavedStats
import com.tommannson.bodystats.infrastructure.configuration.*
import com.tommannson.bodystats.model.paramrecalculation.progress.MeasureProgressCalculator
import com.tommannson.bodystats.model.statistics.Statistic
import com.tommannson.bodystats.model.statistics.getStatFormatter
import com.tommannson.bodystats.utils.fmt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.threeten.bp.LocalDate
import java.math.BigDecimal
import javax.inject.Inject


@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val userDao: UserDao,
    private val statsDao: StatsDao,
) : BaseViewmodel() {

    val progressCalculator = MeasureProgressCalculator()

    private val _state = MutableStateFlow<HomeState>(HomeState.Init)
    val state: StateFlow<HomeState> = _state.asStateFlow()
    var subscription: Job? = null

    fun initialiseData(statsToInit: List<String>) {

        _state.value = HomeState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getAll().firstOrNull()

            if (user == null) {
                _state.value = HomeState.NoData
                return@launch
            }
            subscription?.cancel()
            subscription = statsDao.getParamLive(user.id, statsToInit)
                .onEach {

                    val groupedStats = mutableMapOf<String, MutableList<SavedStats>>()

                    for (statItem in it) {
                        val foundList = groupedStats[statItem.statName] ?: mutableListOf()
                        foundList.add(statItem)
                        groupedStats[statItem.statName] = foundList
                    }

                    val progress: MeasurementsProgress =
                        progressCalculator.calculateDifference(groupedStats)


                    val lastWeightLog =
                        if (groupedStats[Statistic.WEIGHT] != null && (groupedStats[Statistic.WEIGHT]?.isEmpty() == false)) {
                            groupedStats[Statistic.WEIGHT]?.last()?.value ?: user.weight
                        } else {
                            user.weight
                        }

                    val loadedWeightInfo = WeightInfo(
                        lastWeightLog,
                        user.dreamWeight fmt getStatFormatter(Statistic.WEIGHT)
                    )

                    _state.value = HomeState.DataLoaded(
                        user,
                        groupedStats,
                        loadedWeightInfo,
                        progress,
                    )
                }.launchIn(viewModelScope + Dispatchers.IO)
        }
    }

    fun decreaseWeight() {
        invokeTransaction {
            val user = userDao.getAll().firstOrNull() as ApplicationUser
            val operationTime = LocalDate.now()
            val foundWeight =
                statsDao.getParamInfo(user.id, operationTime, listOf(Statistic.WEIGHT))

            if (foundWeight != null) {
                val foundValue = foundWeight.value
                val calculatedValue = BigDecimal(foundValue.toDouble()).subtract(BigDecimal(.1))
                statsDao.udateStats(listOf(foundWeight.copy(value = calculatedValue.toFloat())))
            } else {
                val internalState = state.value
                val valueToCalculate =
                    if (internalState is HomeState.DataLoaded && internalState.mapOfStats[Statistic.WEIGHT]?.size ?: 0 > 0) {
                        internalState.mapOfStats[Statistic.WEIGHT]?.last()?.value?.toDouble() ?: 0.0
                    } else {
                        user.weight.toDouble()
                    }
                val calculatedValue = BigDecimal(valueToCalculate).subtract(BigDecimal(.1))
                val newStatsValue =
                    SavedStats(Statistic.WEIGHT, calculatedValue.toFloat(), operationTime, user.id)
                statsDao.createNewStats(listOf(newStatsValue))
            }
        }
    }

    fun increaseWeight() {
        invokeTransaction {
            val user = userDao.getAll().firstOrNull() as ApplicationUser
            val operationTime = LocalDate.now()
            val foundWeight =
                statsDao.getParamInfo(user.id, operationTime, listOf(Statistic.WEIGHT))

            if (foundWeight != null) {
                val foundValue = foundWeight.value
                val calculatedValue = BigDecimal(foundValue.toDouble()).add(BigDecimal(.1))
                statsDao.udateStats(listOf(foundWeight.copy(value = calculatedValue.toFloat())))
            } else {
                val internalState = state.value
                val valueToCalculate =
                    if (internalState is HomeState.DataLoaded && internalState.mapOfStats[Statistic.WEIGHT]?.size ?: 0 > 0) {
                        internalState.mapOfStats[Statistic.WEIGHT]?.last()?.value?.toDouble() ?: 0.0
                    } else {
                        user.weight.toDouble()
                    }
                val calculatedValue = BigDecimal(valueToCalculate).add(BigDecimal(.1))
                val newStatsValue =
                    SavedStats(Statistic.WEIGHT, calculatedValue.toFloat(), operationTime, user.id)
                statsDao.createNewStats(listOf(newStatsValue))
            }
        }
    }
}

@Immutable
data class WeightInfo(
    val weight: Float,
    val targetWeight: String
) {

    val formattedWeight get() = weight fmt getStatFormatter(Statistic.WEIGHT)
}

sealed class HomeState {
    object Init : HomeState()
    object Loading : HomeState()
    object NoData : HomeState()
    data class DataLoaded(
        val currentUser: ApplicationUser,
        val mapOfStats: Map<String, List<SavedStats>> = mapOf(),
        val weightInfo: WeightInfo = WeightInfo(1f, ""),
        val progress: MeasurementsProgress
    ) : HomeState()
}

@Immutable
data class MeasurementsProgress(
    val submitDate: LocalDate,
    val partialProgress: Map<String, Float>
) {

    val summaryProgress: Float = partialProgress.values.sum()
    val valid = !partialProgress.isEmpty()
}


