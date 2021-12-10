package com.tommannson.bodystats.architecture.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

//tivi
abstract class FlowTracker<P : Any, T>(initialisationDispatcher: CoroutineDispatcher = Dispatchers.Main) {
    // Ideally this would be buffer = 0, since we use flatMapLatest below, BUT invoke is not
    // suspending. This means that we can't suspend while flatMapLatest cancels any
    // existing flows. The buffer of 1 means that we can use tryEmit() and buffer the value
    // instead, resulting in mostly the same result.
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow: Flow<T> = paramState
        .distinctUntilChanged()
        .flowOn(initialisationDispatcher)
        .flatMapLatest { createObservable(it) }
        .distinctUntilChanged()

    fun trackWithParam(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>
}

abstract class NoParamsFlowTracker<T>(initialisationDispatcher: CoroutineDispatcher = Dispatchers.Main) {
    // Ideally this would be buffer = 0, since we use flatMapLatest below, BUT invoke is not
    // suspending. This means that we can't suspend while flatMapLatest cancels any
    // existing flows. The buffer of 1 means that we can use tryEmit() and buffer the value
    // instead, resulting in mostly the same result.
    private val paramState = MutableSharedFlow<Param>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow: Flow<T> = paramState
        .distinctUntilChanged()
        .flatMapLatest {

//            runBlocking {
//                async(initialisationDispatcher) {
                    createObservable()
//                }.await()
//            }
        }
        .distinctUntilChanged()

    fun track() {
        paramState.tryEmit(Param)
    }

    protected abstract fun createObservable(): Flow<T>

    object Param
}