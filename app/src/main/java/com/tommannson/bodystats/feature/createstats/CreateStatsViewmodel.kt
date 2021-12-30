package com.tommannson.bodystats.feature.createstats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.tommannson.bodystats.base.BaseViewmodel
import com.tommannson.bodystats.feature.configuration.ScreenState
import com.tommannson.bodystats.infrastructure.ApplicationUser
import com.tommannson.bodystats.infrastructure.SavedStats
import com.tommannson.bodystats.infrastructure.configuration.StatsDao
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import com.tommannson.bodystats.model.paramrecalculation.ProcessRecalculator
import com.tommannson.bodystats.model.statistics.getStatFormatterModification
import com.tommannson.bodystats.model.statistics.getStatShift
import com.tommannson.bodystats.model.statistics.getStatUnit
import com.tommannson.bodystats.utils.fmt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CreateStatsViewmodel
@Inject constructor(
    val statsDao: StatsDao,
    val userDao: UserDao
) : BaseViewmodel() {

    private val _state = MutableLiveData(State())
    val state: LiveData<State> = _state
    lateinit var navController: NavController
    val recalculator = ProcessRecalculator()
    lateinit var user: ApplicationUser

    fun initialiseData(
        paramsToMeasure: List<String>,
        nav: NavController
    ) {
        this.navController = nav
        val localState = _state.value!!

        val localValuesCopy = localState.valuesToSave.toMutableMap()

        viewModelScope.launch(Dispatchers.IO) {
            user = userDao.getAll().first()
            val result = statsDao.getLastParams(user.id, paramsToMeasure)
            if (!result.isEmpty()) {
                for (item in result) {
                    localValuesCopy[item.statName] = item.value
                }
            }
            _state.postValue(
                localState.copy(
                    viewStateMachine = ScreenState.DataLoaded,
                    valuesToSave = localValuesCopy,
                    orderOfItemsToSave = paramsToMeasure,
                    invalidData = false,
                    user = user
                )
            )
        }
    }

    fun goToNext() {
        val currentStep = _state.value!!.selectedStep
        val orderOfItemsToSave = _state.value!!.orderOfItemsToSave
        val values = _state.value?.valuesToSave ?: mapOf()
        if (currentStep < _state.value!!.orderOfItemsToSave.size - 1) {
            val nextValue = values[orderOfItemsToSave[currentStep + 1]] == null
            _state.postValue(
                _state.value!!.copy(
                    selectedStep = currentStep + 1,
                    invalidData = nextValue
                )
            )
        } else {
            saveValues()
        }
    }

    fun goToPrevious() {
        val currentStep = _state.value!!.selectedStep
        if (currentStep > 0) {
            _state.postValue(
                _state.value!!.copy(
                    selectedStep = currentStep - 1,
                    invalidData = false
                )
            )
        }
    }

    fun setValue(value: String) {
        try {
            val newValue = value.replace(",", ".").toFloat()
            val localValue = _state.value!!
            val valuesCopy = localValue.valuesToSave.toMutableMap()
            valuesCopy[localValue.currentParamKey] = newValue
            recalculator.performCalculations(localValue.currentParamKey, valuesCopy, user = user)
            _state.postValue(localValue.copy(valuesToSave = valuesCopy, invalidData = false))
        } catch (ex: Exception) {
            lockNext()
        }

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
            withContext(Dispatchers.Main) {
                navController.popBackStack()
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
        changeCurrentValue(getStatShift(localValue.currentParamKey) * (-1))
    }

    fun increaseCurrent() {
        val localValue = _state.value!!
        changeCurrentValue(getStatShift(localValue.currentParamKey))
    }

    fun changeCurrentValue(difference: Double) {
        val localValue = _state.value!!
        val localState = localValue.valuesToSave.toMutableMap()
        val newValue =
            localState[localValue.currentParamKey]?.toBigDecimal()?.add(difference.toBigDecimal())
                ?: BigDecimal.ZERO
        localState[localValue.currentParamKey] = newValue.toFloat()
        recalculator.performCalculations(localValue.currentParamKey, localState, user = user)
        _state.postValue(localValue.copy(valuesToSave = localState, invalidData = false))
    }

    fun lockNext() {
        val localValue = _state.value!!
        _state.postValue(localValue.copy(invalidData = true))
    }
}

data class State(
    val selectedStep: Int = 0,
    val valuesToSave: MutableMap<String, Float> = mutableMapOf(),
    val orderOfItemsToSave: List<String> = listOf(),
    val viewStateMachine: ScreenState = ScreenState.Init,
    val invalidData: Boolean = true,
    val user: ApplicationUser? = null
) {
    val currentValue get() = valuesToSave[orderOfItemsToSave[selectedStep]]
    val currentValueText get() = currentValue fmt formatter
    val currentParamKey get() = orderOfItemsToSave[selectedStep]
    val paramUnit get() = getStatUnit(currentParamKey)
    val formatter get() = getStatFormatterModification(currentParamKey)
    val nextButtonText get() = if (selectedStep < orderOfItemsToSave.size - 1) "Następny" else "Zakończ"
}

