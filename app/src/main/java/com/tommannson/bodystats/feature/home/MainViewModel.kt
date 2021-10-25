package com.tommannson.bodystats.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.infrastructure.configuration.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDao: UserDao,
    private val configDao: ConfigDao,
    private val nextDao: NextDao,
    private val db: AppDatabase
) : ViewModel() {

    val _listResult = MutableStateFlow(emptyList<String>())
    val listResult: StateFlow<List<String>> = _listResult

    fun prepareScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            db.runInTransaction {
                val result = userDao.getAll();

                if (result.isEmpty()) {
                    val newUser = ApplicationUser("Gosia", 165.0f, 70.0f, 0);
                    userDao.insertAll(newUser)
                    val config = SavedStatsConfiguration(
                        1.0f,
                        1.0f,
                        1.0f,
                        1.0f,
                        1.0f,
                        1.0f,
                        1.0f,
                        1.0f,
                        1
                    )
                    configDao.insertAll(config)
                }

                val stats = configDao.getAll()
                val next = nextDao.getAll();

                _listResult.tryEmit(prepareList(stats))
            }
        }
    }

    private fun prepareList(stats: List<SavedStatsConfiguration>): List<String> {
        return stats.map { it.waga.toString() }
    }
}


