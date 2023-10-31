package com.idos.tictactoe.ui.Screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.idos.tictactoe.data.MainPlayerUiState

@ViewModelFactoryDsl
class SignUpViewModel(): ViewModel() {

    var verifiedPassword = mutableStateOf("")

    var playerUiState by mutableStateOf(MainPlayerUiState())
        private set

    var signUpName by mutableStateOf(SignUpName())
        private set

    fun updateEmail(newEmail: SignUpName) {
        signUpName = newEmail.copy()
    }

    fun updateUiState(newPlayerUiState: MainPlayerUiState) {
        playerUiState = newPlayerUiState.copy()
    }

    fun clearPlayer() {
        playerUiState = MainPlayerUiState()
    }
}


data class SignUpName(
    var name: String = "",
    var name2: String = "",
)