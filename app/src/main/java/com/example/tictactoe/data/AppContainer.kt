package com.example.tictactoe.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val playerRepository: PlayerRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflinePlayerRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    override val playerRepository: PlayerRepository by lazy {
        OfflinePlayerRepository(PlayerDatabase.getDatabase(context).playerDao())
    }
}