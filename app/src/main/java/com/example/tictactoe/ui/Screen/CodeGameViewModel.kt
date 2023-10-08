package com.example.tictactoe.ui.Screen

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CodeGameViewModel {
    private val _onlineGameValuesUiState = MutableStateFlow(OnlineGameRememberedValues())
    val onlineGameValuesUiState: StateFlow<OnlineGameRememberedValues> = _onlineGameValuesUiState.asStateFlow()

    fun updateGameCode(newGameCode: String) {
        _onlineGameValuesUiState.update {
            it.copy(gameCode = newGameCode)
        }
    }
}

data class OnlineGameRememberedValues(
    var gameCode: String = "",
    var FinalScoreText: String = ""
)