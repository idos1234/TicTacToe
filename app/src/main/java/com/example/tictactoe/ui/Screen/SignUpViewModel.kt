package com.example.tictactoe.ui.Screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.tictactoe.data.*

@ViewModelFactoryDsl
class SignUpViewModel(private val playerRepository: PlayerRepository): ViewModel() {

    var playerUiState by mutableStateOf(MainPlayerUiState())
        private set


    fun updateUiState(newPlayerUiState: MainPlayerUiState) {
        playerUiState = newPlayerUiState.copy()
    }

    suspend fun savePlayer() {
        if (playerUiState.isValid()) {
            playerRepository.insertIPlayer(playerUiState.toPlayer())
        }
    }

    suspend fun updateScore(player: Player) {
        playerRepository.updatePlayerScore(id = player.id, score = player.score + 1)
    }


    suspend fun clearData() {
        playerRepository.clearData()
    }
}

