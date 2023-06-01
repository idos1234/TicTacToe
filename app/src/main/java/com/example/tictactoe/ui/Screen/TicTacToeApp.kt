package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import java.util.*
import kotlin.concurrent.schedule

enum class GameScreen(@SuppressLint("SupportAnnotationUsage") @StringRes val title: String) {
    Start(title = "TicTacToe"),
    TwoPlayers(title = "Two players"),
    SinglePlayer("SinglePlayer")
}

@Composable
fun TicTacToeApp(viewModel: TicTacToeViewModel = TicTacToeViewModel()) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GameScreen.valueOf(
        backStackEntry?.destination?.route ?: GameScreen.Start.name
    )

    var timesPlayed by remember {
        mutableStateOf(0)
    }

    Scaffold(topBar = {}) {
        innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = GameScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = GameScreen.Start.name) {
                HomeScreen(
                    onTwoPlayersClick = {navController.navigate(GameScreen.TwoPlayers.name)},
                    onSinglePlayerClick = {navController.navigate(GameScreen.SinglePlayer.name)})
            }

            composable(route = GameScreen.TwoPlayers.name) {
                TicTacToeScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onPlayAgain = {navController.navigate(GameScreen.TwoPlayers.name)})
            }

            composable(route = GameScreen.SinglePlayer.name) {
                TicTacToeSinglePlayerScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onPlayAgain = {
                        timesPlayed ++
                        resetGame(viewModel, navController, timesPlayed)
                        Timer().schedule(1000) {
                            if (timesPlayed % 2 == 1) {
                                viewModel.botTurn(uiState)
                            }
                        }
                    },
                )
            }
        }

    }

}

fun resetGame(
    viewModel: TicTacToeViewModel,
    navController: NavHostController,
    times: Int
) {
    val isBotTurn =
        times % 2 != 1
    viewModel.resetGame(isBotTurn)
    navController.popBackStack(GameScreen.SinglePlayer.name, inclusive = false)
}