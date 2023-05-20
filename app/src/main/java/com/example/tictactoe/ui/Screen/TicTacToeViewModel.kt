package com.example.tictactoe.ui.Screen

import androidx.lifecycle.ViewModel
import com.example.tictactoe.data.UiState
import com.example.tictactoe.ui.WillWin
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

    fun botTurn(uiState: UiState) {

        when(WillWin(uiState = uiState)) {
            1 -> {_uiState.value.Box1 = "O"}
            2 -> {_uiState.value.Box2 = "O"}
            3 -> {_uiState.value.Box3 = "O"}
            4 -> {_uiState.value.Box4 = "O"}
            5 -> {_uiState.value.Box5 = "O"}
            6 -> {_uiState.value.Box6 = "O"}
            7 -> {_uiState.value.Box7 = "O"}
            8 -> {_uiState.value.Box8 = "O"}
            9 -> {_uiState.value.Box9 = "O"}
            else -> {
                if(_uiState.value.Box1 == "") {
                    _uiState.value.Box1 = "O"
                }
                else if(_uiState.value.Box2 == "") {
                    _uiState.value.Box2= "O"
                }
                else if(_uiState.value.Box3 == "") {
                    _uiState.value.Box3 = "O"
                }
                else if(_uiState.value.Box4 == "") {
                    _uiState.value.Box4 = "O"
                }
                else if(_uiState.value.Box5 == "") {
                    _uiState.value.Box5 = "O"
                }
                else if(_uiState.value.Box6 == "") {
                    _uiState.value.Box6 = "O"
                }
                else if(_uiState.value.Box7 == "") {
                    _uiState.value.Box7 = "O"
                }
                else if(_uiState.value.Box8 == "") {
                    _uiState.value.Box8 = "O"
                }
                else if(_uiState.value.Box9 == "") {
                    _uiState.value.Box9 = "O"
                }
            }
        }
        _uiState.value.times ++
        check_ToCheck()
        changePlayer()
    }
}
