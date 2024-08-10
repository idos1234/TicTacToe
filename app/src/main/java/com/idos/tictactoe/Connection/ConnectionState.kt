package com.example.network.Connection

sealed class ConnectionState {
    object Available: ConnectionState()
    object UnAvailable: ConnectionState()
}