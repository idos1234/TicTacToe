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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.data.Player
import com.example.tictactoe.data.UiState
import com.example.tictactoe.ui.CheckWinner
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Orange
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

/**
 * Show a single button in the single player game grid
 */

@OptIn(ExperimentalTextApi::class)
@Composable
fun SinglePlayerGameButton(player: String, onClick: () -> Unit = {}, viewModel: TicTacToeViewModel = TicTacToeViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Card(modifier = Modifier.padding(8.dp),shape = RoundedCornerShape(25.dp), border = BorderStroke(2.dp, color = Secondery)) {
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
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Black,
                    brush =
                    if (player == "X") {
                        Brush.linearGradient(
                            listOf(Color.Blue, Secondery)
                        )
                    } else {
                        Brush.linearGradient(
                            listOf(Orange, Color.Yellow)
                        )
                    }
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .size(50.dp)
            )
        }
    }
}

/**
 * Show the single player game grid
 */

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SinglePlayerButtonGrid(
    viewModel: TicTacToeViewModel,
    signUpViewModel: SignUpViewModel,
    onPlayAgain: () -> Unit,
    uiState: UiState,
    player1: Player,
    player2: Player,
    onWinner: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var wasNotUpdate by remember {
        mutableStateOf(true)
    }
    var toShowWinner by remember {
        mutableStateOf(false)
    }

    if(uiState.ToCheck) {
        uiState.winner = CheckWinner(uiState)
        if(uiState.winner != "") {
            if (uiState.winner == "X"){
                if (wasNotUpdate) {
                    coroutineScope.launch {
                        signUpViewModel.updateScore(player1)
                    }
                    Timer().schedule(500) {
                        onWinner()
                    }
                    wasNotUpdate = false
                }
            }

            Timer().schedule(500) { toShowWinner = true }

            if (toShowWinner) {
                showWinner(
                    winner = "Winner is: ${if (uiState.winner == "X") player1.name else player2.name} \n ${if (uiState.winner == "X")"Your Score: ${player1.score}" else ""}",
                    text = "Congratulations for winning",
                    onPlayAgain = {
                        wasNotUpdate = true
                        toShowWinner = false
                        onPlayAgain()
                    }
                )
            }
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



    Column {
        Row {
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

/**
 * Show the single player game screen
 */

@Composable
fun TicTacToeSinglePlayerScreen(
    viewModel: TicTacToeViewModel,
    signUpViewModel: SignUpViewModel,
    uiState: UiState,
    onPlayAgain: () -> Unit,
    player1: Player,
    player2: Player,
    onWinner: () -> Unit
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(BackGround)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(2f))
        ShowPlayerTurn(
            player = if (uiState.player_Turn == "X") {
                player1.name
            } else {
                player2.name
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        SinglePlayerButtonGrid(
            viewModel = viewModel,
            onPlayAgain = {
                onPlayAgain()
            },
            uiState = uiState,
            player1 = player1,
            player2 = player2,
            signUpViewModel = signUpViewModel,
            onWinner = onWinner
        )
        Spacer(modifier = Modifier.weight(4f))
    }
}