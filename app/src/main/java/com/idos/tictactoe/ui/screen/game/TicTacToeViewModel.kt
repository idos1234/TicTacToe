package com.idos.tictactoe.ui.screen.game

import androidx.lifecycle.ViewModel
import com.idos.tictactoe.data.UiState
import com.idos.tictactoe.ui.willWin
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
                playerTurn =
                when (currentState.playerTurn) {
                    "X" -> "O"
                    else -> "X"
                }
            )
        }
    }

    fun resetGame(times: Int) {
        _uiState.value = UiState(
            playerTurn = if (times % 2 == 1) { "O" } else { "X" },
            isEnabled = times % 2 == 0,
            player1Score = uiState.value.player1Score,
            player2Score = uiState.value.player2Score
        )
    }

    fun updateScore(player: Int) {
        when(player) {
            1 -> _uiState.update { it.copy(player1Score = it.player1Score+1) }
            2 -> _uiState.update { it.copy(player2Score = it.player2Score+1) }
        }
        _uiState.update {
            it.copy(isScoreUpdated = true)
        }
    }

    fun onClick() {
        _uiState.update {
            it.copy(
                times = it.times + 1
            )
        }
        checkToCheck()
        changePlayer()
    }

    fun onSinglePlayerClick() {
        _uiState.update {
            it.copy(
                times = it.times + 1 ,
                isEnabled = false
            )
        }
    }

    fun checkToCheck() {
        _uiState.update {
            it.copy(toCheck = _uiState.value.times >= 4)
        }
    }

    fun setBox(box: Int) {

        _uiState.update {
            when(box) {
                1 -> it.copy(boxes = it.boxes.copy(box1 = _uiState.value.playerTurn))
                2 -> it.copy(boxes = it.boxes.copy(box2 = _uiState.value.playerTurn))
                3 -> it.copy(boxes = it.boxes.copy(box3 = _uiState.value.playerTurn))
                4 -> it.copy(boxes = it.boxes.copy(box4 = _uiState.value.playerTurn))
                5 -> it.copy(boxes = it.boxes.copy(box5 = _uiState.value.playerTurn))
                6 -> it.copy(boxes = it.boxes.copy(box6 = _uiState.value.playerTurn))
                7 -> it.copy(boxes = it.boxes.copy(box7 = _uiState.value.playerTurn))
                8 -> it.copy(boxes = it.boxes.copy(box8 = _uiState.value.playerTurn))
                9 -> it.copy(boxes = it.boxes.copy(box9 = _uiState.value.playerTurn))
                else -> it.copy()
            }
        }
        _uiState.value.targets.removeAll(setOf(box))
    }

    fun botTurn(uiState: UiState, botDifficulty: Int) {

        val negativePlayer = when (uiState.playerTurn) {
            "X" -> "O"
            else -> "X"
        }

        //if bot can win
        when (botDifficulty) {
            3 -> {
                when (willWin(uiState = uiState, player = uiState.playerTurn)) {
                    1 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(box1 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(1))
                    }

                    2 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(box2 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(2))
                    }

                    3 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(box3 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(3))
                    }

                    4 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(box4 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(4))
                    }

                    5 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(box5 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(5))
                    }

                    6 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(box6 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(6))
                    }

                    7 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(box7 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(7))
                    }

                    8 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(box8 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(8))
                    }

                    9 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(box9 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(9))
                    }

                    else -> {

                        //if bot need to defend
                        when (willWin(uiState = uiState, player = negativePlayer)) {
                            1 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box1 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(1))
                            }

                            2 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box2 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(2))
                            }

                            3 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box3 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(3))
                            }

                            4 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box4 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(4))
                            }

                            5 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box5 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(5))
                            }

                            6 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box6 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(6))
                            }

                            7 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box7 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(7))
                            }

                            8 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box8 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(8))
                            }

                            9 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box9 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(9))
                            }
                            //random box
                            else -> {

                                val randomBox =
                                    if (_uiState.value.targets.isEmpty()) {
                                        listOf(1, 2, 3, 4, 5, 6, 7, 8, 9).random()
                                    } else {
                                        _uiState.value.targets.random()
                                    }
                                when (randomBox) {
                                    1 -> {
                                        _uiState.value.boxes =
                                            _uiState.value.boxes.copy(box1 = uiState.playerTurn)
                                        _uiState.value.targets.removeAll(setOf(1))
                                    }

                                    2 -> {
                                        _uiState.value.boxes =
                                            _uiState.value.boxes.copy(box2 = uiState.playerTurn)
                                        _uiState.value.targets.removeAll(setOf(2))
                                    }

                                    3 -> {
                                        _uiState.value.boxes =
                                            _uiState.value.boxes.copy(box3 = uiState.playerTurn)
                                        _uiState.value.targets.removeAll(setOf(3))
                                    }

                                    4 -> {
                                        _uiState.value.boxes =
                                            _uiState.value.boxes.copy(box4 = uiState.playerTurn)
                                        _uiState.value.targets.removeAll(setOf(4))
                                    }

                                    5 -> {
                                        _uiState.value.boxes =
                                            _uiState.value.boxes.copy(box5 = uiState.playerTurn)
                                        _uiState.value.targets.removeAll(setOf(5))
                                    }

                                    6 -> {
                                        _uiState.value.boxes =
                                            _uiState.value.boxes.copy(box6 = uiState.playerTurn)
                                        _uiState.value.targets.removeAll(setOf(6))
                                    }

                                    7 -> {
                                        _uiState.value.boxes =
                                            _uiState.value.boxes.copy(box7 = uiState.playerTurn)
                                        _uiState.value.targets.removeAll(setOf(7))
                                    }

                                    8 -> {
                                        _uiState.value.boxes =
                                            _uiState.value.boxes.copy(box8 = uiState.playerTurn)
                                        _uiState.value.targets.removeAll(setOf(8))
                                    }

                                    9 -> {
                                        _uiState.value.boxes =
                                            _uiState.value.boxes.copy(box9 = uiState.playerTurn)
                                        _uiState.value.targets.removeAll(setOf(9))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                //if bot need to defend
                when (willWin(uiState = uiState, player = negativePlayer)) {
                    1 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box1 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(1))
                    }

                    2 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box2 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(2))
                    }

                    3 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box3 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(3))
                    }

                    4 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box4 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(4))
                    }

                    5 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box5 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(5))
                    }

                    6 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box6 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(6))
                    }

                    7 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box7 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(7))
                    }

                    8 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box8 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(8))
                    }

                    9 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box9 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(9))
                    }
                    //random box
                    else -> {

                        val randomBox =
                            if (_uiState.value.targets.isEmpty()) {
                                listOf(1, 2, 3, 4, 5, 6, 7, 8, 9).random()
                            } else {
                                _uiState.value.targets.random()
                            }
                        when (randomBox) {
                            1 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box1 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(1))
                            }

                            2 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box2 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(2))
                            }

                            3 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box3 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(3))
                            }

                            4 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box4 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(4))
                            }

                            5 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box5 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(5))
                            }

                            6 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box6 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(6))
                            }

                            7 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box7 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(7))
                            }

                            8 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box8 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(8))
                            }

                            9 -> {
                                _uiState.value.boxes =
                                    _uiState.value.boxes.copy(box9 = uiState.playerTurn)
                                _uiState.value.targets.removeAll(setOf(9))
                            }
                        }
                    }
                }
            }
            1 -> {
                val randomBox =
                    if (_uiState.value.targets.isEmpty()) {
                        listOf(1, 2, 3, 4, 5, 6, 7, 8, 9).random()
                    } else {
                        _uiState.value.targets.random()
                    }
                when (randomBox) {
                    1 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box1 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(1))
                    }

                    2 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box2 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(2))
                    }

                    3 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box3 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(3))
                    }

                    4 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box4 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(4))
                    }

                    5 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box5 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(5))
                    }

                    6 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box6 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(6))
                    }

                    7 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box7 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(7))
                    }

                    8 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box8 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(8))
                    }

                    9 -> {
                        _uiState.value.boxes =
                            _uiState.value.boxes.copy(box9 = uiState.playerTurn)
                        _uiState.value.targets.removeAll(setOf(9))
                    }
                }
            }
        }
        uiState.times++
        uiState.isEnabled = true
        checkToCheck()
        changePlayer()
    }
}
