package com.tommannson.bodystats.feature.previewstats

import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.base.BaseViewmodel
import com.tommannson.bodystats.feature.home.ScreenState
import com.tommannson.bodystats.infrastructure.configuration.FULL_LIST_OF_STATS
import com.tommannson.bodystats.infrastructure.configuration.SavedStats
import com.tommannson.bodystats.infrastructure.configuration.StatsDao
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewStatsViewmodel
@Inject constructor(
    val statsDao: StatsDao,
    val userDao: UserDao,
) : BaseViewmodel() {

    val _state = MutableStateFlow(State(mapOf()))
    val state: StateFlow<State> = _state

    fun initPreview() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getAll().firstOrNull()

            if (user != null) {
                statsDao.getParamLive(user.id, FULL_LIST_OF_STATS).collect {

                    val groupedStats = mutableMapOf<String, MutableList<SavedStats>>()

                    for (statItem in it) {
                        val foundList = groupedStats[statItem.statName] ?: mutableListOf()
                        foundList.add(statItem)
                        groupedStats[statItem.statName] = foundList
                    }

                    _state.emit(
                        state.value.copy(
                            groupedStats,
                            stateMachine = ScreenState.DataLoaded
                        )
                    )
                }

            }
        }
    }


}

data class State(
    val groupded: Map<String, List<SavedStats>>,
    val selected: Int = 0,
    val stateMachine: ScreenState = ScreenState.Init
)