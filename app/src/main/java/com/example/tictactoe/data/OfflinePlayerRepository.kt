package com.example.tictactoe.data

import kotlinx.coroutines.flow.Flow

class OfflinePlayerRepository(private val playerDao: PlayerDao) : PlayerRepository {
    override fun getAllPlayersStream(): Flow<List<Player>> {
        return playerDao.getAllPlayers()
    }

    override suspend fun clearData() = playerDao.clearData()

    override suspend fun updatePlayer(player: Player) = playerDao.update(player)

    override suspend fun insertIPlayer(player: Player) = playerDao.insert(player)

    override suspend fun deletePlayer(player: Player) = playerDao.delete(player)

    override suspend fun updatePlayerScore(id: Int, score: Int) = playerDao.updatePlayerScore(id, score)
}