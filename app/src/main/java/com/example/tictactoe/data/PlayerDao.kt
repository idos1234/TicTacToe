package com.example.tictactoe.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Query("SELECT * from players ORDER BY id ASC")
    fun getAllPlayers(): Flow<List<Player>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player)

    @Update
    suspend fun update(player: Player)

    @Query(
        "UPDATE players " + "SET score = :score " + "WHERE id = :id"
    )
    suspend fun updatePlayerScore(id: Int, score: Int)

    @Delete
    suspend fun delete(player: Player)


    @Query("DELETE FROM players")
    suspend fun clearData()
}