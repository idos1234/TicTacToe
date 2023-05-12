package com.example.tictactoe.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tictactoe.ui.Screen.TicTacToeViewModel

@Composable
fun CheckWinner(viewModel: TicTacToeViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    var winner: String = ""

    // first row

    if(uiState.Box1 == "X") {
        if(uiState.Box2 == "X") {
            if(uiState.Box3 == "X"){
                winner = "X"
            }
        }
    }

    else if(uiState.Box1 == "O") {
        if(uiState.Box2 == "O") {
            if(uiState.Box3 == "O"){
                winner = "O"
            }
        }
    }

    // second row

    else if(uiState.Box4 == "X") {
        if(uiState.Box5 == "X") {
            if(uiState.Box6 == "X"){
                winner = "X"
            }
        }
    }

    else if(uiState.Box4 == "O") {
        if(uiState.Box5 == "O") {
            if(uiState.Box6 == "O"){
                winner = "O"
            }
        }
    }

    // third row

    else if(uiState.Box7 == "X") {
        if(uiState.Box8 == "X") {
            if(uiState.Box9 == "X"){
                winner = "X"
            }
        }
    }

    else if(uiState.Box7 == "O") {
        if(uiState.Box8 == "O") {
            if(uiState.Box9 == "O"){
                winner = "O"
            }
        }
    }

    // first column

    else if(uiState.Box1 == "X") {
        if(uiState.Box4 == "X") {
            if(uiState.Box7 == "X"){
                winner = "X"
            }
        }
    }

    else if(uiState.Box1 == "O") {
        if(uiState.Box4 == "O") {
            if(uiState.Box7 == "O"){
                winner = "O"
            }
        }
    }

    // second column

    else if(uiState.Box2 == "X") {
        if(uiState.Box5 == "X") {
            if(uiState.Box8 == "X"){
                winner = "X"
            }
        }
    }

    else if(uiState.Box2 == "O") {
        if(uiState.Box5 == "O") {
            if(uiState.Box8 == "O"){
                winner = "O"
            }
        }
    }

    // third column

    else if(uiState.Box3 == "X") {
        if(uiState.Box6 == "X") {
            if(uiState.Box9 == "X"){
                winner = "X"
            }
        }
    }

    else if(uiState.Box3 == "O") {
        if(uiState.Box6 == "O") {
            if(uiState.Box9 == "O"){
                winner = "O"
            }
        }
    }
}