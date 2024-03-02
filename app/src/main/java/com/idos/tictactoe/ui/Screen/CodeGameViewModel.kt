package com.idos.tictactoe.ui.Screen

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CodeGameViewModel: ViewModel() {
    private val _onlineGameValuesUiState = MutableStateFlow(OnlineGameRememberedValues())
    val onlineGameValuesUiState: StateFlow<OnlineGameRememberedValues> = _onlineGameValuesUiState.asStateFlow()

    private val _enableState = MutableStateFlow(Enable())
    val enableState: StateFlow<Enable> = _enableState.asStateFlow()

    fun changeEnable(isEnable: Boolean) {
        _enableState.update {
            it.copy(enable = isEnable)
        }
    }

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

    fun removeGame (id: String, db: DatabaseReference, onSuccessListener: () -> Unit) {
        db.child(id).removeValue().addOnCompleteListener {
            onSuccessListener()
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

data class  Enable(
    var enable: Boolean = true
)