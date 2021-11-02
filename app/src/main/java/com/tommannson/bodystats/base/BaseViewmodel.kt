package com.tommannson.bodystats.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.infrastructure.configuration.AppDatabase
import kotlinx.coroutines.*
import javax.inject.Inject

open class BaseViewmodel: ViewModel() {

    @Inject
    lateinit var database: AppDatabase

    fun invokeTransaction (unitOfWork: suspend () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            database.runInTransaction {
                runBlocking {
                    unitOfWork()
                }
            }
        }
    }

}