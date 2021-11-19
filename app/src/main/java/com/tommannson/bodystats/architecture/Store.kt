package com.tommannson.bodystats.architecture

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import org.reduxkotlin.Store


@Composable
fun <T> Store<T>.observeAsState(): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state = remember { mutableStateOf(state) }
    DisposableEffect(this, lifecycleOwner) {
        val observer = {
            val newValue = this@observeAsState.state
            if (newValue != state.value) {
                state.value = newValue
            }
        }
        val subscription = subscribe(observer)
        onDispose(subscription)
    }
    return state
}

class AppState {

}


