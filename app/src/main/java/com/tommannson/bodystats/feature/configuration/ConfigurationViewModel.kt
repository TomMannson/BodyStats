package com.tommannson.bodystats.feature.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.infrastructure.configuration.ConfigDao
import com.tommannson.bodystats.infrastructure.configuration.SavedStatsConfiguration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    val dao: ConfigDao
) : ViewModel() {

    fun submitConfiguration(stats: SavedStatsConfiguration) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertAll(stats)
        }
    }
}