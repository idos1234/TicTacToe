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
import com.example.tictactoe.data.PlayerUiState
import com.example.tictactoe.ui.AppViewModelProvider
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes
import kotlinx.coroutines.launch

@Composable
fun ShowPlayersScreen(
    signUpViewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory),
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModel: TicTacToeViewModel,
    onValueChange: (PlayerUiState) -> Unit,
    playerNumber: Int,
    isSinglePlayer: Boolean,
    ) {
    val signUpUiState = signUpViewModel.playerUiState
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
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item{
                if (settingsUiState.playerList.isEmpty()) {
                    Column() {
                        Text(text = "There Are Not Players", color = Color.Black)
                    }

                } else {
                    settingsUiState.playerList.forEach{ player ->

                        TextButton(
                            onClick = {
                                if (isSinglePlayer){
                                    viewModel.setPlayers(player.name, "Bot")
                                }
                                 else {
                                        if (playerNumber == 1){
                                            viewModel.setPlayers(player1 = player.name)
                                        }

                                        if (playerNumber == 2){
                                            viewModel.setPlayers(player2 = player.name)
                                        }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Card(elevation = 10.dp, backgroundColor = Secondery, modifier = Modifier.fillMaxWidth()) {
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

            item {
                if(toAddPlayer) {
                    OutlinedTextField(
                        value = signUpUiState.name,
                        onValueChange = { onValueChange(signUpUiState.copy(name = it)) },
                        singleLine = true,
                        label = { Text(text = "Player") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.LightGray, textColor = Color.Black),
                        shape = Shapes.large,
                    )
                }
            }

            item{
                Button(
                    onClick = {
                        if (toAddPlayer) {
                            coroutineScope.launch{
                                signUpViewModel.savePlayer()
                            }
                        }
                        toAddPlayer = !toAddPlayer
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun chooseSinglePlayerScreen(
    signUpViewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModel: TicTacToeViewModel,
    playerNumber: Int = 1,
    modifier: Modifier = Modifier,
    isSinglePlayer: Boolean = true
) {

    val uiState by viewModel.uiState.collectAsState()

    var toShowPlayers by remember {
        mutableStateOf(false)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = modifier
        .fillMaxSize()
        .background(BackGround)) {

        Text("Player: ${if (playerNumber == 1) uiState.player1 else uiState.player2}", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

        Button(onClick = {toShowPlayers = !toShowPlayers}) {
            Text("Show Players", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }

        if (toShowPlayers) {
            ShowPlayersScreen(
                onValueChange = signUpViewModel::updateUiState,
                viewModel = viewModel,
                playerNumber = playerNumber,
                isSinglePlayer = isSinglePlayer
            )
        }

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(Secondery)
        ) {
            Text("Ready", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun chooseTwoPlayersScreen (
    viewModel: TicTacToeViewModel
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        chooseSinglePlayerScreen(
            modifier = Modifier.weight(1f),
            viewModel = viewModel,
            playerNumber = 1,
            isSinglePlayer = false
        )
        chooseSinglePlayerScreen(
            modifier = Modifier.weight(1f),
            viewModel = viewModel,
            playerNumber = 2,
            isSinglePlayer = false
        )
    }
}