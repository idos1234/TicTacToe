package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.example.tictactoe.data.UiState
import com.example.tictactoe.ui.CheckWinner
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery
import java.util.*

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
    onPlayAgain: () -> Unit,
    uiState: UiState,
) {

    //if need to check winner
    if(uiState.ToCheck) {
        uiState.winner = CheckWinner(uiState)
        if(uiState.winner != "") {

            //show winner
             showWinner(
                 winner = "Winner is: ${uiState.winner}",
                 text = "Congratulations for winning",
                 onPlayAgain = {
                     onPlayAgain()
                 }
             )
        }
        //show tie
        else if (uiState.times == 9){
            showWinner(winner = "Draw", text = "Try to win next time", onPlayAgain = onPlayAgain)
        }

    } else {
        uiState.winner = ""
    }

    val onClick = {
        viewModel.onClick()
    }



    Column() {
        Row() {
            //box1
            GameButton(
                player = uiState.Box1,
                onClick = {
                    if (uiState.Box1 == "") {
                        viewModel.SetBox(1)
                        onClick()
                    }
                },
            )
            //box2
            GameButton(
                player = uiState.Box2,
                onClick = {
                    if (uiState.Box2 == "") {
                        viewModel.SetBox(2)
                        onClick()
                    }
                },
            )
            //box3
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
            //box4
            GameButton(
                player = uiState.Box4,
                onClick = {
                    if (uiState.Box4 == "") {
                        viewModel.SetBox(4)
                        onClick()
                    }
                },
            )
            //box5
            GameButton(
                player = uiState.Box5,
                onClick = {
                    if (uiState.Box5 == "") {
                        viewModel.SetBox(5)
                        onClick()
                    }
                },
            )
            //box6
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
            //box7
            GameButton(
                player = uiState.Box7,
                onClick = {
                    if (uiState.Box7 == "") {
                        viewModel.SetBox(7)
                        onClick()
                    }
                },
            )
            //box8
            GameButton(
                player = uiState.Box8,
                onClick = {
                    if (uiState.Box8 == "") {
                        viewModel.SetBox(8)
                        onClick()
                    }
                },
            )
            //box9
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

/**
 * Show the two players game screen
 */

@Composable
fun TicTacToeScreen(
    viewModel: TicTacToeViewModel,
    uiState: UiState,
    onPlayAgain: () -> Unit
) {


    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier.size(150.dp).padding(20.dp), elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (uiState.player_Turn == "X") Primery else { Secondery})) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("X", fontWeight = FontWeight.Bold, fontSize = 50.sp)
                }
            }
            Card(modifier = Modifier.size(150.dp).padding(20.dp), elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (uiState.player_Turn == "O") Primery else { Secondery})) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("O", fontWeight = FontWeight.Bold, fontSize = 50.sp)
                }
            }
        }
        Spacer(modifier = Modifier.weight(2f))
        ButtonGrid(
            viewModel = viewModel,
            onPlayAgain = {
                onPlayAgain()
            },
            uiState = uiState,
        )
        Spacer(modifier = Modifier.weight(4f))
    }
}