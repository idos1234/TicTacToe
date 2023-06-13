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

/**
 * [SettingScreen] Show the settings screen
 */

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

/**
 * Show the players from the data
 */

@Composable
fun ShowPlayersButton(Players: List<Player> = listOf()) {
    var toShowPlayers by remember {
        mutableStateOf(false)
    }
    val icon = when(toShowPlayers) {
        true -> Icons.Default.KeyboardArrowDown
        else -> Icons.Default.KeyboardArrowUp
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(elevation = 4.dp, backgroundColor = Secondery) {
                Row() {
                    Text(text = "Show Players", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(4.dp))

                    IconButton(onClick = { toShowPlayers = !toShowPlayers }) {
                        Icon(imageVector = icon, contentDescription = null, tint = Color.Black)
                    }
            }
        }
        if (toShowPlayers) {
            ShowPlayers(Players = Players)
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
                Card(backgroundColor = Secondery, elevation = 10.dp, modifier = Modifier
                    .width(250.dp)
                    .padding(16.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = "${player.id}.", fontSize = 30.sp, modifier = Modifier.weight(1f))
                            Text(text = player.name, fontSize = 30.sp, modifier = Modifier.weight(2f))
                        }
                    }
                }
            }
        }
    }
}