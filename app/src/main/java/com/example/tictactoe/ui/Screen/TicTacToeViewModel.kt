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

    private var _uiState = MutableStateFlow(UiState())
    var uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setPlayers(player1: String = "", player2: String = "") {
        _uiState.update {
            it.copy(
                player1 = player1,
                player2 = player2
            )
        }
    }

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

    fun resetGame(isBotTurn: Boolean = true, times: Int) {
        _uiState.value = UiState(isenabled = isBotTurn)

        _uiState.update { currentState ->
            currentState.copy(
                player_Turn =
                if (times % 2 == 1) {
                    "O"
                } else {
                    "X"
                }
            )
        }
    }

    fun check_ToCheck() {
        _uiState.value.ToCheck = _uiState.value.times >= 4
    }

    fun botTurn(uiState: UiState) {

        val negativePlayer = when (uiState.player_Turn) {
            "X" -> "O"
            else -> "X"
        }

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
