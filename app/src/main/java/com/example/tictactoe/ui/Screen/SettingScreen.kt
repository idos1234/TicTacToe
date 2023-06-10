package com.example.tictactoe.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.data.Player
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.AppViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.ui.theme.Secondery

@JvmOverloads
@Composable
fun SettingScreen(
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val settingsUiState by viewModel.settingsUiState.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(BackGround)) {
        ShowPlayersButton(settingsUiState.playerList)
    }
}

@Composable
fun ShowPlayersButton(Players: List<Player> = listOf()) {
    var toShowPlayers by remember {
        mutableStateOf(false)
    }
    val icon = when(toShowPlayers) {
        true -> Icons.Default.KeyboardArrowDown
        else -> Icons.Default.KeyboardArrowUp
    }

    Card(elevation = 4.dp, backgroundColor = Secondery) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row() {
                    Text(text = "Show Players", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(4.dp))

                    IconButton(onClick = { toShowPlayers = !toShowPlayers }) {
                        Icon(imageVector = icon, contentDescription = null, tint = Color.Black)
                    }
            }
            if (toShowPlayers) {
                ShowPlayers(Players = Players)
            }
        }
    }
}

@Composable
fun ShowPlayers(Players: List<Player>) {
    if (Players.isEmpty()) {
        Text(text = "There Are Not Players", color = Color.Black)
    } else {
        LazyColumn(verticalArrangement = Arrangement.Center) {
            items(items = Players, key = { it.id }) { player ->
                Card {
                    Text(text = player.name, fontSize = 50.sp)
                }
            }
        }
    }
}