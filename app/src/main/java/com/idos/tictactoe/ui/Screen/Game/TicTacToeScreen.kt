package com.idos.tictactoe.ui.Screen.Game

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.idos.tictactoe.R
import com.idos.tictactoe.data.GetO
import com.idos.tictactoe.data.GetX
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.UiState
import com.idos.tictactoe.ui.CheckWinner
import com.idos.tictactoe.ui.Screen.GameScreen
import com.idos.tictactoe.ui.Screen.Menu.getPlayer
import com.idos.tictactoe.ui.theme.Secondery

/**
 * Show a single button in the two players game grid
 */

@Composable
fun GameButton(box: String, onClick: () -> Unit = {}, context: Context = LocalContext.current, playerName: String, modifier: Modifier) {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }

    //get Players collection from database
    player = getPlayer(playerName, context)


    val colors = MaterialTheme.colorScheme

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    Card(
        shape = RoundedCornerShape(20),
        modifier = modifier
            .padding(5.dp)
            .size(((screenWidth - 20 - 30) / 3).dp)
            .background(Color.Transparent)
            .clickable(
                onClick = onClick,
                enabled = box.isEmpty(),
            ),
        border = BorderStroke(1.dp, colors.onPrimary),
        colors = CardDefaults.cardColors(containerColor = colors.primary)
    ) {
        Image(
            painter = painterResource(
                id = when (box) {
                    "X" -> GetX( player.currentX )
                    "O" -> GetO( player.currentO )
                    else -> {
                        R.drawable.o_1}
                }
            ),
            alpha = if (box != "") DefaultAlpha else 0f,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
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

             ShowWinner(
                 winner = "Winner is: ${uiState.winner}!",
                 text = "Congratulations for winning",
                 onPlayAgain = { onPlayAgain() },
                 navController = navController
             )
        }
        //show tie
        else if (uiState.times == 9){
            ShowWinner(
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


    Column(
        modifier = modifier
            .background(Color.Transparent)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)) {
            GameButton(
                box = uiState.boxes.Box1,
                onClick = {
                    if (uiState.boxes.Box1 == "") {
                        viewModel.SetBox(1)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.Box2,
                onClick = {
                    if (uiState.boxes.Box2 == "") {
                        viewModel.SetBox(2)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.Box3,
                onClick = {
                    if (uiState.boxes.Box3 == "") {
                        viewModel.SetBox(3)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)) {
            GameButton(
                box = uiState.boxes.Box4,
                onClick = {
                    if (uiState.boxes.Box4 == "") {
                        viewModel.SetBox(4)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.Box5,
                onClick = {
                    if (uiState.boxes.Box5 == "") {
                        viewModel.SetBox(5)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.Box6,
                onClick = {
                    if (uiState.boxes.Box6 == "") {
                        viewModel.SetBox(6)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)) {
            GameButton(
                box = uiState.boxes.Box7,
                onClick = {
                    if (uiState.boxes.Box7 == "") {
                        viewModel.SetBox(7)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.Box8,
                onClick = {
                    if (uiState.boxes.Box8 == "") {
                        viewModel.SetBox(8)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.Box9,
                onClick = {
                    if (uiState.boxes.Box9 == "") {
                        viewModel.SetBox(9)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

/**
 * Show the winner
 */

@Composable
fun ShowWinner(winner: String, text: String, onPlayAgain: () -> Unit, navController: NavController) {

    AlertDialog(
        containerColor = Secondery,
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



@Composable
private fun ShowPlayersBar(
    modifier: Modifier,
    colors: ColorScheme,
    screenWidth: Int,
    player: MainPlayerUiState,
    uiState: UiState
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .size(((screenWidth - 40) / 4).dp),
                shape = RoundedCornerShape(20),
                border = BorderStroke(2.dp, if (uiState.player_Turn == "X") colors.tertiary else { colors.background})
            ) {
                Image(
                    painter = painterResource(id = GetXO(player.currentImage)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20))
                )
            }
            Text(
                text = "Player 1",
                fontSize = screenWidth.sp * 0.05,
                color = colors.onBackground
            )
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = colors.primary),
            modifier = Modifier
                .height(((screenWidth - 40) / 3 / 3).dp)
                .weight(1f)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "${uiState.player1Score} : ${uiState.player2Score}",
                    textAlign = TextAlign.Left,
                    fontSize = screenWidth.sp * 0.04,
                    color = colors.onPrimary
                )
            }
        }
        Column(
            Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .size(((screenWidth - 40) / 4).dp),
                shape = RoundedCornerShape(20),
                border = BorderStroke(2.dp, if (uiState.player_Turn == "O") colors.tertiary else { colors.background})
            ) {
                Image(
                    painter = painterResource(id = GetXO(player.currentImage)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20))
                )
            }
            Text(
                text = "Player 2",
                fontSize = screenWidth.sp * 0.05,
                color = colors.onBackground
            )
        }
    }
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
    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(brush)
            .fillMaxSize()
    ) {
        ShowPlayersBar(
            modifier = Modifier.weight(2f),
            colors = colors,
            screenWidth = screenWidth.value.toInt(),
            player = player,
            uiState = uiState
        )
        ButtonGrid(
            viewModel = viewModel,
            onPlayAgain = {
                onPlayAgain()
            },
            uiState = uiState,
            playerName = playerName,
            navController = navController,
            modifier = Modifier
                .padding(10.dp)
                .weight(3f)
                .fillMaxSize()
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}