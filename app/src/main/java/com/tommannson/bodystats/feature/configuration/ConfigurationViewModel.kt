package com.tommannson.bodystats.feature.configuration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.feature.home.HomeState
import com.tommannson.bodystats.infrastructure.configuration.ApplicationUser
import com.tommannson.bodystats.infrastructure.configuration.Gender
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    val dao: UserDao
) : ViewModel() {

    private val _configurationState: MutableLiveData<ConfigurationState> = MutableLiveData()
    val configurationState: LiveData<ConfigurationState> = _configurationState

    fun initializeScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = dao.getAll().firstOrNull()
            _configurationState.postValue(ConfigurationState(ScreenState.DataLoaded, user))
        }
    }

    fun submit(name: String, height: Float, weight: Float, dreamWeight: Float, gender: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val currentUser = dao.getAll().firstOrNull()

            if (currentUser == null) {
                val user = ApplicationUser(name, height, weight, dreamWeight, Gender.FEMALE)
                dao.insertAll(user)
            } else {
                currentUser.copy(name = name, height = height, weight = weight, dreamWeight = dreamWeight, sex = gender)
                    .let { dao.updateUser(it) }
            }
        }
    }
}

data class ConfigurationState(
    val screenState: ScreenState,
    val applicationUser: ApplicationUser?
)

sealed class ScreenState {
    object Init: ScreenState()
    object DataLoaded: ScreenState()
    object Loading: ScreenState()
}
