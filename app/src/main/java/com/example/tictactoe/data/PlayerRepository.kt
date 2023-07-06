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
    suspend fun insertIPlayer(player: Player)

    /**
     * Delete player from the data source
     */
    suspend fun deletePlayer(player: Player)

    /**
     * Delete all players from the data source
     */
    suspend fun clearData()

    /**
     * Update player in the data source
     */
    suspend fun updatePlayer(player: Player)

    /**
     * Update player score in the data source
     */
    suspend fun updatePlayerScore(id: Int, score: Int)
}