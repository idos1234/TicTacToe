package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.data.ModelPreferencesManager
import com.example.tictactoe.data.Player
import com.example.tictactoe.ui.AppViewModelProvider
import com.example.tictactoe.ui.theme.BackGround
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

enum class GameScreen(@SuppressLint("SupportAnnotationUsage") @StringRes val title: String) {
    SignUp(title = "Sign Up"),
    Start(title = "Home"),
    ChooseSinglePlayer(title = "Choose Player"),
    ChooseTwoPlayers(title = "Choose players"),
    Settings(title = "Settings"),
    AboutUs(title = "About Us"),
    TwoPlayers(title = "Two players"),
    SinglePlayer(title = "Single Player")
}

/**
 * Provides Navigation graph for the application.
 */

@Composable
fun HomeScreenMenu(navController: NavHostController, modifier: Modifier, onChaneScreen: () -> Unit = {}) {

    val player = ModelPreferencesManager.get<MainPlayerUiState>("Player")

    Icon(
        imageVector = Icons.Default.Close,
        contentDescription = null,
        modifier = modifier.padding(16.dp)
    )

    Column {
        TopHomeScreenMenu(modifier = Modifier.weight(1f), player = player!!)
        ButtonHomeScreenMenu(modifier = Modifier.weight(5f), navController = navController, onChaneScreen = onChaneScreen)
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TopHomeScreenMenu(modifier: Modifier, player: MainPlayerUiState) {

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        if (player.image == null) {
            Image(imageVector = Icons.Default.SupervisedUserCircle, contentDescription = null, contentScale = ContentScale.FillBounds)
        } else {
            AsyncImage(
                model = player.image,
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Text(text = player.name, fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ButtonHomeScreenMenu(modifier: Modifier, navController: NavHostController, onChaneScreen: () -> Unit = {}) {
    Spacer(modifier = Modifier.height(30.dp))

    Column(modifier = modifier) {

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
fun TopAppBar(onClick: () -> Unit, icon: ImageVector?, isBar: Boolean = true) {
    if (isBar) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .background(BackGround), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = icon!!,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .size(30.dp)
                    .clickable(
                        onClick = onClick
                    )
            )
            Spacer(modifier = Modifier.weight(3f))
        }
    }
}
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun TicTacToeApp(
    viewModel: TicTacToeViewModel = TicTacToeViewModel(),
    signUpViewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    //nav controller
    val context = LocalContext.current
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GameScreen.valueOf(
        backStackEntry?.destination?.route ?: GameScreen.Start.name
    )

    // viewModel and uiState
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val signupUiState = signUpViewModel.emailsharedPreferences
    val playerUiState = signUpViewModel.playerUiState


    var timesPlayed by remember {
        mutableStateOf(0)
    }

    //game players
    var player1 by remember {
        mutableStateOf(Player())
    }
    var player2 by remember {
        mutableStateOf(Player())
    }

    //share preferences
    var player by remember {
        mutableStateOf(playerUiState)
    }
    ModelPreferencesManager.with(application = Application())

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
        when(currentScreen) {
            GameScreen.Start ->
                TopAppBar(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    icon = Icons.Default.Menu
                )
            GameScreen.Settings ->
                TopAppBar(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    icon = Icons.Default.Menu
                )
            GameScreen.AboutUs ->
                TopAppBar(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    icon = Icons.Default.Menu
                )
            else -> {
                if (currentScreen == GameScreen.SignUp) {
                    TopAppBar(
                        icon = null,
                        onClick = {},
                        isBar = false
                    )
                } else {
                    TopAppBar(
                        onClick = {navController.navigateUp()},
                        icon = Icons.Default.ArrowBack)
                }
            }
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

        val startedDestination = if(player == null) {
            GameScreen.SignUp.name
        } else {
            GameScreen.Start.name
        }

        NavHost(
            navController = navController,
            startDestination = GameScreen.SignUp.name,
            modifier = Modifier.padding(innerPadding)
        ){

            composable(route = GameScreen.SignUp.name) {
                SignUpScreen(
                    signUpViewModel,
                    context,
                    emailsharedPreferences = signupUiState,
                    onClick = {
                        player = playerUiState
                        ModelPreferencesManager.put(player, "Player")
                    }
                )
            }

            composable(route = GameScreen.Start.name) {
                HomeScreen(
                    onTwoPlayersClick = {navController.navigate(GameScreen.ChooseTwoPlayers.name)},
                    onSinglePlayerClick = {navController.navigate(GameScreen.ChooseSinglePlayer.name)})
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
                    },
                    player1 = player1,
                    player2 = player2,
                    signUpViewModel = signUpViewModel,
                    onWinner = {
                        player1.score = player1.score + 1
                    }
                )
            }

            composable(route = GameScreen.SinglePlayer.name) {
                TicTacToeSinglePlayerScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onPlayAgain = {
                        timesPlayed++
                        resetGame(
                            viewModel,
                            navController.popBackStack(
                                GameScreen.SinglePlayer.name,
                                inclusive = false,
                            ),
                            timesPlayed,
                            GameScreen.SinglePlayer,
                        )
                        Timer().schedule(1000) {
                            if (timesPlayed % 2 == 1) {
                                viewModel.botTurn(uiState)
                            }
                        }
                    },
                    player1 = player1,
                    player2 = player2,
                    signUpViewModel = signUpViewModel,
                    onWinner = {
                        player1.score = player1.score + 1
                    }
                )
            }

            composable(route= GameScreen.Settings.name) {
                SettingScreen(
                    onClearClick = {
                        val player = null
                        ModelPreferencesManager.put(player, "Player")
                    },
                )
            }

            composable(route= GameScreen.ChooseSinglePlayer.name) {
                chooseSinglePlayerScreen(
                    viewModel = viewModel,
                    onReadyClicked = {navController.navigate(GameScreen.SinglePlayer.name)},
                    uiState = uiState,
                    onPlayerClick = {
                        player1 = uiState.player1
                        player2 = uiState.player2
                    }
                )
            }

            composable(route= GameScreen.ChooseTwoPlayers.name) {
                chooseTwoPlayersScreen(
                    viewModel = viewModel,
                    onReadyClicked = { navController.navigate(GameScreen.TwoPlayers.name) },
                    uiState = uiState,
                    onPlayerClick = {
                        player1 = uiState.player1
                        player2 = uiState.player2
                    }
                )
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