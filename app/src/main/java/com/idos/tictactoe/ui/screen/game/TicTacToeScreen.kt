package com.idos.tictactoe.ui.screen.game

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.idos.tictactoe.R
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.UiState
import com.idos.tictactoe.data.getO
import com.idos.tictactoe.data.getX
import com.idos.tictactoe.data.getXO
import com.idos.tictactoe.ui.checkWinner
import com.idos.tictactoe.ui.screen.GameScreen
import com.idos.tictactoe.ui.screen.menu.getPlayer

/**
 * Show a single button in the two players game grid
 */

@Composable
fun GameButton(box: String, onClick: () -> Unit = {}, playerName: String, modifier: Modifier) {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }

    //get Players collection from database
    player = getPlayer(playerName)


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
                    "X" -> getX( player.currentX )
                    "O" -> getO( player.currentO )
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
    if(uiState.toCheck) {
        uiState.winner = checkWinner(uiState)
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
                 winner = "Winner is: ${uiState.winner}",
                 text = "Congratulations for winning",
                 onPlayAgain = { onPlayAgain() },
                 navController = navController
             )
        }
        //show tie
        else if (uiState.times == 9){
            ShowWinner(
                winner = "Draw",
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
                box = uiState.boxes.box1,
                onClick = {
                    if (uiState.boxes.box1 == "") {
                        viewModel.setBox(1)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.box2,
                onClick = {
                    if (uiState.boxes.box2 == "") {
                        viewModel.setBox(2)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.box3,
                onClick = {
                    if (uiState.boxes.box3 == "") {
                        viewModel.setBox(3)
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
                box = uiState.boxes.box4,
                onClick = {
                    if (uiState.boxes.box4 == "") {
                        viewModel.setBox(4)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.box5,
                onClick = {
                    if (uiState.boxes.box5 == "") {
                        viewModel.setBox(5)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.box6,
                onClick = {
                    if (uiState.boxes.box6 == "") {
                        viewModel.setBox(6)
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
                box = uiState.boxes.box7,
                onClick = {
                    if (uiState.boxes.box7 == "") {
                        viewModel.setBox(7)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.box8,
                onClick = {
                    if (uiState.boxes.box8 == "") {
                        viewModel.setBox(8)
                        onClick()
                    }
                },
                playerName = playerName,
                modifier = Modifier
                    .weight(1f)
            )
            GameButton(
                box = uiState.boxes.box9,
                onClick = {
                    if (uiState.boxes.box9 == "") {
                        viewModel.setBox(9)
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
    val colors = MaterialTheme.colorScheme
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            Modifier
                .fillMaxWidth(0.8f)
                .background(colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                Text(
                    text = winner,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.onBackground,
                    fontSize = screenHeight.value.sp * 0.02,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "!",
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.onBackground,
                    fontSize = screenHeight.value.sp * 0.02,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = text,
                color = colors.onBackground,
                textAlign = TextAlign.Center,
                fontSize = screenHeight.value.sp * 0.02,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(GameScreen.Home.title)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Exit",
                        fontSize = screenHeight.value.sp * 0.02
                    )
                }


                TextButton(
                    onClick = onPlayAgain,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Play again",
                        fontSize = screenHeight.value.sp * 0.02
                    )
                }
            }
        }
    }
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
                border = BorderStroke(2.dp, if (uiState.playerTurn == "X") colors.tertiary else { colors.background})
            ) {
                Image(
                    painter = painterResource(id = getXO(player.currentImage)),
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
                border = BorderStroke(2.dp, if (uiState.playerTurn == "O") colors.tertiary else { colors.background})
            ) {
                Image(
                    painter = painterResource(id = getXO(player.currentImage)),
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
    val player = getPlayer(email = playerName)
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