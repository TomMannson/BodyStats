package com.tommannson.bodystats.feature.createstats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.base.BaseViewmodel
import com.tommannson.bodystats.feature.home.ScreenState
import com.tommannson.bodystats.infrastructure.configuration.BASIC_PARAMS
import com.tommannson.bodystats.infrastructure.configuration.SavedStats
import com.tommannson.bodystats.infrastructure.configuration.StatsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class CreateStatsViewmodel
@Inject constructor(
    val statsDao: StatsDao
) : BaseViewmodel() {


    private val _state = MutableLiveData(State())
    val state: LiveData<State> = _state


    fun initialiseData(paramsToLoad: List<String>) {
        val localState = _state.value!!

        val localValuesCopy = localState.valuesToSave.toMutableMap()

        viewModelScope.launch(Dispatchers.IO) {
            val result = statsDao.getLastParams(1, paramsToLoad)
            if (!result.isEmpty()) {
                for (item in result) {
                    localValuesCopy[item.statName] = item.value
                }
            }
            _state.postValue(
                localState.copy(
                    viewStateMachine = ScreenState.DataLoaded,
                    valuesToSave = localValuesCopy,
                    orderOfItemsToSave = paramsToLoad
                )
            )
        }
    }

    fun goToNext() {
        val currentStep = _state.value!!.selectedStep
        if (currentStep < _state.value!!.orderOfItemsToSave.size - 1) {
            _state.postValue(_state.value!!.copy(selectedStep = currentStep + 1))
        } else {
            saveValues()
        }
    }

    fun goToPrevious() {
        val currentStep = _state.value!!.selectedStep
        if (currentStep > 0) {
            _state.postValue(_state.value!!.copy(selectedStep = currentStep - 1))
        }
    }

    fun setValue(value: Float) {
        val localValue = _state.value!!
        val valuesCopy = localValue.valuesToSave.toMutableMap()
        valuesCopy[localValue.currentParamKey] = value
        _state.postValue(localValue.copy(valuesToSave = valuesCopy))
    }

    fun saveValues() {
        val localState = state.value as State
        invokeTransaction {
            val now = LocalDate.now()
            val savedValues = statsDao.getParamsForDate(1, now, localState.orderOfItemsToSave)

            if (savedValues.isEmpty()) {
                saveFirstTime(now)
            } else {
                updateTime(savedValues)
            }
        }
    }

    private fun updateTime(itemsToUpdate: List<SavedStats>) {
        val localState = _state.value?.valuesToSave
        val statsToSave = mutableListOf<SavedStats>()

        for (item in itemsToUpdate) {
            val value: Float = localState!![item.statName] as Float
            val statsInfo = item.copy(value = value)
            statsToSave.add(statsInfo)
        }
        statsDao.udateStats(statsToSave)
    }

    private fun saveFirstTime(dateOfSubmit: LocalDate) {
        val localState = _state.value?.valuesToSave
        val statsToSave = mutableListOf<SavedStats>()

        for (param in (_state.value?.orderOfItemsToSave ?: arrayListOf())) {
            val value: Float = localState!![param] ?: 0f
            val statsInfo = SavedStats(param, value, dateOfSubmit, 1)
            statsToSave.add(statsInfo)
        }
        statsDao.createNewStats(statsToSave)
    }

    fun decreaseCurrent() {
        val localValue = _state.value!!
        val localState = localValue.valuesToSave.toMutableMap()
        val newValue =
            localState[localValue.currentParamKey]?.toBigDecimal()?.subtract(.1.toBigDecimal())
                ?: BigDecimal.ZERO
        localState[localValue.currentParamKey] = newValue.toFloat()
        _state.postValue(localValue.copy(valuesToSave = localState))
    }

    fun increaseCurrent() {
        val localValue = _state.value!!
        val localState = localValue.valuesToSave.toMutableMap()
        val newValue =
            localState[localValue.currentParamKey]?.toBigDecimal()?.add(.1.toBigDecimal())
                ?: BigDecimal.ZERO
        localState[localValue.currentParamKey] = newValue.toFloat()
        _state.postValue(localValue.copy(valuesToSave = localState))
    }
}

data class State(
    val selectedStep: Int = 0,
    val valuesToSave: MutableMap<String, Float> = mutableMapOf(),
    val orderOfItemsToSave: List<String> = listOf(),
    val viewStateMachine: ScreenState = ScreenState.Init
) {
    val currentValue get() = valuesToSave[orderOfItemsToSave[selectedStep]] ?: 0
    val currentParamKey get() = orderOfItemsToSave[selectedStep]
    val paramUnit get() = getStatUnit(currentParamKey)
}

fun getStatUnit(stat: String) = when(stat){
        in BASIC_PARAMS -> "cm"
        else -> ""
    }

fun Float.round(decimals: Int): Float {
    val decimal = BigDecimal(this.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
    return decimal.toFloat()
}