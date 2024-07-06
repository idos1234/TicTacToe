package com.idos.tictactoe.ui

import androidx.compose.runtime.Composable
import com.idos.tictactoe.data.UiState
import com.idos.tictactoe.ui.Screen.Game.TicTacToeScreen
import com.idos.tictactoe.ui.Screen.Game.TicTacToeSinglePlayerScreen

/**
 * Check the winner for the [TicTacToeScreen] and the [TicTacToeSinglePlayerScreen]
 */

@Composable
fun CheckWinner(uiState: UiState): String {

    var winner = ""

    // first row

    if(uiState.boxes.Box1 == "X") {
        if(uiState.boxes.Box2 == "X") {
            if(uiState.boxes.Box3 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.Box1 == "O") {
        if(uiState.boxes.Box2 == "O") {
            if(uiState.boxes.Box3 == "O"){
                winner = "O"
            }
        }
    }

    // second row

    if(uiState.boxes.Box4 == "X") {
        if(uiState.boxes.Box5 == "X") {
            if(uiState.boxes.Box6 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.Box4 == "O") {
        if(uiState.boxes.Box5 == "O") {
            if(uiState.boxes.Box6 == "O"){
                winner = "O"
            }
        }
    }

    // third row

    if(uiState.boxes.Box7 == "X") {
        if(uiState.boxes.Box8 == "X") {
            if(uiState.boxes.Box9 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.Box7 == "O") {
        if(uiState.boxes.Box8 == "O") {
            if(uiState.boxes.Box9 == "O"){
                winner = "O"
            }
        }
    }

    // first column

    if(uiState.boxes.Box1 == "X") {
        if(uiState.boxes.Box4 == "X") {
            if(uiState.boxes.Box7 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.Box1 == "O") {
        if(uiState.boxes.Box4 == "O") {
            if(uiState.boxes.Box7 == "O"){
                winner = "O"
            }
        }
    }

    // second column

    if(uiState.boxes.Box2 == "X") {
        if(uiState.boxes.Box5 == "X") {
            if(uiState.boxes.Box8 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.Box2 == "O") {
        if(uiState.boxes.Box5 == "O") {
            if(uiState.boxes.Box8 == "O"){
                winner = "O"
            }
        }
    }

    // third column

    if(uiState.boxes.Box3 == "X") {
        if(uiState.boxes.Box6 == "X") {
            if(uiState.boxes.Box9 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.Box3 == "O") {
        if(uiState.boxes.Box6 == "O") {
            if(uiState.boxes.Box9 == "O"){
                winner = "O"
            }
        }
    }

    // first diagonal line

    if(uiState.boxes.Box1 == "X") {
        if(uiState.boxes.Box5 == "X") {
            if(uiState.boxes.Box9 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.Box1 == "O") {
        if(uiState.boxes.Box5 == "O") {
            if(uiState.boxes.Box9 == "O"){
                winner = "O"
            }
        }
    }

    // second diagonal line

    if(uiState.boxes.Box3 == "X") {
        if(uiState.boxes.Box5 == "X") {
            if(uiState.boxes.Box7 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.Box3 == "O") {
        if(uiState.boxes.Box5 == "O") {
            if(uiState.boxes.Box7 == "O"){
                winner = "O"
            }
        }
    }

    return winner

}