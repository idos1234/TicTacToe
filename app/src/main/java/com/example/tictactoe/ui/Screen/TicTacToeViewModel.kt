package com.example.tictactoe.ui.Screen

import androidx.lifecycle.ViewModel
import com.example.tictactoe.data.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TicTacToeViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun changePlayer() {
        _uiState.update {currentState ->
            currentState.copy(player_Turn =
            when (currentState.player_Turn) {
                "X" -> "O"
                else -> "X"
            })
        }
    }
}
