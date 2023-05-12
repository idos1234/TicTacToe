package com.example.tictactoe.ui.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            enabled = text != player,
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
fun ButtonGrid(viewModel: TicTacToeViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column() {
        Row() {
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box1 = uiState.player_Turn
                    viewModel.changePlayer()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box2 = uiState.player_Turn
                    viewModel.changePlayer()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box3 = uiState.player_Turn
                    viewModel.changePlayer()

                })
        }
        Row() {
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box4 = uiState.player_Turn
                    viewModel.changePlayer()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box5 = uiState.player_Turn
                    viewModel.changePlayer()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box6 = uiState.player_Turn
                    viewModel.changePlayer()
                })
        }
        Row() {
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box7 = uiState.player_Turn
                    viewModel.changePlayer()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box8 = uiState.player_Turn
                    viewModel.changePlayer()
                })
            GameButton(
                player = uiState.player_Turn,
                onClick = {
                    uiState.Box9 = uiState.player_Turn
                    viewModel.changePlayer()
                }
            )
        }
    }
}