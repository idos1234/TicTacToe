package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.idos.tictactoe.R
import com.idos.tictactoe.data.GetO
import com.idos.tictactoe.data.GetX
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.UiState
import com.idos.tictactoe.ui.CheckWinner
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Primery
import com.idos.tictactoe.ui.theme.Secondery

/**
 * Show a single button in the two players game grid
 */

@Composable
fun GameButton(box: String, onClick: () -> Unit = {}, context: Context = LocalContext.current, playerName: String, modifier: Modifier) {
    var player by remember {
        mutableStateOf(com.idos.tictactoe.data.MainPlayerUiState())
    }

    //get Players collection from database
    player = getPlayer(playerName, context)

    Card(modifier = modifier,shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        Image(
            painter = painterResource(
                id = when (box) {
                    "X" -> GetX( player.currentX )
                    "O" -> GetO( player.currentO )
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
                    onClick = onClick,
                    enabled = box.isEmpty(),
                )
        )
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
            //show winner

            if(!uiState.isScoreUpdated) {
                if(uiState.winner == "X") {
                    viewModel.updateScore(1)
                } else if(uiState.winner == "O") {
                    viewModel.updateScore(2)
                }
            }

             showWinner(
                 winner = "Winner is: ${uiState.winner}!",
                 text = "Congratulations for winning",
                 onPlayAgain = { onPlayAgain() },
                 navController = navController
             )
        }
        //show tie
        else if (uiState.times == 9){
            showWinner(
                winner = "Draw!",
                text = "Try to win next time",
                onPlayAgain = { onPlayAgain() },
                navController)
        }

    } else {
        uiState.winner = ""
    }

    val onClick = {
        viewModel.onClick()
    }



    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(0.5f))
        Row(modifier = Modifier.weight(5f)) {
            Spacer(modifier = Modifier.weight(1f))
            //box1
            GameButton(
                box = uiState.boxes.Box1,
                onClick = {
                    if (uiState.boxes.Box1 == "") {
                        viewModel.SetBox(1)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box2
            GameButton(
                box = uiState.boxes.Box2,
                onClick = {
                    if (uiState.boxes.Box2 == "") {
                        viewModel.SetBox(2)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box3
            GameButton(
                box = uiState.boxes.Box3,
                onClick = {
                    if (uiState.boxes.Box3 == "") {
                        viewModel.SetBox(3)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Row(modifier = Modifier.weight(5f)) {
            Spacer(modifier = Modifier.weight(1f))
            //box4
            GameButton(
                box = uiState.boxes.Box4,
                onClick = {
                    if (uiState.boxes.Box4 == "") {
                        viewModel.SetBox(4)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box5
            GameButton(
                box = uiState.boxes.Box5,
                onClick = {
                    if (uiState.boxes.Box5 == "") {
                        viewModel.SetBox(5)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box6
            GameButton(
                box = uiState.boxes.Box6,
                onClick = {
                    if (uiState.boxes.Box6 == "") {
                        viewModel.SetBox(6)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Row(modifier = Modifier.weight(5f)) {
            Spacer(modifier = Modifier.weight(1f))
            //box7
            GameButton(
                box = uiState.boxes.Box7,
                onClick = {
                    if (uiState.boxes.Box7 == "") {
                        viewModel.SetBox(7)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)

            )
            Spacer(modifier = Modifier.weight(1f))
            //box8
            GameButton(
                box = uiState.boxes.Box8,
                onClick = {
                    if (uiState.boxes.Box8 == "") {
                        viewModel.SetBox(8)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
            //box9
            GameButton(
                box = uiState.boxes.Box9,
                onClick = {
                    if (uiState.boxes.Box9 == "") {
                        viewModel.SetBox(9)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier.weight(5f).size(size)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * Show the winner
 */

@Composable
fun showWinner(winner: String, text: String, onPlayAgain: () -> Unit, navController: NavController) {

    AlertDialog(
        backgroundColor = Secondery,
        onDismissRequest = {},
        title = { Text(text = winner, fontWeight = FontWeight.ExtraBold, color = Color.Black, textAlign = TextAlign.Center)},
        text = { Text(text = text, color = Color.Black, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)},
        dismissButton = {
            TextButton(
                onClick = {
                    navController.navigate(GameScreen.Start.title)
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
    onPlayAgain: () -> Unit,
    playerName: String,
    navController: NavController
) {
    val player = getPlayer(email = playerName, context = LocalContext.current)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val size = (screenWidth/10)*3

    val currentImage = GetXO(player.currentImage)

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(2f)) {
            Spacer(modifier = Modifier.weight(1f))
            Card(modifier = Modifier
                .size(size)
                .weight(3f)
                , elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (uiState.player_Turn == "X") Primery else { Secondery})) {
                Image(
                    painterResource(id = currentImage),
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
                    painterResource(id = currentImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(1f))
        ButtonGrid(
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