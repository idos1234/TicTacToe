package com.example.tictactoe

import android.app.Application
import com.example.tictactoe.data.AppContainer
import com.example.tictactoe.data.AppDataContainer

class PlayerApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}