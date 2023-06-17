package com.example.tictactoe.data

import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    /**
     * Retrieve all the players from the the given data source.
     */
    fun getAllPlayersStream(): Flow<List<Player>>

    /**
     * Insert player in the data source
     */
    suspend fun insertIPlayer(item: Player)

    /**
     * Delete player from the data source
     */
    suspend fun deletePlayer(item: Player)

    /**
     * Delete all players from the data source
     */
    suspend fun clearData()

    /**
     * Update player in the data source
     */
    suspend fun updatePlayer(item: Player)
}