package com.example.tictactoe.data

import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllPlayersStream(): Flow<List<Player>>

    /**
     * Insert item in the data source
     */
    suspend fun insertIPlayer(item: Player)

    /**
     * Delete item from the data source
     */
    suspend fun deletePlayer(item: Player)

    /**
     * Update item in the data source
     */
    suspend fun updatePlayer(item: Player)
}