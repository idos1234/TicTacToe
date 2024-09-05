package com.idos.tictactoe.connection

sealed class ConnectionState {
    object Available: ConnectionState()
    object UnAvailable: ConnectionState()
}