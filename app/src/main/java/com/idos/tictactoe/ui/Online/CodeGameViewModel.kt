package com.idos.tictactoe.ui.Online

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CodeGameViewModel: ViewModel() {
    private val _onlineGameValuesUiState = MutableStateFlow(OnlineGameRememberedValues())
    val onlineGameValuesUiState: StateFlow<OnlineGameRememberedValues> = _onlineGameValuesUiState.asStateFlow()

    private val _enableState = MutableStateFlow(Enable())
    val enableState: StateFlow<Enable> = _enableState.asStateFlow()

    fun clearCode() {
        _onlineGameValuesUiState.value.gameCode = ""
    }

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

    fun removeGame (id: String, db: DatabaseReference, times: Int,  onSuccessListener: () -> Unit) {
        if(times < 50) {
            if (!db.child(id).removeValue().isSuccessful) {
                removeGame(id, db, times + 1,  onSuccessListener)
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccessListener()
                }
            }
        } else {
            viewModelScope.launch(Dispatchers.Main) {
                onSuccessListener()
            }
        }
    }

    fun playerQuit(player: String, databaseReference: DatabaseReference, id: String, times: Int = 0) {
        if (times < 50) {


            if (player == "X") {
                if (!databaseReference.child(id).child("player1Quit").setValue(true).isSuccessful) {
                    playerQuit(player, databaseReference, id, times + 1)
                }
            } else {
                if (!databaseReference.child(id).child("player2Quit").setValue(true).isSuccessful) {
                    playerQuit(player, databaseReference, id, times + 1)
                }
            }
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