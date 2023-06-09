package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.data.Player
import com.example.tictactoe.data.UiState
import com.example.tictactoe.ui.CheckWinner
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

/**
 * Show a single button in the two players game grid
 */

@Composable
fun GameButton(player: String, onClick: () -> Unit = {}) {

    Card(modifier = Modifier.padding(8.dp),shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        TextButton(
            onClick = onClick,
            enabled = player.isEmpty(),
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

/**
 * Show the two players game grid
 */

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ButtonGrid(
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
            } else if (uiState.winner == "O"){
                if (wasNotUpdate) {
                    coroutineScope.launch {
                        signUpViewModel.updateScore(player2)
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
                    winner = "Winner is: ${if (uiState.winner == "X") player1.name else player2.name} \n ${if (uiState.winner == "X")"Your Score: ${player1.score}" else "Your Score: ${player2.score}"}",
                    text = "Congratulations for winning",
                    onPlayAgain = {
                        wasNotUpdate = true
                        toShowWinner = false
                        onPlayAgain()
                    }
                )
            }
        }
        else if (uiState.times == 9){
            showWinner(winner = "Tie", text = "Try to win next time", onPlayAgain = onPlayAgain)
        }

    } else {
        uiState.winner = ""
    }

    val onClick = {
        viewModel.onClick()
    }



    Column() {



        Row() {
            GameButton(
                player = uiState.Box1,
                onClick = {
                    if (uiState.Box1 == "") {
                        viewModel.SetBox(1)
                        onClick()
                    }
                },
            )
            GameButton(
                player = uiState.Box2,
                onClick = {
                    if (uiState.Box2 == "") {
                        viewModel.SetBox(2)
                        onClick()
                    }
                },
            )
            GameButton(
                player = uiState.Box3,
                onClick = {
                    if (uiState.Box3 == "") {
                        viewModel.SetBox(3)
                        onClick()
                    }
                },
            )
        }
        Row() {
            GameButton(
                player = uiState.Box4,
                onClick = {
                    if (uiState.Box4 == "") {
                        viewModel.SetBox(4)
                        onClick()
                    }
                },
            )
            GameButton(
                player = uiState.Box5,
                onClick = {
                    if (uiState.Box5 == "") {
                        viewModel.SetBox(5)
                        onClick()
                    }
                },
            )
            GameButton(
                player = uiState.Box6,
                onClick = {
                    if (uiState.Box6 == "") {
                        viewModel.SetBox(6)
                        onClick()
                    }
                },
            )
        }
        Row() {
            GameButton(
                player = uiState.Box7,
                onClick = {
                    if (uiState.Box7 == "") {
                        viewModel.SetBox(7)
                        onClick()
                    }
                },
            )
            GameButton(
                player = uiState.Box8,
                onClick = {
                    if (uiState.Box8 == "") {
                        viewModel.SetBox(8)
                        onClick()
                    }
                },
            )
            GameButton(
                player = uiState.Box9,
                onClick = {
                    if (uiState.Box9 == "") {
                        viewModel.SetBox(9)
                        onClick()
                    }
                },
            )
        }
    }
}

/**
 * Show the winner
 */

@Composable
fun showWinner(winner: String, text: String, onPlayAgain: () -> Unit) {

    val activity = (LocalContext.current as Activity)

    AlertDialog(
        backgroundColor = Secondery,
        onDismissRequest = {},
        title = { Text(text = winner, fontWeight = FontWeight.ExtraBold, color = Color.Black, textAlign = TextAlign.Center)},
        text = { Text(text = text, color = Color.Black, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)},
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text("Exit")
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = "Play again")
            }
        }
    )
}

@Composable
fun ShowPlayerTurn(player: String) {
    Text(text = "Player: $player", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold)

}

/**
 * Show the two players game screen
 */

@Composable
fun TicTacToeScreen(
    viewModel: TicTacToeViewModel,
    signUpViewModel: SignUpViewModel,
    uiState: UiState,
    onPlayAgain: () -> Unit,
    player1: Player,
    player2: Player,
    onWinner: () -> Unit
) {


    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        Spacer(modifier = Modifier.weight(2f))
        ShowPlayerTurn(
            player = if (uiState.player_Turn == "X") {
                player1.name
            } else {
                player2.name
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        ButtonGrid(
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