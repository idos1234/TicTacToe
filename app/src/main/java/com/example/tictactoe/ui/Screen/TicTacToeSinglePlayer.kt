package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.data.UiState
import com.example.tictactoe.ui.CheckWinner
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun SinglePlayerGameButton(player: String, onClick: () -> Unit = {}, viewModel: TicTacToeViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Card(modifier = Modifier.padding(8.dp),shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        TextButton(
            onClick = {
                onClick()
                if (player.isEmpty()) {
                    Timer().schedule(1000) {
                        if (uiState.winner == "") {
                            viewModel.botTurn(uiState)
                        }
                    }
                }
            },
            enabled = uiState.isenabled ,
            modifier = Modifier.background(Primery)
        ) {
            Text(
                text = player,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .size(50.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun SinglePlayerButtonGrid(viewModel: TicTacToeViewModel = TicTacToeViewModel(), onPlayAgain: () -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.ToCheck) {
        uiState.winner = CheckWinner(uiState)
        if(uiState.winner != "") {
            showWinner(winner = "Winner is: ${uiState.winner}", text = "Congratulations for winning", onPlayAgain = onPlayAgain)
        }
        else if (uiState.times >= 9){
            showWinner(winner = "Tie", text = "Try to win next time", onPlayAgain = onPlayAgain)
        }

    } else {
        uiState.winner = ""
    }

    val onClick = {
        uiState.times ++
        uiState.isenabled = false
        viewModel.check_ToCheck()
        viewModel.changePlayer()
    }



    Column() {
        Row() {
            SinglePlayerGameButton(
                player = uiState.Box1,
                onClick = {
                    if (uiState.Box1 == "") {
                        uiState.Box1 = uiState.player_Turn
                        onClick()
                    }
                },
                viewModel = viewModel)
            SinglePlayerGameButton(
                player = uiState.Box2,
                onClick = {
                    if (uiState.Box2 == "") {
                        uiState.Box2 = uiState.player_Turn
                        onClick()
                    }
                },
                viewModel = viewModel)
            SinglePlayerGameButton(
                player = uiState.Box3,
                onClick = {
                    if (uiState.Box3 == "") {
                        uiState.Box3 = uiState.player_Turn
                        onClick()
                    }
                },
                viewModel = viewModel)
        }
        Row() {
            SinglePlayerGameButton(
                player = uiState.Box4,
                onClick = {
                    if (uiState.Box4 == "") {
                        uiState.Box4 = uiState.player_Turn
                        onClick()
                    }
                },
                viewModel = viewModel)
            SinglePlayerGameButton(
                player = uiState.Box5,
                onClick = {
                    if (uiState.Box5 == "") {
                        uiState.Box5 = uiState.player_Turn
                        onClick()
                    }
                },
                viewModel = viewModel)
            SinglePlayerGameButton(
                player = uiState.Box6,
                onClick = {
                    if (uiState.Box6 == "") {
                        uiState.Box6 = uiState.player_Turn
                        onClick()
                    }
                },
                viewModel = viewModel)
        }
        Row() {
            SinglePlayerGameButton(
                player = uiState.Box7,
                onClick = {
                    if (uiState.Box7 == "") {
                        uiState.Box7 = uiState.player_Turn
                        onClick()
                    }
                },
                viewModel = viewModel)
            SinglePlayerGameButton(
                player = uiState.Box8,
                onClick = {
                    if (uiState.Box8 == "") {
                        uiState.Box8 = uiState.player_Turn
                        onClick()
                    }
                },
                viewModel = viewModel)
            SinglePlayerGameButton(
                player = uiState.Box9,
                onClick = {
                    if (uiState.Box9 == "") {
                        uiState.Box9 = uiState.player_Turn
                        onClick()
                    }
                },
                viewModel = viewModel)
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TicTacToeSinglePlayerScreen(viewModel: TicTacToeViewModel = TicTacToeViewModel(), uiState: UiState = UiState(), onPlayAgain: () -> Unit = {}) {

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        Spacer(modifier = Modifier.weight(2f))
        Text(text = "Player: ${uiState.player_Turn}", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.weight(1f))
        SinglePlayerButtonGrid(viewModel = viewModel, onPlayAgain = onPlayAgain)
        Spacer(modifier = Modifier.weight(4f))
    }
}