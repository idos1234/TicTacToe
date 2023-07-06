package com.example.tictactoe.ui

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tictactoe.ui.Screen.SettingsViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.example.tictactoe.PlayerApplication
import com.example.tictactoe.ui.Screen.SignUpViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            SettingsViewModel(
                playerApplication().container.playerRepository
            )
        }

        initializer {
            SignUpViewModel(playerApplication().container.playerRepository)
        }
    }
}

fun CreationExtras.playerApplication(): PlayerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PlayerApplication)
