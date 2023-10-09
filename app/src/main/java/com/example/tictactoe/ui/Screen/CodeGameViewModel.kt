package com.example.tictactoe.ui.Screen

import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.data.OnlineGameUiState
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

    fun updateFinalScoreScreenData(Text: String, game: OnlineGameUiState, player1: MainPlayerUiState, player2: MainPlayerUiState) {
        _onlineGameValuesUiState.update {
            it.copy(
                FinalScoreText = Text,
                game = game,
                player1 = player1,
                player2 = player2
            )
        }
    }
}

data class OnlineGameRememberedValues(
    var gameCode: String = "",
    var FinalScoreText: String = "",
    var game: OnlineGameUiState = OnlineGameUiState(),
    var player1: MainPlayerUiState = MainPlayerUiState(),
    var player2: MainPlayerUiState = MainPlayerUiState()
)