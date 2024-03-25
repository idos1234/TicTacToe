package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.dataStore.SharedPreferencesDataStore
import com.idos.tictactoe.ui.Screen.GoogleSignIn.GoogleSignInScreen
import com.idos.tictactoe.ui.Screen.GoogleSignIn.GoogleSignInViewModel
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Secondery
import com.idos.tictactoe.ui.theme.Shapes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.concurrent.schedule


//game screens
enum class GameScreen(val title: String) {
    SignUp("SignUp"),
    LogIn("LogIn"),
    Start("Start"),
    TwoPlayers("TwoPlayers"),
    SinglePlayer("SinglePlayer"),
    Online("Online"),
    LeaderBoard("LeaderBoard"),
    ProfileScreen("ProfileScreen"),
    CodeGame("CodeGame"),
    OpenGameWithCode("OpenGameWithCode"),
    EnterGameWithCode("EnterGameWithCode"),
    ShowGameFinalScore("ShowGameFinalScore"),
    GoogleSignIn("GoogleSignIn"),
    NewName("NewName"),
    TimeUp("TimeUp")
}

data class sharedPreferences(
    var lastTimeSeen: Long = 0,
    var messageSent: Boolean = false,
    var messagingSendingTime: Long = 0
)

/**
 * Provides Navigation graph for the application.
 */

@Composable
fun CheckLogOut(
    onCancelClick: () -> Unit,
    onLogOut: () -> Unit
) {

    AlertDialog(
        title = { Text(text = "Are you sure you want to log out?")},
        backgroundColor = Secondery,
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
                onClick = onLogOut
            ) {
                Text("Log out")
            }
        }
    )
}

@Composable
fun HomeScreenMenu(navController: NavHostController, modifier: Modifier, onChangeScreen: () -> Unit = {}, email: String, onLogOutClick: () -> Unit) {

    Box(modifier = Modifier
        .background(Secondery)
        .fillMaxWidth()) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            modifier = modifier.padding(16.dp)
        )
    }

    Column(modifier = Modifier.background(Secondery)) {
        TopHomeScreenMenu(
            modifier = Modifier.weight(1f),
            context = LocalContext.current,
            email = email,
            navController = navController,
            onChangeScreen = onChangeScreen
        )
        ButtonHomeScreenMenu(
            modifier = Modifier.weight(5f),
            navController = navController,
            onChangeScreen = onChangeScreen,
            onLogOutClick = onLogOutClick
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TopHomeScreenMenu(modifier: Modifier, context: Context, email: String, navController: NavHostController, onChangeScreen: () -> Unit) {
    var player by remember {
        mutableStateOf(com.idos.tictactoe.data.MainPlayerUiState())
    }

    //get Players collection from database
    player = getPlayer(email, context)

    val currentImage = GetXO(player.currentImage)

    Box(modifier = modifier
        .clickable(
            onClick = {
                navController.navigate(GameScreen.ProfileScreen.title)
                onChangeScreen()
            }
        )
        .fillMaxWidth()
        .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(
                shape = RoundedCornerShape(125.dp),
                elevation = 10.dp,
                modifier = Modifier
                    .size(90.dp)
            ) {
                Image(painter = painterResource(id = currentImage), contentDescription = null, contentScale = ContentScale.Crop)
                }

            Spacer(modifier = Modifier.width(15.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Welcome ${player.name}",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Score: ${player.score}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500
                )
            }

        }
    }
}

@Composable
fun ButtonHomeScreenMenu(modifier: Modifier, navController: NavHostController, onChangeScreen: () -> Unit = {}, onLogOutClick: () -> Unit) {
    var checkLogOut by remember {
        mutableStateOf(false)
    }
    Spacer(modifier = Modifier.height(30.dp))

    Column(modifier = modifier) {

        Button(onClick = {
            navController.navigate(GameScreen.Start.title)
            onChangeScreen()},
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Home")
        }
        Button(onClick = {
            navController.navigate(GameScreen.LeaderBoard.title)
            onChangeScreen()},
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Leader Board")
        }
        Button(onClick = {
            navController.navigate(GameScreen.CodeGame.title)
            onChangeScreen()},
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Play with code")
        }
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            onClick = {checkLogOut = true},
            shape = Shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp, bottom = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Log out")
                Spacer(modifier = Modifier.width(10.dp))
                Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "Log out icon")
            }

        }
    }

    if (checkLogOut) {
        CheckLogOut(
            onCancelClick = {checkLogOut = false},
            onLogOut = {
                onLogOutClick()
                checkLogOut = false
                onChangeScreen()
            }
        )
    }
}

@Composable
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



