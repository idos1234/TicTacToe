package com.example.tictactoe.data

import android.content.Context

interface AppContainer {
    val playerRepository: PlayerRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val playerRepository: PlayerRepository by lazy {
        OfflinePlayerRepository(PlayerDatabase.getDatabase(context).playerDao())
    }
}