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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.data.UiState
import com.example.tictactoe.ui.CheckWinner
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery

@Composable
fun GameButton(player: String, onClick: () -> Unit = {}) {
    var text by remember {
        mutableStateOf("")
    }

    Card(modifier = Modifier.padding(8.dp),shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        TextButton(
            onClick = {
                onClick()
                text = player},
            enabled = player.isNotEmpty(),
            modifier = Modifier.background(Primery)
        ) {
            Text(
                text = text,
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
fun ButtonGrid(viewModel: TicTacToeViewModel, onPlayAgain: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.ToCheck) {
        uiState.winner = CheckWinner(uiState)
        if(uiState.winner != "") {
            showWinner(winner = "Winner is: ${uiState.winner}", text = "Congratulations for winning", onPlayAgain = onPlayAgain)
        }
        else if (uiState.times == 9){
            showWinner(winner = "Tie", text = "Try to win next time", onPlayAgain = onPlayAgain)
        }

    } else {
        uiState.winner = ""
    }

    val onClick = {
        uiState.times ++
        viewModel.check_ToCheck()
        viewModel.changePlayer()
    }



    Column() {
        Row() {
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box1 = uiState.player_Turn
                    onClick()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box2 = uiState.player_Turn
                    onClick()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box3 = uiState.player_Turn
                    onClick()
                })
        }
        Row() {
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box4 = uiState.player_Turn
                    onClick()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box5 = uiState.player_Turn
                    onClick()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box6 = uiState.player_Turn
                    onClick()
                })
        }
        Row() {
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box7 = uiState.player_Turn
                    onClick()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box8 = uiState.player_Turn
                    onClick()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box9 = uiState.player_Turn
                    onClick()
                }
            )
        }
    }
}

@Composable
fun showWinner(winner: String, text: String, onPlayAgain: () -> Unit) {

    val activity = (LocalContext.current as Activity)

    AlertDialog(
        modifier = Modifier.background(BackGround),
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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TicTacToeScreen(viewModel: TicTacToeViewModel = TicTacToeViewModel(), uiState: UiState = UiState(), onPlayAgain: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        Spacer(modifier = Modifier.weight(2f))
        Text(text = "Player: ${uiState.player_Turn}", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.weight(1f))
        ButtonGrid(viewModel = viewModel, onPlayAgain = onPlayAgain)
        Spacer(modifier = Modifier.weight(4f))

    }
}

@Preview(showBackground = true)
@Composable
fun gamePreview() {
    TicTacToeScreen()
}