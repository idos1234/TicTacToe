package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.ui.GoogleSignIn.GoogleSignInScreen
import com.idos.tictactoe.ui.GoogleSignIn.GoogleSignInViewModel
import com.idos.tictactoe.ui.Online.CodeGameViewModel
import com.idos.tictactoe.ui.Online.EnterGameOnline
import com.idos.tictactoe.ui.Online.OnlineTicTacToe
import com.idos.tictactoe.ui.Online.OpenGameOnline
import com.idos.tictactoe.ui.Online.SearchGameScreen
import com.idos.tictactoe.ui.Online.CodeGameScreen
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Secondery
import com.idos.tictactoe.ui.theme.Shapes
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule


//game screens
enum class GameScreen(val title: String) {
    Start("Start"),
    TwoPlayers("TwoPlayers"),
    SinglePlayer("SinglePlayer"),
    Online("Online"),
    LeaderBoard("LeaderBoard"),
    ProfileScreen("ProfileScreen"),
    CodeGame("CodeGame"),
    OpenGameWithCode("OpenGameWithCode"),
    EnterGameWithCode("EnterGameWithCode"),
    GoogleSignIn("GoogleSignIn"),
    NewName("NewName"),
    TimeUp("TimeUp"),
    SearchGame("SearchGame")
}

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
        containerColor = Secondery,
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
                elevation = CardDefaults.cardElevation(10.dp),
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

    // viewModel and uiState
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val onlineGameValuesUiState by codeGameViewModel.onlineGameValuesUiState.collectAsState()
    val emailState = googleSignInViewModel.emailState
    val enableState by codeGameViewModel.enableState.collectAsState()


    var timesPlayed by remember {
        mutableIntStateOf(0)
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

    Scaffold(
        scaffoldState = scaffoldState,
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

                    encryptedSharedPreferences.edit().putString("email", "").apply()
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
                    onFriendlyBattleClick = {navController.navigate(GameScreen.CodeGame.title)},
                    onOnlineClick = { navController.navigate(GameScreen.SearchGame.title) },
                    context = LocalContext.current
                )
            }

            //game screen
            composable(route = GameScreen.TwoPlayers.title) {
                BackHandler {}
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
                BackHandler {}
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
            composable(
                route = "${GameScreen.Online.title}/{dbName}",
                arguments = listOf(
                    navArgument("dbName") {
                        type = NavType.StringType
                    }
                )
            ) {
                BackHandler {}
                OnlineTicTacToe(
                    player = email.value,
                    viewModel = codeGameViewModel,
                    navController = navController,
                    enableState = enableState,
                    currentGame = onlineGameValuesUiState,
                    dbName = it.arguments?.getString("dbName") ?: ""
                )
            }

            //leaderboard screen
            composable(route = GameScreen.LeaderBoard.title) {
                BackHandler {}
                LeaderBoardScreen(context = LocalContext.current, yourPlayer = email.value)
            }

            //profile screen
            composable(route = GameScreen.ProfileScreen.title) {
                BackHandler {}
                ProfileScreen(player = email.value, context = LocalContext.current)
            }

            //codeGame
            composable(route = GameScreen.CodeGame.title) {
                BackHandler {}
                CodeGameScreen(
                    navController = navController,
                    context = LocalContext.current
                )
            }

            //open game with code
            composable(route = GameScreen.OpenGameWithCode.title) {
                BackHandler {}
                OpenGameOnline(
                    context = LocalContext.current,
                    player = email.value,
                    navController = navController,
                )
            }

            //enter game with code
            composable(route = GameScreen.EnterGameWithCode.title) {
                BackHandler {}
                EnterGameOnline(
                    context = LocalContext.current,
                    player = email.value,
                    navController = navController,
                )
            }

            // google sign in
            composable(GameScreen.GoogleSignIn.title) {
                BackHandler {}
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
                BackHandler {}
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
                BackHandler {}
                TimeUp(navController = navController)
            }
            
            composable(GameScreen.SearchGame.title) {
                BackHandler {}
                SearchGameScreen(
                    navController = navController,
                    player = email.value,
                    currentGame = onlineGameValuesUiState,
                    context = context,
                    viewModel = codeGameViewModel
                )
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