@SuppressLint("UnrememberedMutableState", "UnusedMaterialScaffoldPaddingParameter",
    "CoroutineCreationDuringComposition"
)
@Composable
fun TicTacToeApp(
    viewModel: TicTacToeViewModel = TicTacToeViewModel(),
    codeGameViewModel: CodeGameViewModel = viewModel(),
    googleSignInViewModel: GoogleSignInViewModel = viewModel()
) {
    //nav controller
    val context = LocalContext.current
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GameScreen.valueOf(
        backStackEntry?.destination?.route ?: GameScreen.Start.title
    )

    // viewModel and uiState
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val onlineGameValuesUiState by codeGameViewModel.onlineGameValuesUiState.collectAsState()
    var sharedPreferencesUiState by mutableStateOf(sharedPreferences())
    val emailState = googleSignInViewModel.emailState
    val enableState by codeGameViewModel.enableState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    var timesPlayed by remember {
        mutableIntStateOf(0)
    }
    var open by remember {
        mutableStateOf(false)
    }
    var updateSharedPreferences by remember {
        mutableStateOf(false)
    }

    //encrypted shared preferences

    val email = remember {
        mutableStateOf("")
    }
    val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        "preferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    val emailStr = encryptedSharedPreferences.getString("email", "")
    email.value = emailStr!!


    //share preferences
    val sharedPreferences = SharedPreferencesDataStore(context)

    if (!updateSharedPreferences) {
        sharedPreferencesUiState.messageSent = false
        sharedPreferencesUiState.lastTimeSeen = (System.currentTimeMillis() / 1000)

        sharedPreferences.setMessageSent(sharedPreferencesUiState.messageSent)
        sharedPreferences.setLastTimeSeen(sharedPreferencesUiState.lastTimeSeen)

        coroutineScope.launch {
            sharedPreferences.getDetails().collect {
                withContext(Dispatchers.IO) {
                    sharedPreferencesUiState = it
                }
            }
        }
        updateSharedPreferences = true
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            when (currentScreen) {
                GameScreen.Start ->
                    TopAppBar(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },
                        icon = Icons.Default.Menu
                    )

                GameScreen.LeaderBoard ->
                    TopAppBar(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },
                        icon = Icons.Default.Menu
                    )

                GameScreen.CodeGame ->
                    TopAppBar(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },
                        icon = Icons.Default.Menu
                    )

                GameScreen.SignUp ->
                    TopAppBar(
                        icon = null,
                        onClick = {},
                        isBar = false
                    )

                GameScreen.LogIn ->
                    TopAppBar(
                        icon = null,
                        onClick = {},
                        isBar = false
                    )

                GameScreen.Online ->
                    TopAppBar(
                        onClick = {
                            open = true
                        },
                        icon = Icons.AutoMirrored.Filled.ArrowBack
                    )

                GameScreen.EnterGameWithCode ->
                    TopAppBar(
                        onClick = {
                            open = true
                        },
                        icon = Icons.AutoMirrored.Filled.ArrowBack
                    )

                GameScreen.OpenGameWithCode ->
                    TopAppBar(
                        onClick = {
                            open = true
                        },
                        icon = Icons.AutoMirrored.Filled.ArrowBack
                    )

                GameScreen.ShowGameFinalScore ->
                    TopAppBar(
                        icon = null,
                        onClick = {},
                        isBar = false
                    )

                GameScreen.GoogleSignIn ->
                    TopAppBar(
                        icon = null,
                        onClick = {},
                        isBar = false
                    )

                else -> {
                    TopAppBar(
                        onClick = { navController.navigateUp() },
                        icon = Icons.AutoMirrored.Filled.ArrowBack
                    )
                }
            }
            if (open) {
                CheckExitOnlineGame(
                    onCancelClick = { open = false },
                    onQuitClick = {
                        codeGameViewModel.clearCode()
                        open = false
                                  },
                    navController = navController
                )
            }
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            HomeScreenMenu(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    ),
                navController = navController,
                onChangeScreen = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                email = email.value,
                onLogOutClick = {
                    email.value = ""

                    val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                        .build()

                    val encryptedSharedPreferences = EncryptedSharedPreferences.create(
                        context,
                        "preferences",
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )

                    encryptedSharedPreferences.edit().putString("email", email.value).apply()
                }
            )
        }
    ) {

        val startDestination = if (email.value == "") {
            GameScreen.GoogleSignIn.title
        } else {
            GameScreen.Start.title
        }

        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            //home screen
            composable(route = GameScreen.Start.title) {
                HomeScreen(
                    onTwoPlayersClick = { navController.navigate(GameScreen.TwoPlayers.title) },
                    onSinglePlayerClick = { navController.navigate(GameScreen.SinglePlayer.title) },
                    onOnlineClick = { navController.navigate(GameScreen.Online.title) },
                    onBackClick = {
                        sharedPreferencesUiState.lastTimeSeen = (System.currentTimeMillis() / 1000)

                        sharedPreferences.setLastTimeSeen(sharedPreferencesUiState.lastTimeSeen)
                    },
                    context = LocalContext.current
                )
            }

            //game screen
            composable(route = GameScreen.TwoPlayers.title) {
                TicTacToeScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onPlayAgain = {
                        timesPlayed++
                        resetGame(
                            viewModel,
                            navController.popBackStack(
                                GameScreen.TwoPlayers.title,
                                inclusive = false
                            ),
                            timesPlayed
                        )
                    },
                    playerName = email.value,
                    navController = navController
                )
            }

            //single player game screen
            composable(route = GameScreen.SinglePlayer.title) {
                TicTacToeSinglePlayerScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onPlayAgain = {
                        timesPlayed++
                        resetGame(
                            viewModel,
                            navController.popBackStack(
                                GameScreen.SinglePlayer.title,
                                inclusive = false,
                            ),
                            timesPlayed
                        )
                        Timer().schedule(1000) {
                            if ((timesPlayed % 2 == 1) && uiState.player_Turn == "O") {
                                viewModel.botTurn(uiState)
                            }
                        }
                    },
                    playerName = email.value,
                    navController = navController
                )
            }

            //online game screen
            composable(route = GameScreen.Online.title) {
                OnlineTicTacToe(
                    player = email.value,
                    context = LocalContext.current,
                    viewModel = codeGameViewModel,
                    navController = navController,
                    enableState = enableState
                )
            }

            //leaderboard screen
            composable(route = GameScreen.LeaderBoard.title) {
                LeaderBoardScreen(context = LocalContext.current, yourPlayer = email.value)
            }

            //profile screen
            composable(route = GameScreen.ProfileScreen.title) {
                ProfileScreen(player = email.value, context = LocalContext.current)
            }

            //codeGame
            composable(route = GameScreen.CodeGame.title) {
                codeGameScreen(
                    codeGameViewModel = codeGameViewModel,
                    codeGameUiState = onlineGameValuesUiState,
                    navController = navController,
                    context = LocalContext.current
                )
            }

            //open game with code
            composable(route = GameScreen.OpenGameWithCode.title) {
                OpenOnlineGameWithCode(
                    context = LocalContext.current,
                    player = email.value,
                    viewModel = codeGameViewModel,
                    navController = navController,
                    enableState = enableState,
                    codeGameViewModel = codeGameViewModel
                )
            }

            //enter game with code
            composable(route = GameScreen.EnterGameWithCode.title) {
                EnterOnlineGameWithCode(
                    context = LocalContext.current,
                    player = email.value,
                    gameId = onlineGameValuesUiState.gameCode,
                    viewModel = codeGameViewModel,
                    navController = navController,
                    enableState = enableState
                )
            }

            //show game final score
            composable(
                route = GameScreen.ShowGameFinalScore.title,
                exitTransition = {
                    slideOutVertically(animationSpec = tween(300)) + fadeOut(
                        animationSpec = tween(
                            300
                        )
                    )
                },
                enterTransition = {
                    slideInVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
                }
            ) {
                ShowGameFinalScore(uiState = onlineGameValuesUiState, navController = navController, codeGameViewModel = codeGameViewModel)
            }

            // google sign in
            composable(GameScreen.GoogleSignIn.title) {
                GoogleSignInScreen(
                    viewModel = googleSignInViewModel,
                    changeEmail = { email.value = it },
                    navController = navController,
                    onClick = {
                        val masterKey =
                            MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                                .build()

                        val encryptedSharedPreferences = EncryptedSharedPreferences.create(
                            context,
                            "preferences",
                            masterKey,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        )

                        encryptedSharedPreferences.edit().putString("email", email.value).apply()
                    }
                )
            }

            // choose new name
            composable(
                route = GameScreen.NewName.title
            ) {
                ChooseName(
                    changeEmail = { email.value = it },
                    viewModel = googleSignInViewModel,
                    context = LocalContext.current,
                    emailState = emailState,
                    onClick = {
                        val masterKey =
                            MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                                .build()

                        val encryptedSharedPreferences = EncryptedSharedPreferences.create(
                            context,
                            "preferences",
                            masterKey,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        )

                        encryptedSharedPreferences.edit().putString("email", email.value).apply()
                    }
                )
            }
            
            composable(GameScreen.TimeUp.title) {
                TimeUp(navController = navController)
            }

        }
    }

}

fun resetGame(
    viewModel: TicTacToeViewModel,
    navController: Boolean,
    times: Int,
) {
    viewModel.resetGame(times)
    navController
}