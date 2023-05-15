package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

enum class GameScreen(@SuppressLint("SupportAnnotationUsage") @StringRes val title: String) {
    Start(title = "TicTacToe"),
    TwoPlayers(title = "T"),
}

@Composable
fun TicTacToeApp(viewModel: TicTacToeViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GameScreen.valueOf(
        backStackEntry?.destination?.route ?: GameScreen.Start.name
    )

    Scaffold(topBar = {}) {
        innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = GameScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = GameScreen.Start.name) {
                HomeScreen()
            }

            composable(route = GameScreen.TwoPlayers.name) {

            }
        }

    }

}