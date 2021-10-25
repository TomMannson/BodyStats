package com.tommannson.bodystats.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tommannson.bodystats.base.BaseViewmodel
import com.tommannson.bodystats.feature.home.model.CurrentUserInfo

class HomeViewModel : BaseViewmodel() {



    val _currentUser = MutableLiveData<HomeState>()
    val currentUser: LiveData<HomeState> = _currentUser

    fun initialiseData(){

    }
}

class HomeState(
    val screenState: ScreenState,
    val currentUser: CurrentUserInfo?,
)

sealed class ScreenState {
    object Init : ScreenState()
    object Loading : ScreenState()
    object DataLoaded : ScreenState()
}