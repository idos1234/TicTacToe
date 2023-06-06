package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.ui.theme.BackGround
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

enum class GameScreen(@SuppressLint("SupportAnnotationUsage") @StringRes val title: String) {
    Start(title = "Home"),
    Settings(title = "Settings"),
    AboutUs(title = "About Us"),
    TwoPlayers(title = "Two players"),
    SinglePlayer("SinglePlayer")
}

@Composable()
fun HomeScreenMenu(modifier: Modifier) {
    Spacer(modifier = Modifier.height(30.dp))

    Column() {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            modifier = modifier)

        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Home")
        }
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Settings")
        }
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(text = "About Us")
        }
    }
}

@Composable()
fun topHomeScreenBar(onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(45.dp)
        .background(BackGround), verticalAlignment = Alignment.CenterVertically) {
        Image(
            imageVector = Icons.Default.Menu,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .size(30.dp)
                .clickable(
                    onClick = onClick
                ))
        Spacer(modifier = Modifier.weight(3f))
    }
}

@Composable
fun TicTacToeApp(viewModel: TicTacToeViewModel = TicTacToeViewModel()) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GameScreen.valueOf(
        backStackEntry?.destination?.route ?: GameScreen.Start.name
    )

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var timesPlayed by remember {
        mutableStateOf(0)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
        when(currentScreen) {
            GameScreen.Start ->
                topHomeScreenBar(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
            GameScreen.Settings ->
                topHomeScreenBar(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                )
            GameScreen.AboutUs ->
                topHomeScreenBar(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                )
        }
    },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            HomeScreenMenu(
                modifier = Modifier
                    .clickable (
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    )
            )
        }
    ) {
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