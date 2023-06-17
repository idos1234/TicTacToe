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

    @Delete
    suspend fun delete(player: Player)


    @Query("DELETE FROM players")
    suspend fun clearData()
}