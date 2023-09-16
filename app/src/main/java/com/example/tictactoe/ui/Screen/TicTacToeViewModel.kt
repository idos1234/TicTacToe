package com.example.tictactoe.ui.Screen

import androidx.lifecycle.ViewModel
import com.example.tictactoe.data.UiState
import com.example.tictactoe.ui.WillWin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * The viewModel for the [TicTacToeScreen] and the [TicTacToeSinglePlayerScreen]
 */

class TicTacToeViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun changePlayer() {
        _uiState.update { currentState ->
            currentState.copy(
                player_Turn =
                when (currentState.player_Turn) {
                    "X" -> "O"
                    else -> "X"
                }
            )
        }
    }

    fun resetGame(times: Int) {
        _uiState.value = UiState(player_Turn = if (times % 2 == 1) { "O" } else { "X" }
        )
    }

    fun onClick() {
        _uiState.update {
            it.copy(
                times = it.times + 1
            )
        }
        check_ToCheck()
        changePlayer()
    }

    fun onSinglePlayerClick() {
        _uiState.update {
            it.copy(
                times = it.times + 1 ,
                isenabled = false
            )
        }
    }

    fun check_ToCheck() {
        _uiState.update {
            it.copy(ToCheck = _uiState.value.times >= 4)
        }
    }

    fun SetBox(box: Int) {

        _uiState.update {
            when(box) {
                1 -> it.copy(Box1 = _uiState.value.player_Turn)
                2 -> it.copy(Box2 = _uiState.value.player_Turn)
                3 -> it.copy(Box3 = _uiState.value.player_Turn)
                4 -> it.copy(Box4 = _uiState.value.player_Turn)
                5 -> it.copy(Box5 = _uiState.value.player_Turn)
                6 -> it.copy(Box6 = _uiState.value.player_Turn)
                7 -> it.copy(Box7 = _uiState.value.player_Turn)
                8 -> it.copy(Box8 = _uiState.value.player_Turn)
                9 -> it.copy(Box9 = _uiState.value.player_Turn)
                else -> it.copy()
            }
        }
    }

    fun botTurn(uiState: UiState) {

        val negativePlayer = when (uiState.player_Turn) {
            "X" -> "O"
            else -> "X"
        }

        //if bot can win
        when (WillWin(uiState = uiState, player = uiState.player_Turn)) {
            1 -> {
                _uiState.value.Box1 = uiState.player_Turn
            }
            2 -> {
                _uiState.value.Box2 = uiState.player_Turn
            }
            3 -> {
                _uiState.value.Box3 = uiState.player_Turn
            }
            4 -> {
                _uiState.value.Box4 = uiState.player_Turn
            }
            5 -> {
                _uiState.value.Box5 = uiState.player_Turn
            }
            6 -> {
                _uiState.value.Box6 = uiState.player_Turn
            }
            7 -> {
                _uiState.value.Box7 = uiState.player_Turn
            }
            8 -> {
                _uiState.value.Box8 = uiState.player_Turn
            }
            9 -> {
                _uiState.value.Box9 = uiState.player_Turn
            }
            else -> {

                //if bot need to defend
                when (WillWin(uiState = uiState, player = negativePlayer)) {
                    1 -> {
                        _uiState.value.Box1 = uiState.player_Turn
                    }
                    2 -> {
                        _uiState.value.Box2 = uiState.player_Turn
                    }
                    3 -> {
                        _uiState.value.Box3 = uiState.player_Turn
                    }
                    4 -> {
                        _uiState.value.Box4 = uiState.player_Turn
                    }
                    5 -> {
                        _uiState.value.Box5 = uiState.player_Turn
                    }
                    6 -> {
                        _uiState.value.Box6 = uiState.player_Turn
                    }
                    7 -> {
                        _uiState.value.Box7 = uiState.player_Turn
                    }
                    8 -> {
                        _uiState.value.Box8 = uiState.player_Turn
                    }
                    9 -> {
                        _uiState.value.Box9 = uiState.player_Turn
                    }
                    //random box
                    else -> {
                        if (_uiState.value.Box1 == "") {
                            _uiState.value.Box1 = uiState.player_Turn
                        } else if (_uiState.value.Box2 == "") {
                            _uiState.value.Box2 = uiState.player_Turn
                        } else if (_uiState.value.Box3 == "") {
                            _uiState.value.Box3 = uiState.player_Turn
                        } else if (_uiState.value.Box4 == "") {
                            _uiState.value.Box4 = uiState.player_Turn
                        } else if (_uiState.value.Box5 == "") {
                            _uiState.value.Box5 = uiState.player_Turn
                        } else if (_uiState.value.Box6 == "") {
                            _uiState.value.Box6 = uiState.player_Turn
                        } else if (_uiState.value.Box7 == "") {
                            _uiState.value.Box7 = uiState.player_Turn
                        } else if (_uiState.value.Box8 == "") {
                            _uiState.value.Box8 = uiState.player_Turn
                        } else if (_uiState.value.Box9 == "") {
                            _uiState.value.Box9 = uiState.player_Turn
                        }
                    }
                }
            }
        }
        uiState.times++
        uiState.isenabled = true
        check_ToCheck()
        changePlayer()
    }
}
