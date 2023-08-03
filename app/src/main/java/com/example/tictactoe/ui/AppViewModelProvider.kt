package com.example.tictactoe.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tictactoe.ui.Screen.SettingsViewModel
import com.example.tictactoe.ui.Screen.SignUpViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            SettingsViewModel()
        }

        initializer {
            SignUpViewModel()
        }
    }
}
