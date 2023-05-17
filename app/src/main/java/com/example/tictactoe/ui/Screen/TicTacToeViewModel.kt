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

    fun check_ToCheck(){
        _uiState.value.ToCheck = _uiState.value.times >= 4
    }

    fun botTurn() {
        Thread.sleep(1500)


        if(uiState.value.Box1 == "") {
            uiState.value.Box1 = "O"
        }
        else if(uiState.value.Box2 == "") {
            uiState.value.Box2= "O"
        }
        else if(uiState.value.Box3 == "") {
            uiState.value.Box3 = "O"
        }
        else if(uiState.value.Box4 == "") {
            uiState.value.Box4 = "O"
        }
        else if(uiState.value.Box5 == "") {
            uiState.value.Box5 = "O"
        }
        else if(uiState.value.Box6 == "") {
            uiState.value.Box6 = "O"
        }
        else if(uiState.value.Box7 == "") {
            uiState.value.Box7 = "O"
        }
        else if(uiState.value.Box8 == "") {
            uiState.value.Box8 = "O"
        }
        else if(uiState.value.Box9 == "") {
            uiState.value.Box9 = "O"
        }
    }
}
