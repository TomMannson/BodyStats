package com.tommannson.bodystats.feature.home

import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.base.BaseViewmodel
import com.tommannson.bodystats.feature.createstats.getStatFormatter
import com.tommannson.bodystats.infrastructure.configuration.*
import com.tommannson.bodystats.utils.fmt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.math.BigDecimal
import javax.inject.Inject


@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val userDao: UserDao,
    private val statsDao: StatsDao,
) : BaseViewmodel() {

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
            val pointOfQuery = LocalDate.now().minusDays(60)
            subscription?.cancel()
            subscription = statsDao.getParamLive(user.id!!, statsToInit, pointOfQuery)
                .onEach {


                val groupedStats = mutableMapOf<String, MutableList<SavedStats>>()

                for (statItem in it) {
                    val foundList = groupedStats[statItem.statName] ?: mutableListOf()
                    foundList.add(statItem)
                    groupedStats[statItem.statName] = foundList
                }

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
                    loadedWeightInfo
                )
            }.launchIn(viewModelScope)
        }
    }

    fun decreaseWeight() {
        invokeTransaction {
            val user = userDao.getAll().firstOrNull() as ApplicationUser
            val operationTime = LocalDate.now()
            val foundWeight =
                statsDao.getParamInfo(user.id!!, operationTime, listOf(Statistic.WEIGHT))

            if (foundWeight != null) {
                val foundValue = foundWeight.value
                val calculatedValue = BigDecimal(foundValue.toDouble()).subtract(BigDecimal(.1))
                statsDao.udateStats(listOf(foundWeight.copy(value = calculatedValue.toFloat())))
            } else {
                val calculatedValue = BigDecimal(user.weight.toDouble()).subtract(BigDecimal(.1))
                val newStatsValue =
                    SavedStats(Statistic.WEIGHT, calculatedValue.toFloat(), operationTime, user.id!!)
                statsDao.createNewStats(listOf(newStatsValue))
            }
        }
    }

    fun increaseWeight() {
        invokeTransaction {
            val user = userDao.getAll().firstOrNull() as ApplicationUser
            val operationTime = LocalDate.now()
            val foundWeight =
                statsDao.getParamInfo(user.id!!, operationTime, listOf(Statistic.WEIGHT))

            if (foundWeight != null) {
                val foundValue = foundWeight.value
                val calculatedValue = BigDecimal(foundValue.toDouble()).add(BigDecimal(.1))
                statsDao.udateStats(listOf(foundWeight.copy(value = calculatedValue.toFloat())))
            } else {
                val calculatedValue = BigDecimal(user.weight.toDouble()).add(BigDecimal(.1))
                val newStatsValue =
                    SavedStats(Statistic.WEIGHT, calculatedValue.toFloat(), operationTime, user.id!!)
                statsDao.createNewStats(listOf(newStatsValue))
            }
        }
    }
}

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
    ) : HomeState()
}