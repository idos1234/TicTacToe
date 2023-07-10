package com.example.tictactoe.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.data.Player
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.data.UiState
import com.example.tictactoe.ui.AppViewModelProvider
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

/**
 * Show All The Players To Choose
 */

@Composable
fun ShowPlayersScreen(
    signUpViewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory),
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModel: TicTacToeViewModel,
    uiState: UiState,
    playerUiState: MainPlayerUiState,
    onValueChange: (MainPlayerUiState) -> Unit = {},
    playerNumber: Int,
    isSinglePlayer: Boolean,
    onPlayerClick: () -> Unit,
    ) {
    val settingsUiState by settingsViewModel.settingsUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var toAddPlayer by remember {
        mutableStateOf(false)
    }

    val icon = if (toAddPlayer) {
        Icons.Default.Done
    } else{
        Icons.Default.Add
    }

    Card(
        elevation = 10.dp,
        backgroundColor = Secondery,
        modifier = Modifier
            .height(300.dp)
            .width(200.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    if (settingsUiState.playerList.isEmpty()) {
                        Column() {
                            Text(text = "There Are Not Players", color = Color.Black)
                        }

                    } else {
                        settingsUiState.playerList.forEach { player ->

                            TextButton(
                                onClick = {
                                    if (isSinglePlayer) {
                                        viewModel.setPlayers(player, Player(name = "Bot"))
                                    } else {
                                        if (playerNumber == 1) {
                                            viewModel.setPlayers(
                                                player1 = player,
                                                player2 = uiState.player2
                                            )
                                        }

                                        if (playerNumber == 2) {
                                            viewModel.setPlayers(
                                                player1 = uiState.player1,
                                                player2 = player
                                            )
                                        }
                                    }
                                    Timer().schedule(100) {
                                        onPlayerClick()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Card(
                                    elevation = 10.dp,
                                    backgroundColor = Secondery,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = player.name,
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (toAddPlayer) {
                OutlinedTextField(
                    value = playerUiState.name,
                    onValueChange = { onValueChange(playerUiState.copy(name = it)) },
                    singleLine = true,
                    label = { Text(text = "Player") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.LightGray,
                        textColor = Color.Black
                    ),
                    shape = Shapes.large,
                )
            }

            Button(
                onClick = {
                    if (toAddPlayer) {
                        coroutineScope.launch {
                            signUpViewModel.savePlayer()
                        }
                    }
                    toAddPlayer = !toAddPlayer
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }
    }
}

/**
 * Choose Player For [TicTacToeSinglePlayerScreen]
 */

@Composable
fun chooseSinglePlayerScreen(
    signUpViewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModel: TicTacToeViewModel,
    playerNumber: Int = 1,
    modifier: Modifier = Modifier,
    isSinglePlayer: Boolean = true,
    onReadyClicked: () -> Unit,
    isPlayerReady: Boolean = false,
    uiState: UiState,
    onPlayerClick: () -> Unit,
) {


    var toShowPlayers by remember {
        mutableStateOf(false)
    }

    val buttonSize = if (isSinglePlayer) {
        20.sp
    } else {
        15.sp
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = modifier
        .fillMaxSize()
        .background(BackGround)) {

        if (!isSinglePlayer && isPlayerReady) {
            Text(text = "Ready!", fontWeight = FontWeight.SemiBold, fontSize = 30.sp)
        }

        Text("Player: ${if (playerNumber == 1) uiState.player1.name else uiState.player2.name}", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {toShowPlayers = !toShowPlayers}) {
            Text("Show Players", fontSize = buttonSize, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (toShowPlayers) {
            ShowPlayersScreen(
                onValueChange = signUpViewModel::updateUiState,
                viewModel = viewModel,
                playerNumber = playerNumber,
                isSinglePlayer = isSinglePlayer,
                uiState = uiState,
                onPlayerClick = onPlayerClick,
                playerUiState = signUpViewModel.playerUiState
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                      if (isSinglePlayer) {
                          if (uiState.player1.name != "") {
                              onReadyClicked()
                          }
                      } else
                          onReadyClicked()
            },
            colors = ButtonDefaults.buttonColors(Secondery)
        ) {
            Text("Ready", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

/**
 * Choose Player For [TicTacToeScreen]
 */

@Composable
fun chooseTwoPlayersScreen (
    viewModel: TicTacToeViewModel,
    onReadyClicked: () -> Unit,
    uiState: UiState,
    onPlayerClick: () -> Unit
) {

    var isPlayer1Ready by remember {
        mutableStateOf(false)
    }

    var isPlayer2Ready by remember {
        mutableStateOf(false)
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(BackGround)) {
        chooseSinglePlayerScreen(
            modifier = Modifier
                .weight(1f)
                .padding(15.dp),
            viewModel = viewModel,
            playerNumber = 1,
            isSinglePlayer = false,
            onReadyClicked = {
                if (uiState.player1.name != "") isPlayer1Ready = !isPlayer1Ready
                if (isPlayer1Ready == true && isPlayer2Ready == true) onReadyClicked()
            },
            isPlayerReady = isPlayer1Ready,
            uiState = uiState,
            onPlayerClick = onPlayerClick
        )
        chooseSinglePlayerScreen(
            modifier = Modifier
                .weight(1f)
                .padding(15.dp),
            viewModel = viewModel,
            playerNumber = 2,
            isSinglePlayer = false,
            onReadyClicked = {
                if (uiState.player2.name != "") isPlayer2Ready = !isPlayer2Ready
                if (isPlayer1Ready == true && isPlayer2Ready == true) onReadyClicked()
            },
            isPlayerReady = isPlayer2Ready,
            uiState = uiState,
            onPlayerClick = onPlayerClick
        )
    }
}