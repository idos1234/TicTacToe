package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.idos.tictactoe.R
import com.idos.tictactoe.data.UiState
import com.idos.tictactoe.ui.CheckWinner
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Primery
import com.idos.tictactoe.ui.theme.Secondery
import java.util.*
import kotlin.concurrent.schedule

/**
 * Show a single button in the single player game grid
 */

@Composable
fun SinglePlayerGameButton(box: String, onClick: () -> Unit = {}, viewModel: TicTacToeViewModel, context: Context = LocalContext.current, playerName: String, modifier: Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    var player by remember {
        mutableStateOf(com.idos.tictactoe.data.MainPlayerUiState())
    }

    //get Players collection from database
    player = getPlayer(playerName, context)

    Card(modifier = modifier,shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        Image(
            painter = painterResource(
                id = when (box) {
                    "X" -> player.currentX
                    "O" -> player.currentO
                    else -> {
                        R.drawable.o_1}
                }
            ),
            contentDescription = null,
            alpha = if (box != "") DefaultAlpha else 0f,
            modifier = Modifier
                .background(Color.Gray)
                .size(100.dp)
                .clickable(
                    onClick = {
                        onClick()
                        if (box.isEmpty()) {
                            Timer().schedule(1000) {
                                if (uiState.winner == "") {
                                    viewModel.botTurn(uiState)
                                }
                            }
                        }
                    },
                    enabled = uiState.isenabled
                )
        )
    }
}

/**
 * Show the single player game grid
 */

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SinglePlayerButtonGrid(
    viewModel: TicTacToeViewModel,
    onPlayAgain: () -> Unit,
    uiState: UiState,
    playerName: String,
    navController: NavController,
    modifier: Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val size = (screenWidth/19)*5

    //if need to check winner
    if(uiState.ToCheck) {
        uiState.winner = CheckWinner(uiState)
        if(uiState.winner != "") {
            if(uiState.winner == "X") {
                //Won
                if (!uiState.isScoreUpdated) {
                    viewModel.updateScore(1)
                }
                showWinner(
                    winner = "You won!",
                    text = "Congratulations for winning",
                    onPlayAgain = { onPlayAgain() },
                    navController = navController
                )
            } else if(uiState.winner == "O") {
                //Lost
                if (!uiState.isScoreUpdated) {
                    viewModel.updateScore(2)
                }
                showWinner(
                    winner = "You lost!",
                    text = "Try to win next time",
                    onPlayAgain = { onPlayAgain() },
                    navController = navController
                )
            }
        }
        //show tie
        else if (uiState.times >= 9){
            showWinner(
                winner = "Draw!",
                text = "Try to win next time",
                onPlayAgain = { onPlayAgain() },
                navController = navController)
        }

    } else {
        uiState.winner = ""
    }

    val onClick = {
        viewModel.onSinglePlayerClick()
        viewModel.check_ToCheck()
        if (uiState.winner == "") {
            viewModel.changePlayer()
        }
    }



    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(0.5f))
        Row(modifier = Modifier.weight(5f)) {
            Spacer(modifier = Modifier.weight(1f))
            //box1
            SinglePlayerGameButton(
                box = uiState.boxes.Box1,
                onClick = {
                    if (uiState.boxes.Box1 == "") {
                        viewModel.SetBox(1)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box2
            SinglePlayerGameButton(
                box = uiState.boxes.Box2,
                onClick = {
                    if (uiState.boxes.Box2 == "") {
                        viewModel.SetBox(2)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box3
            SinglePlayerGameButton(
                box = uiState.boxes.Box3,
                onClick = {
                    if (uiState.boxes.Box3 == "") {
                        viewModel.SetBox(3)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Row(modifier = Modifier.weight(5f)) {
            Spacer(modifier = Modifier.weight(1f))
            //box4
            SinglePlayerGameButton(
                box = uiState.boxes.Box4,
                onClick = {
                    if (uiState.boxes.Box4 == "") {
                        viewModel.SetBox(4)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box5
            SinglePlayerGameButton(
                box = uiState.boxes.Box5,
                onClick = {
                    if (uiState.boxes.Box5 == "") {
                        viewModel.SetBox(5)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box6
            SinglePlayerGameButton(
                box = uiState.boxes.Box6,
                onClick = {
                    if (uiState.boxes.Box6 == "") {
                        viewModel.SetBox(6)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Row(modifier = Modifier.weight(5f)) {
            Spacer(modifier = Modifier.weight(1f))
            //box7
            SinglePlayerGameButton(
                box = uiState.boxes.Box7,
                onClick = {
                    if (uiState.boxes.Box7 == "") {
                        viewModel.SetBox(7)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box8
            SinglePlayerGameButton(
                box = uiState.boxes.Box8,
                onClick = {
                    if (uiState.boxes.Box8 == "") {
                        viewModel.SetBox(8)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box9
            SinglePlayerGameButton(
                box = uiState.boxes.Box9,
                onClick = {
                    if (uiState.boxes.Box9 == "") {
                        viewModel.SetBox(9)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * Show the single player game screen
 */

@Composable
fun TicTacToeSinglePlayerScreen(
    viewModel: TicTacToeViewModel,
    uiState: UiState,
    onPlayAgain: () -> Unit,
    playerName: String,
    navController: NavController
) {
    val player = getPlayer(email = playerName, context = LocalContext.current)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val size = (screenWidth/10)*3

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(BackGround)
            .fillMaxSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(2f)) {
            Spacer(modifier = Modifier.weight(1f))
            Card(modifier = Modifier
                .size(size)
                .weight(3f), elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (uiState.player_Turn == "X") Primery else { Secondery})) {
                Image(
                    painterResource(id = player.currentImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("${uiState.player1Score} : ${uiState.player2Score}", fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Card(modifier = Modifier
                .size(size)
                .weight(3f), elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (uiState.player_Turn == "O") Primery else { Secondery})) {
                Image(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(1f))
        SinglePlayerButtonGrid(
            viewModel = viewModel,
            onPlayAgain = {
                onPlayAgain()
            },
            uiState = uiState,
            playerName = playerName,
            navController = navController,
            modifier = Modifier.weight(6f)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}