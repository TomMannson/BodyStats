package com.tommannson.bodystats.feature.configuration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.infrastructure.configuration.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    val dao: UserDao,
    val statsDao: StatsDao
) : ViewModel() {

    private val _configurationState: MutableLiveData<ConfigurationState> = MutableLiveData()
    val configurationState: LiveData<ConfigurationState> = _configurationState

    private val _events = MutableSharedFlow<Events>()
    val events = _events.asSharedFlow()

    fun initializeScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = dao.getAll().firstOrNull()
            _configurationState.postValue(
                ConfigurationState(
                    ScreenState.DataLoaded,
                    user,
                    user == null
                )
            )
        }
    }

    fun submit(name: String, height: String, weight: String, dreamWeight: String, gender: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val currentUser = dao.getAll().firstOrNull()

            try {
                if (currentUser == null) {
                    val user = ApplicationUser(
                        name,
                        height.toFloat(),
                        weight.toFloat(),
                        dreamWeight.toFloat(),
                        gender
                    )
                    val ids = dao.insertAll(user)
                    val newWeight =
                        SavedStats(Statistic.WEIGHT, weight.toFloat(), LocalDate.now(), ids[0])
                    statsDao.createNewStats(listOf(newWeight))
                } else {
                    currentUser.copy(
                        name = name,
                        height = height.toFloat(),
                        weight = weight.toFloat(),
                        dreamWeight = dreamWeight.toFloat(),
                        sex = gender
                    )

                        .let { dao.updateUser(it) }
                }
                _events.emit(Events.Created)
            } catch (ex: Exception) {
                _events.emit(Events.Error("Wypełnij wszystkie pola"))
            }
        }
    }
}

data class ConfigurationState(
    val screenState: ScreenState,
    val applicationUser: ApplicationUser?,
    val validForm: Boolean = false
)

sealed class ScreenState {
    object Init : ScreenState()
    object DataLoaded : ScreenState()
}

sealed class Events {
    class Error(val message: String) : Events()
    object Created : Events()
}

