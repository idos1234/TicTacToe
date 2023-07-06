package com.example.tictactoe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single player in the database.
 */

@Entity(tableName = "players")
data class Player (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    var score: Int = 0,
    val password: String? = null
)