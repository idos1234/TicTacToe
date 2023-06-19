package com.example.tictactoe.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.data.PlayerUiState
import com.example.tictactoe.ui.AppViewModelProvider
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes

@Composable
fun ShowPlayersScreen(
    signUpViewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory),
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onPlayerClick: () -> Unit,
    onValueChange: (PlayerUiState) -> Unit,
    ) {
    val signUpUiState = signUpViewModel.playerUiState
    val settingsUiState by settingsViewModel.settingsUiState.collectAsState()

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
        modifier = Modifier
            .background(Secondery)
            .height(100.dp)) {
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
                                onPlayerClick()
                            }
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

            item{
                Button(
                    onClick = {
                        toAddPlayer = !toAddPlayer
                }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null)
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
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Secondery),
                        shape = Shapes.large,
                    )
                }
            }
        }
    }
}