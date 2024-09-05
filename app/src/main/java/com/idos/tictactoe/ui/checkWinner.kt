package com.idos.tictactoe.ui

import androidx.compose.runtime.Composable
import com.idos.tictactoe.data.UiState
import com.idos.tictactoe.ui.screen.game.TicTacToeScreen
import com.idos.tictactoe.ui.screen.game.TicTacToeSinglePlayerScreen

/**
 * Check the winner for the [TicTacToeScreen] and the [TicTacToeSinglePlayerScreen]
 */

@Composable
fun checkWinner(uiState: UiState): String {

    var winner = ""

    // first row

    if(uiState.boxes.box1 == "X") {
        if(uiState.boxes.box2 == "X") {
            if(uiState.boxes.box3 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.box1 == "O") {
        if(uiState.boxes.box2 == "O") {
            if(uiState.boxes.box3 == "O"){
                winner = "O"
            }
        }
    }

    // second row

    if(uiState.boxes.box4 == "X") {
        if(uiState.boxes.box5 == "X") {
            if(uiState.boxes.box6 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.box4 == "O") {
        if(uiState.boxes.box5 == "O") {
            if(uiState.boxes.box6 == "O"){
                winner = "O"
            }
        }
    }

    // third row

    if(uiState.boxes.box7 == "X") {
        if(uiState.boxes.box8 == "X") {
            if(uiState.boxes.box9 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.box7 == "O") {
        if(uiState.boxes.box8 == "O") {
            if(uiState.boxes.box9 == "O"){
                winner = "O"
            }
        }
    }

    // first column

    if(uiState.boxes.box1 == "X") {
        if(uiState.boxes.box4 == "X") {
            if(uiState.boxes.box7 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.box1 == "O") {
        if(uiState.boxes.box4 == "O") {
            if(uiState.boxes.box7 == "O"){
                winner = "O"
            }
        }
    }

    // second column

    if(uiState.boxes.box2 == "X") {
        if(uiState.boxes.box5 == "X") {
            if(uiState.boxes.box8 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.box2 == "O") {
        if(uiState.boxes.box5 == "O") {
            if(uiState.boxes.box8 == "O"){
                winner = "O"
            }
        }
    }

    // third column

    if(uiState.boxes.box3 == "X") {
        if(uiState.boxes.box6 == "X") {
            if(uiState.boxes.box9 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.box3 == "O") {
        if(uiState.boxes.box6 == "O") {
            if(uiState.boxes.box9 == "O"){
                winner = "O"
            }
        }
    }

    // first diagonal line

    if(uiState.boxes.box1 == "X") {
        if(uiState.boxes.box5 == "X") {
            if(uiState.boxes.box9 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.box1 == "O") {
        if(uiState.boxes.box5 == "O") {
            if(uiState.boxes.box9 == "O"){
                winner = "O"
            }
        }
    }

    // second diagonal line

    if(uiState.boxes.box3 == "X") {
        if(uiState.boxes.box5 == "X") {
            if(uiState.boxes.box7 == "X"){
                winner = "X"
            }
        }
    }

    if(uiState.boxes.box3 == "O") {
        if(uiState.boxes.box5 == "O") {
            if(uiState.boxes.box7 == "O"){
                winner = "O"
            }
        }
    }

    return winner

}