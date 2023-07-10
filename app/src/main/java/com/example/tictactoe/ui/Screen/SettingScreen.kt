package com.example.tictactoe.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import com.example.tictactoe.data.MainPlayerUiState
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
    val isDialogOpenUiState by viewModel.isDialogOpen.collectAsState()

    val coroutineScope = rememberCoroutineScope()


    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(BackGround)) {

        Text(text = "Settings", fontWeight = FontWeight.Bold, fontSize = 70.sp)
        
        Spacer(modifier = Modifier.height(50.dp))
        
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxSize()
                .background(BackGround)
        ) {
            item {
                ShowPlayersButton(
                    Players = settingsUiState.playerList,
                    playerUiState = signUpUiState,
                    onValueChange = signUpViewModel::updateUiState,
                    coroutineScope = coroutineScope,
                    viewModel = signUpViewModel,
                    settingsViewModel = viewModel,
                    uiState = isDialogOpenUiState

                )
            }

            item {
                Spacer(modifier = Modifier.height(50.dp))
            }

            item {
                ClearDataButton(
                    signUpViewModel = signUpViewModel,
                    settingsViewModel = viewModel,
                    uiState = isDialogOpenUiState,
                    coroutineScope = coroutineScope
                )
            }
        }
    }
}

/**
 * Show the players from the data
 */

@Composable
fun ShowPlayersButton(
    Players: List<Player> = listOf(),
    playerUiState: MainPlayerUiState,
    onValueChange: (MainPlayerUiState) -> Unit,
    coroutineScope: CoroutineScope,
    viewModel: SignUpViewModel,
    settingsViewModel: SettingsViewModel,
    uiState: isDialogOpen
) {


    Button(
         onClick = {settingsViewModel.ChangeShowingPlayersAlertDialog()},
         colors = ButtonDefaults.buttonColors(Secondery), shape = Shapes.large, modifier = Modifier.width(200.dp)) {
         Text(text = "Show Players", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black, modifier = Modifier.padding(4.dp))
     }
    if (uiState.isShowingPlayersDialogOpen) {
        ShowPlayers(
            Players = Players,
            playerUiState = playerUiState,
            onValueChange = onValueChange,
            coroutineScope = coroutineScope,
            viewModel = viewModel,
            settingsViewModel = settingsViewModel,
        )
    }
}

@Composable
fun ShowPlayers(
    Players: List<Player> = listOf(),
    playerUiState: MainPlayerUiState,
    onValueChange: (MainPlayerUiState) -> Unit = {},
    coroutineScope: CoroutineScope,
    viewModel: SignUpViewModel,
    settingsViewModel: SettingsViewModel,
) {

    var toAddPlayer by remember {
        mutableStateOf(false)
    }

    val icon = if (toAddPlayer) {
        Icons.Default.Done
    } else{
        Icons.Default.Add
    }

    AlertDialog(
        backgroundColor = BackGround,
        title = {
            IconButton(onClick = {settingsViewModel.ChangeShowingPlayersAlertDialog()}) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
                },
        text = {
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
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.height(300.dp)
                ) {
                    Players.forEach() { player ->
                        item {
                            Card(
                                backgroundColor = Secondery,
                                elevation = 10.dp,
                                modifier = Modifier
                                    .width(250.dp)
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = player.name,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    item {
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
                    }
                    item {
                        Button(
                            onClick = {
                                if (toAddPlayer) {
                                    if (playerUiState.isValid()) {
                                        coroutineScope.launch {
                                            toAddPlayer = false
                                            viewModel.savePlayer()
                                        }
                                    }
                                } else {
                                    toAddPlayer = true
                                }
                                      },
                            colors = ButtonDefaults.buttonColors(Secondery),
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
               },
        onDismissRequest = {},
        confirmButton = {}
    )
}


/**
 * This function clear the players data from the game
 */

@Composable
fun ClearDataButton(
    signUpViewModel: SignUpViewModel,
    settingsViewModel: SettingsViewModel,
    coroutineScope: CoroutineScope,
    uiState: isDialogOpen
) {

    Button(
        onClick = {
            settingsViewModel.ChangeCheckClearDataAlertDialog()
        },
        colors = ButtonDefaults.buttonColors(Secondery),
    ) {
        Text(text = "Clear Data", textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
    }

    if (uiState.isCheckClearDataDialogOpen) {
        CheckClearData(
            viewModel = signUpViewModel,
            coroutineScope = coroutineScope,
            onCancelClick = {settingsViewModel.ChangeCheckClearDataAlertDialog()}
        )
    }
}

@Composable
fun CheckClearData(
    viewModel: SignUpViewModel,
    coroutineScope: CoroutineScope,
    onCancelClick: () -> Unit
) {

    AlertDialog(
        title = { Text(text = "Are you sure you want to clear all data?") },
        onDismissRequest = {},
        dismissButton = {
            TextButton(
                onClick = { onCancelClick() }
            ) {
                Text("Cancel")
            }
                        },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.clearData()
                        }
                    }
                ) {
                   Text("Clear")
                }
            }
        )
}