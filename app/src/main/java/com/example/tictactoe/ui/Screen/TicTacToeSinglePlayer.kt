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
fun SinglePlayerGameButton(player: String, onClick: () -> Unit = {}, setButton: () -> Unit = {}) {

    Card(modifier = Modifier.padding(8.dp),shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        TextButton(
            onClick = {
                setButton()
                Timer().schedule(500){onClick()}
            },
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

@Composable
fun SinglePlayerButtonGrid(viewModel: TicTacToeViewModel = TicTacToeViewModel(), onPlayAgain: () -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.ToCheck) {
        uiState.winner = CheckWinner(viewModel = viewModel)
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
        viewModel.check_ToCheck()
        viewModel.changePlayer()
        viewModel.botTurn()
    }



    Column() {
        Row() {
            SinglePlayerGameButton(
                player = uiState.Box1,
                setButton = {uiState.Box1 = uiState.player_Turn},
                onClick = {onClick()})
            SinglePlayerGameButton(
                player = uiState.Box2,
                setButton = {uiState.Box2 = uiState.player_Turn},
                onClick = {onClick()})
            SinglePlayerGameButton(
                player = uiState.Box3,
                setButton = {uiState.Box3 = uiState.player_Turn},
                onClick = {onClick()})
        }
        Row() {
            SinglePlayerGameButton(
                player = uiState.Box4,
                setButton = {uiState.Box4= uiState.player_Turn},
                onClick = {onClick()})
            SinglePlayerGameButton(
                player = uiState.Box5,
                setButton = {uiState.Box5 = uiState.player_Turn},
                onClick = {onClick()})
            SinglePlayerGameButton(
                player = uiState.Box6,
                setButton = {uiState.Box6 = uiState.player_Turn},
                onClick = {onClick()})
        }
        Row() {
            SinglePlayerGameButton(
                player = uiState.Box7,
                setButton = {uiState.Box7 = uiState.player_Turn},
                onClick = {onClick()})
            SinglePlayerGameButton(
                player = uiState.Box8,
                setButton = {uiState.Box8 = uiState.player_Turn},
                onClick = {onClick()})
            SinglePlayerGameButton(
                player = uiState.Box9,
                setButton = {uiState.Box9 = uiState.player_Turn},
                onClick = {onClick()})
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