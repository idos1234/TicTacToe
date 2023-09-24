package com.example.tictactoe.ui.Screen

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CodeGameViewModel {
    private val _codeGameUiState = MutableStateFlow(CodeGame())
    val codeGameUiState: StateFlow<CodeGame> = _codeGameUiState.asStateFlow()

    fun updateGameCode(newGameCode: String) {
        _codeGameUiState.update {
            it.copy(gameCode = newGameCode)
        }
    }
    fun openGame(openGame: Boolean) {
        _codeGameUiState.update {
            it.copy(openGame = openGame)
        }
    }
}

data class CodeGame(
    var gameCode: String = "",
    var openGame: Boolean = true
)