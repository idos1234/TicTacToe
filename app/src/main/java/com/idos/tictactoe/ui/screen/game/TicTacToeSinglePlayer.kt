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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.idos.tictactoe.R
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.UiState
import com.idos.tictactoe.data.getO
import com.idos.tictactoe.data.getX
import com.idos.tictactoe.data.getXO
import com.idos.tictactoe.ui.checkWinner
import com.idos.tictactoe.ui.screen.menu.getPlayer
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * Show a single button in the single player game grid
 */

@Composable
fun SinglePlayerGameButton(
    box: String,
    onClick: () -> Unit = {},
    viewModel: TicTacToeViewModel,
    playerName: String,
    modifier: Modifier,
    botDifficulty: Int
) {
    val uiState by viewModel.uiState.collectAsState()
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
                onClick = {
                    onClick()
                    if (box.isEmpty()) {
                        Timer().schedule(1000) {
                            if (uiState.winner == "") {
                                viewModel.botTurn(uiState = uiState, botDifficulty = botDifficulty)
                            }
                        }
                    }
                },
                enabled = uiState.isEnabled
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
    modifier: Modifier,
    botDifficulty: Int
) {
    //if need to check winner
    if(uiState.toCheck) {
        uiState.winner = checkWinner(uiState)
        if(uiState.winner != "") {
            if(uiState.winner == "X") {
                //Won
                if (!uiState.isScoreUpdated) {
                    viewModel.updateScore(1)
                }
                ShowWinner(
                    winner = "You won",
                    text = "Congratulations for winning",
                    onPlayAgain = { onPlayAgain() },
                    navController = navController
                )
            } else if(uiState.winner == "O") {
                //Lost
                if (!uiState.isScoreUpdated) {
                    viewModel.updateScore(2)
                }
                ShowWinner(
                    winner = "You lost",
                    text = "Try to win next time",
                    onPlayAgain = { onPlayAgain() },
                    navController = navController
                )
            }
        }
        //show tie
        else if (uiState.times >= 9){
            ShowWinner(
                winner = "Draw",
                text = "Try to win next time",
                onPlayAgain = { onPlayAgain() },
                navController = navController)
        }

    } else {
        uiState.winner = ""
    }

    val onClick = {
        viewModel.onSinglePlayerClick()
        viewModel.checkToCheck()
        if (uiState.winner == "") {
            viewModel.changePlayer()
        }
    }



    Column(modifier = modifier) {
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            //box1
            SinglePlayerGameButton(
                box = uiState.boxes.box1,
                onClick = {
                    if (uiState.boxes.box1 == "") {
                        viewModel.setBox(1)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(1f),
                botDifficulty = botDifficulty
            )
            //box2
            SinglePlayerGameButton(
                box = uiState.boxes.box2,
                onClick = {
                    if (uiState.boxes.box2 == "") {
                        viewModel.setBox(2)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(1f),
                botDifficulty = botDifficulty
            )
            //box3
            SinglePlayerGameButton(
                box = uiState.boxes.box3,
                onClick = {
                    if (uiState.boxes.box3 == "") {
                        viewModel.setBox(3)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(1f),
                botDifficulty = botDifficulty
            )
        }
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            //box4
            SinglePlayerGameButton(
                box = uiState.boxes.box4,
                onClick = {
                    if (uiState.boxes.box4 == "") {
                        viewModel.setBox(4)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(1f),
                botDifficulty = botDifficulty
            )
            //box5
            SinglePlayerGameButton(
                box = uiState.boxes.box5,
                onClick = {
                    if (uiState.boxes.box5 == "") {
                        viewModel.setBox(5)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(1f),
                botDifficulty = botDifficulty
            )
            //box6
            SinglePlayerGameButton(
                box = uiState.boxes.box6,
                onClick = {
                    if (uiState.boxes.box6 == "") {
                        viewModel.setBox(6)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(1f),
                botDifficulty = botDifficulty
            )
        }
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            //box7
            SinglePlayerGameButton(
                box = uiState.boxes.box7,
                onClick = {
                    if (uiState.boxes.box7 == "") {
                        viewModel.setBox(7)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(1f),
                botDifficulty = botDifficulty
            )
            //box8
            SinglePlayerGameButton(
                box = uiState.boxes.box8,
                onClick = {
                    if (uiState.boxes.box8 == "") {
                        viewModel.setBox(8)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(1f),
                botDifficulty = botDifficulty
            )
            //box9
            SinglePlayerGameButton(
                box = uiState.boxes.box9,
                onClick = {
                    if (uiState.boxes.box9 == "") {
                        viewModel.setBox(9)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName,
                modifier = Modifier.weight(1f),
                botDifficulty = botDifficulty
            )
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
                text = player.name,
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
                text = "Bot",
                fontSize = screenWidth.sp * 0.05,
                color = colors.onBackground
            )
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
    navController: NavController,
    botDifficulty: Int
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
        SinglePlayerButtonGrid(
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
                .fillMaxSize(),
            botDifficulty = botDifficulty
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}