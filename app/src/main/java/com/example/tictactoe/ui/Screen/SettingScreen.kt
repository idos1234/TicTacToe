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
import com.example.tictactoe.data.Player
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.AppViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.data.PlayerUiState
import com.example.tictactoe.data.isValid
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * [SettingScreen] Show the settings screen
 */

@JvmOverloads
@Composable
fun SettingScreen(
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    signUpViewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val settingsUiState by viewModel.settingsUiState.collectAsState()
    val signUpUiState = signUpViewModel.playerUiState

    val coroutineScope = rememberCoroutineScope()



    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(BackGround)) {
        item {
            ShowPlayersButton(
                Players = settingsUiState.playerList,
                playerUiState = signUpUiState,
                onValueChange = signUpViewModel::updateUiState,
                coroutineScope = coroutineScope,
                viewModel = signUpViewModel
            )
        }
    }
}

/**
 * Show the players from the data
 */

@Composable
fun ShowPlayersButton(
    Players: List<Player> = listOf(),
    playerUiState: PlayerUiState,
    onValueChange: (PlayerUiState) -> Unit,
    coroutineScope: CoroutineScope,
    viewModel: SignUpViewModel) {

    var toShowPlayers by remember {
        mutableStateOf(false)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {toShowPlayers = !toShowPlayers},
            colors = ButtonDefaults.buttonColors(Secondery), shape = Shapes.large, modifier = Modifier.width(200.dp)) {
            Text(text = "Show Players", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black, modifier = Modifier.padding(4.dp))
        }
        if (toShowPlayers) {
            ShowPlayers(
                Players = Players,
                playerUiState = playerUiState,
                onValueChange = onValueChange,
                coroutineScope = coroutineScope,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ShowPlayers(
    Players: List<Player> = listOf(),
    playerUiState: PlayerUiState,
    onValueChange: (PlayerUiState) -> Unit = {},
    coroutineScope: CoroutineScope,
    viewModel: SignUpViewModel
) {

    var toAddPlayer by remember {
        mutableStateOf(false)
    }

    val icon = if (toAddPlayer) {
        Icons.Default.Done
    } else{
        Icons.Default.Add
    }


    Card(
        backgroundColor = BackGround,
        elevation = 25.dp
    ) {
        if (Players.isEmpty()) {
            Column() {
                Text(text = "There Are Not Players", color = Color.Black)
                Button(
                    onClick = { toAddPlayer = !toAddPlayer },
                    modifier = Modifier.width(250.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Players.forEach() { player ->
                    Card(
                        backgroundColor = Secondery, elevation = 10.dp, modifier = Modifier
                            .width(250.dp)
                            .padding(5.dp)
                    ) {
                        Text(
                            text = player.name,
                            fontSize = 15.sp,
                            modifier = Modifier.weight(2f),
                            textAlign = TextAlign.Center
                        )
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
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Secondery),
                        shape = Shapes.large,
                    )
                }
                Button(
                    onClick = {
                        if (toAddPlayer) {
                            if (playerUiState.isValid()) {
                                coroutineScope.launch {
                                    toAddPlayer = false
                                    viewModel.savePlayer()
                                }
                            } else {
                            }
                        } else {
                            toAddPlayer = true
                        } },
                    modifier = Modifier.width(250.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}