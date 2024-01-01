package com.idos.tictactoe.ui.Screen

import androidx.lifecycle.ViewModel
import com.idos.tictactoe.data.UiState
import com.idos.tictactoe.ui.WillWin
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
        _uiState.value = UiState(
            player_Turn = if (times % 2 == 1) { "O" } else { "X" },
            isenabled = times % 2 == 0
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
                1 -> it.copy(boxes = it.boxes.copy(Box1 = _uiState.value.player_Turn))
                2 -> it.copy(boxes = it.boxes.copy(Box2 = _uiState.value.player_Turn))
                3 -> it.copy(boxes = it.boxes.copy(Box3 = _uiState.value.player_Turn))
                4 -> it.copy(boxes = it.boxes.copy(Box4 = _uiState.value.player_Turn))
                5 -> it.copy(boxes = it.boxes.copy(Box5 = _uiState.value.player_Turn))
                6 -> it.copy(boxes = it.boxes.copy(Box6 = _uiState.value.player_Turn))
                7 -> it.copy(boxes = it.boxes.copy(Box7 = _uiState.value.player_Turn))
                8 -> it.copy(boxes = it.boxes.copy(Box8 = _uiState.value.player_Turn))
                9 -> it.copy(boxes = it.boxes.copy(Box9 = _uiState.value.player_Turn))
                else -> it.copy()
            }
        }
        _uiState.value.targets.removeAll(setOf(box))
    }

    fun botTurn(uiState: UiState) {

        val negativePlayer = when (uiState.player_Turn) {
            "X" -> "O"
            else -> "X"
        }

        //if bot can win
        when (WillWin(uiState = uiState, player = uiState.player_Turn)) {
            1 -> {
                _uiState.value.boxes = _uiState.value.boxes.copy(Box1 = uiState.player_Turn)
                _uiState.value.targets.removeAll(setOf(1))
            }
            2 -> {
                _uiState.value.boxes = _uiState.value.boxes.copy(Box2 = uiState.player_Turn)
                _uiState.value.targets.removeAll(setOf(2))
            }
            3 -> {
                _uiState.value.boxes = _uiState.value.boxes.copy(Box3 = uiState.player_Turn)
                _uiState.value.targets.removeAll(setOf(3))
            }
            4 -> {
                _uiState.value.boxes = _uiState.value.boxes.copy(Box4 = uiState.player_Turn)
                _uiState.value.targets.removeAll(setOf(4))
            }
            5 -> {
                _uiState.value.boxes = _uiState.value.boxes.copy(Box5 = uiState.player_Turn)
                _uiState.value.targets.removeAll(setOf(5))
            }
            6 -> {
                _uiState.value.boxes = _uiState.value.boxes.copy(Box6 = uiState.player_Turn)
                _uiState.value.targets.removeAll(setOf(6))
            }
            7 -> {
                _uiState.value.boxes = _uiState.value.boxes.copy(Box7 = uiState.player_Turn)
                _uiState.value.targets.removeAll(setOf(7))
            }
            8 -> {
                _uiState.value.boxes = _uiState.value.boxes.copy(Box8 = uiState.player_Turn)
                _uiState.value.targets.removeAll(setOf(8))
            }
            9 -> {
                _uiState.value.boxes = _uiState.value.boxes.copy(Box9 = uiState.player_Turn)
                _uiState.value.targets.removeAll(setOf(9))
            }
            else -> {

                //if bot need to defend
                when (WillWin(uiState = uiState, player = negativePlayer)) {
                    1 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(Box1 = uiState.player_Turn)
                        _uiState.value.targets.removeAll(setOf(1))
                    }
                    2 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(Box2 = uiState.player_Turn)
                        _uiState.value.targets.removeAll(setOf(2))
                    }
                    3 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(Box3 = uiState.player_Turn)
                        _uiState.value.targets.removeAll(setOf(3))
                    }
                    4 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(Box4 = uiState.player_Turn)
                        _uiState.value.targets.removeAll(setOf(4))
                    }
                    5 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(Box5 = uiState.player_Turn)
                        _uiState.value.targets.removeAll(setOf(5))
                    }
                    6 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(Box6 = uiState.player_Turn)
                        _uiState.value.targets.removeAll(setOf(6))
                    }
                    7 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(Box7 = uiState.player_Turn)
                        _uiState.value.targets.removeAll(setOf(7))
                    }
                    8 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(Box8 = uiState.player_Turn)
                        _uiState.value.targets.removeAll(setOf(8))
                    }
                    9 -> {
                        _uiState.value.boxes = _uiState.value.boxes.copy(Box9 = uiState.player_Turn)
                        _uiState.value.targets.removeAll(setOf(9))
                    }
                    //random box
                    else -> {

                        val randomBox =
                            if (_uiState.value.targets.isEmpty()) {
                                listOf(1,2,3,4,5,6,7,8,9).random()
                            } else {
                                _uiState.value.targets.random()
                            }
                        when (randomBox) {
                            1 -> {
                                _uiState.value.boxes = _uiState.value.boxes.copy(Box1 = uiState.player_Turn)
                                _uiState.value.targets.removeAll(setOf(1))
                            }

                            2 -> {
                                _uiState.value.boxes = _uiState.value.boxes.copy(Box2 = uiState.player_Turn)
                                _uiState.value.targets.removeAll(setOf(2))
                            }

                            3 -> {
                                _uiState.value.boxes = _uiState.value.boxes.copy(Box3 = uiState.player_Turn)
                                _uiState.value.targets.removeAll(setOf(3))
                            }

                            4 -> {
                                _uiState.value.boxes = _uiState.value.boxes.copy(Box4 = uiState.player_Turn)
                                _uiState.value.targets.removeAll(setOf(4))
                            }

                            5 -> {
                                _uiState.value.boxes = _uiState.value.boxes.copy(Box5 = uiState.player_Turn)
                                _uiState.value.targets.removeAll(setOf(5))
                            }

                            6 -> {
                                _uiState.value.boxes = _uiState.value.boxes.copy(Box6 = uiState.player_Turn)
                                _uiState.value.targets.removeAll(setOf(6))
                            }

                            7 -> {
                                _uiState.value.boxes = _uiState.value.boxes.copy(Box7 = uiState.player_Turn)
                                _uiState.value.targets.removeAll(setOf(7))
                            }

                            8 -> {
                                _uiState.value.boxes = _uiState.value.boxes.copy(Box8 = uiState.player_Turn)
                                _uiState.value.targets.removeAll(setOf(8))
                            }

                            9 -> {
                                _uiState.value.boxes = _uiState.value.boxes.copy(Box9 = uiState.player_Turn)
                                _uiState.value.targets.removeAll(setOf(9))
                            }
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
