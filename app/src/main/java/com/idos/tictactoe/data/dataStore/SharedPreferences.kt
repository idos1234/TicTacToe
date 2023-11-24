package com.idos.tictactoe.data.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.idos.tictactoe.ui.Screen.sharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// shared preferences class - not encrypted
class SharedPreferencesDataStore(context: Context) {

    // data values
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingPrefs")
        val messageSent = booleanPreferencesKey("MessageSent")
        val lastTimeSeen = longPreferencesKey("LastTimeSeen")
        val messagingSendingTime = longPreferencesKey("MessagingSendingTime")
    }
    var pref = context.dataStore
    fun setMessageSent(MessageSent: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            pref.edit {
                it[messageSent] = MessageSent
            }
        }
    }
    fun setLastTimeSeen(LastTimeSeen: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            pref.edit {
                it[lastTimeSeen] = LastTimeSeen
            }
        }
    }
    fun setMessagingSendingTime(MessagingSendingTime: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            pref.edit {
                it[messagingSendingTime] = MessagingSendingTime
            }
        }
    }

    fun getDetails() = pref.data.map {
        sharedPreferences(
            lastTimeSeen = it[lastTimeSeen]?:0,
            messageSent = it[messageSent]?:false,
            messagingSendingTime = it[messagingSendingTime]?:0
        )
    }
}