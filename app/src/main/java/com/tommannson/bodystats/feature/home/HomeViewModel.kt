package com.tommannson.bodystats.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.base.BaseViewmodel
import com.tommannson.bodystats.infrastructure.configuration.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.math.BigDecimal
import java.math.MathContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val userDao: UserDao,
    private val statsDao: StatsDao,
) : BaseViewmodel() {

    private val _state = MutableLiveData<HomeState>()
    val state: LiveData<HomeState> = _state

    fun initialiseData(statsToInit: List<String>) {

        val state = _state.value ?: HomeState(screenState = ScreenState.Loading)
        _state.postValue(state)

        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getAll().firstOrNull()

            if (user != null) {
                statsDao.getParamLive(user.id, statsToInit).collect {
                    val savedStats = statsDao.getParams(user.id, statsToInit)

                    val groupedStats = mutableMapOf<String, MutableList<SavedStats>>()

                    for (statItem in savedStats) {
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

                    val loadedWeightInfo = WeightInfo("$lastWeightLog", "${user.dreamWeight}")


                    _state.postValue(
                        state.copy(
                            ScreenState.DataLoaded,
                            user,
                            groupedStats,
                            loadedWeightInfo
                        )
                    )
                }

            } else {
                _state.postValue(state.copy(ScreenState.DataLoaded))
            }



            viewModelScope.launch {

            }
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
                val calculatedValue = BigDecimal(foundValue.toDouble()).add(BigDecimal(.1)).round(
                    MathContext(1)
                )
                statsDao.udateStats(listOf(foundWeight.copy(value = calculatedValue.toFloat())))
            } else {
                val calculatedValue = BigDecimal(user.weight.toDouble()).add(BigDecimal(.1))
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
                val calculatedValue = BigDecimal(user.weight.toDouble()).add(BigDecimal(.1))
                val newStatsValue =
                    SavedStats(Statistic.WEIGHT, calculatedValue.toFloat(), operationTime, user.id)
                statsDao.createNewStats(listOf(newStatsValue))
            }
        }
    }
}

data class HomeState(
    val screenState: ScreenState,
    val currentUser: ApplicationUser? = null,
    val mapOfStats: Map<String, List<SavedStats>> = mapOf(),
    val weightInfo: WeightInfo = WeightInfo("", ""),
)

data class WeightInfo(
    val weight: String,
    val targetWeight: String
) {

}

sealed class ScreenState {
    object Init : ScreenState()
    object Loading : ScreenState()
    object DataLoaded : ScreenState()
}