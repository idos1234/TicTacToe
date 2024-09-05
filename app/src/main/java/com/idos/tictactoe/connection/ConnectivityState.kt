package com.idos.tictactoe.connection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext

@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current
    return produceState(context.currentConnectivityState) {
        context.observeConnectivityAsFlow().collect {
            value = it
        }
    }
}