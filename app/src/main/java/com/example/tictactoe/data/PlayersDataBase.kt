package com.example.tictactoe.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Player::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class PlayerDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile
        private var Instance: PlayerDatabase? = null

        fun getDatabase(context: Context): PlayerDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PlayerDatabase::class.java, "player_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}