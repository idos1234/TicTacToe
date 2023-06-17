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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.ui.AppViewModelProvider
import com.example.tictactoe.ui.theme.BackGround
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

enum class GameScreen(@SuppressLint("SupportAnnotationUsage") @StringRes val title: String) {
    SignUp(title = "Sign Up"),
    Start(title = "Home"),
    Settings(title = "Settings"),
    AboutUs(title = "About Us"),
    TwoPlayers(title = "Two players"),
    SinglePlayer("SinglePlayer")
}

/**
 * Provides Navigation graph for the application.
 */

@Composable()
fun HomeScreenMenu(modifier: Modifier, navController: NavHostController, onChaneScreen: () -> Unit = {}) {
    Spacer(modifier = Modifier.height(30.dp))

    Column() {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            modifier = modifier)

        Button(onClick = {
            navController.navigate(GameScreen.Start.name)
            onChaneScreen()},
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Home")
        }
        Button(onClick = {
            navController.navigate(GameScreen.Settings.name)
            onChaneScreen()},
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Settings")
        }
        Button(onClick = {
            navController.navigate(GameScreen.AboutUs.name)
            onChaneScreen()},
            modifier = Modifier.fillMaxWidth()) {
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
fun TicTacToeApp(
    viewModel: TicTacToeViewModel = TicTacToeViewModel(),
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GameScreen.valueOf(
        backStackEntry?.destination?.route ?: GameScreen.Start.name
    )

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val settingsUiState by settingsViewModel.settingsUiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()


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
            else -> {}
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
                    ),
                navController = navController,
                onChaneScreen = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        }
    ) {
        innerPadding ->

        val startedDestination = if(settingsUiState.playerList.isNotEmpty()) {
            GameScreen.Start.name
        } else {
            GameScreen.SignUp.name
        }

        NavHost(
            navController = navController,
            startDestination =  startedDestination,
            modifier = Modifier.padding(innerPadding)
        ){

            composable(route = GameScreen.SignUp.name) {
                SignUpScreen()
            }

            composable(route = GameScreen.Start.name) {
                HomeScreen(
                    onTwoPlayersClick = {navController.navigate(GameScreen.TwoPlayers.name)},
                    onSinglePlayerClick = {navController.navigate(GameScreen.SinglePlayer.name)})
            }

            composable(route = GameScreen.TwoPlayers.name) {
                TicTacToeScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onPlayAgain = {
                        timesPlayed ++
                        resetGame(
                            viewModel,
                            navController.popBackStack(GameScreen.TwoPlayers.name, inclusive = false),
                            timesPlayed,
                            GameScreen.TwoPlayers,
                        )
                    }
                )
            }

            composable(route = GameScreen.SinglePlayer.name) {
                TicTacToeSinglePlayerScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onPlayAgain = {
                        timesPlayed ++
                        resetGame(viewModel,
                            navController.popBackStack(GameScreen.SinglePlayer.name, inclusive = false),
                            timesPlayed,
                            GameScreen.SinglePlayer)
                        Timer().schedule(1000) {
                            if (timesPlayed % 2 == 1) {
                                viewModel.botTurn(uiState)
                            }
                        }
                    }
                )
            }

            composable(route= GameScreen.Settings.name) {
                SettingScreen()
            }
        }

    }

}

fun resetGame(
    viewModel: TicTacToeViewModel,
    navController: Boolean,
    times: Int,
    screen: GameScreen,
) {
    if (screen == GameScreen.SinglePlayer) {
        val isBotTurn =
            times % 2 != 1
        viewModel.resetGame(isBotTurn, times)
    } else {
        viewModel.resetGame(times = times)
    }
    navController
}