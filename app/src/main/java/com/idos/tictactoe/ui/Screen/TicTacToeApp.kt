package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
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
import com.idos.tictactoe.ui.AppViewModelProvider
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Secondery
import com.idos.tictactoe.ui.theme.Shapes
import com.google.firebase.firestore.FirebaseFirestore
import com.idos.tictactoe.ui.Screen.GoogleSignIn.ChooseName
import com.idos.tictactoe.ui.Screen.GoogleSignIn.GoogleEmail
import com.idos.tictactoe.ui.Screen.GoogleSignIn.GoogleSignInScreen
import com.idos.tictactoe.ui.Screen.GoogleSignIn.GoogleSignInViewModel
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

//game screens
enum class GameScreen(val title: String) {
    SignUp("SignUp"),
    LogIn("LogIn"),
    Start("Start"),
    AboutUs("AboutUs"),
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
    NewName("NewName")
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
    onClearClick: () -> Unit
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
                onClick = onClearClick
            ) {
                Text("Log out")
            }
        }
    )
}

@Composable
fun HomeScreenMenu(navController: NavHostController, modifier: Modifier, onChangeScreen: () -> Unit = {}, sharedPreferences: GoogleEmail, onLogOutClick: () -> Unit) {

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
            sharedPreferences = sharedPreferences,
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
fun TopHomeScreenMenu(modifier: Modifier, context: Context, sharedPreferences: GoogleEmail, navController: NavHostController, onChangeScreen: () -> Unit) {

    var player by remember {
        mutableStateOf(com.idos.tictactoe.data.MainPlayerUiState())
    }
    //get database
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    //get Players collection from database
    db.collection("Players").get()
        //on success
        .addOnSuccessListener { queryDocumentSnapshots ->
            //check if collection is empty
            if (!queryDocumentSnapshots.isEmpty) {
                val list = queryDocumentSnapshots.documents
                for (d in list) {
                    val p: com.idos.tictactoe.data.MainPlayerUiState? = d.toObject(com.idos.tictactoe.data.MainPlayerUiState::class.java)
                    //find player using database
                    if (p?.email == sharedPreferences.email){
                        player = p!!
                    }

                }
            }
        }
        //on failure
        .addOnFailureListener {
            Toast.makeText(
                context,
                "Fail to get the data.",
                Toast.LENGTH_SHORT
            ).show()
        }

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
                Image(painter = painterResource(id = player.currentImage), contentDescription = null, contentScale = ContentScale.Crop)
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
        Button(onClick = {
            navController.navigate(GameScreen.AboutUs.title)
            onChangeScreen()},
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "About Us")
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
                Icon(imageVector = Icons.Default.Logout, contentDescription = "Log out icon")
            }

        }
    }

    if (checkLogOut) {
        CheckLogOut(
            onCancelClick = {checkLogOut = false},
            onClearClick = {
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

@Composable
fun CheckExit(onQuitClick: () -> Unit, onCancelClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = {},
        text = { Text(text = "Are you sure you want to quit the game") },
        dismissButton = {
            Button(onClick = onQuitClick) {
                Text(text = "Quit")
            }
                        },
        confirmButton = {
            Button(onClick = onCancelClick) {
                Text(text = "Cancel")
            }
        }
    )
}
fun getSecuredSharedPreferences(context: Context, fileName: String): SharedPreferences {
    val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    return EncryptedSharedPreferences.create(context, fileName, masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState",
    "StateFlowValueCalledInComposition"
)
@Composable
fun TicTacToeApp(
    viewModel: TicTacToeViewModel = TicTacToeViewModel(),
    signUpViewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory),
    codeGameViewModel: CodeGameViewModel = viewModel(),
    googleSignInViewModel: GoogleSignInViewModel = viewModel(),
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
    val signupUiState = signUpViewModel.signUpName
    val onlineGameValuesUiState by codeGameViewModel.onlineGameValuesUiState.collectAsState()
    val sharedPreferencesUiState by mutableStateOf(sharedPreferences())
    val emailState = googleSignInViewModel.emailState
    val emailState2 = googleSignInViewModel.email.collectAsState()


    var timesPlayed by remember {
        mutableIntStateOf(0)
    }
    var open by remember {
        mutableStateOf(false)
    }
    var updateSharedPreferences by remember {
        mutableStateOf(false)
    }

    //share preferences
    val sharedPreferences = getSecuredSharedPreferences(context, "myPref1")

    val emailStr = sharedPreferences.getString("name", "")
    val googleEmail = sharedPreferences.getString("email", "")
    val messageSent = sharedPreferences.getBoolean("messageSent", false)
    val lastTimeSeen = sharedPreferences.getLong("lastTimeSeen", 0)


    signupUiState.name = emailStr!!
    emailState2.value.email = googleEmail
    sharedPreferencesUiState.messageSent = messageSent
    sharedPreferencesUiState.lastTimeSeen = lastTimeSeen

    if (!updateSharedPreferences) {
        sharedPreferencesUiState.messageSent = false
        sharedPreferencesUiState.lastTimeSeen = (System.currentTimeMillis()/1000)

        sharedPreferences.edit().putBoolean("messageSent", sharedPreferencesUiState.messageSent).apply()
        sharedPreferences.edit().putLong("lastTimeSeen", sharedPreferencesUiState.lastTimeSeen).apply()
        updateSharedPreferences = true
    }

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
            GameScreen.LeaderBoard ->
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
                    icon = Icons.Default.ArrowBack
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
            else ->{
                TopAppBar(
                onClick = {navController.navigateUp()},
                icon = Icons.Default.ArrowBack
            )}
            }
            if (open) {
                CheckExit(
                    onCancelClick = {open = false},
                    onQuitClick = {
                        navController.navigateUp()
                        open = false
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
                    ),
                navController = navController,
                onChangeScreen = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                sharedPreferences = emailState,
                onLogOutClick = {
                    val sharedPreferences = getSecuredSharedPreferences(context, "myPref1")
                    googleSignInViewModel.resetEmail()
                    sharedPreferences.edit().putString("email", emailState2.value.email).apply()
                }
            )
        }
    ) {

        val startedScreen = if (emailState2.value.email2 != "") {
            GameScreen.Start.title
        } else {
            GameScreen.GoogleSignIn.title
        }

        NavHost(
            navController = navController,
            startDestination = startedScreen,
        ){
            //sign up screen
            composable(route = GameScreen.SignUp.title) {
                SignUpScreen(
                    signUpViewModel,
                    context,
                    emailsharedPreferences = signupUiState,
                    onClick = {
                        val sharedPreferences = getSecuredSharedPreferences(context, "myPref1")

                        signUpViewModel.signUpName.name = signUpViewModel.signUpName.name2

                        sharedPreferences.edit().putString("name", signupUiState.name).apply()

                        navController.navigate(GameScreen.Start.title)
                    },
                    signUpViewModel = signUpViewModel,
                    onLogInClick = { navController.navigate(GameScreen.LogIn.title) }
                )
            }

            //log in screen
            composable(route = GameScreen.LogIn.title) {
                LogInScreen(
                    signUpViewModel,
                    context,
                    emailsharedPreferences = signupUiState,
                    onClick = {
                        val sharedPreferences = getSecuredSharedPreferences(context, "myPref1")

                        signUpViewModel.signUpName.name = signUpViewModel.signUpName.name2

                        sharedPreferences.edit().putString("name", signupUiState.name).apply()

                        navController.navigate(GameScreen.Start.title)
                    },
                    onSignUpClick = { navController.navigate(GameScreen.SignUp.title) }
                )
            }

            //home screen
            composable(route = GameScreen.Start.title) {
                HomeScreen(
                    onTwoPlayersClick = {navController.navigate(GameScreen.TwoPlayers.title)},
                    onSinglePlayerClick = {navController.navigate(GameScreen.SinglePlayer.title)},
                    onOnlineClick = {navController.navigate(GameScreen.Online.title)},
                    onBackClick = {
                        val sharedPreferences = getSecuredSharedPreferences(context, "myPref1")

                        sharedPreferencesUiState.lastTimeSeen = (System.currentTimeMillis()/1000)

                        sharedPreferences.edit().putLong("lastTimeSeen", sharedPreferencesUiState.lastTimeSeen).apply()
                    }
                )
            }

            //game screen
            composable(route = GameScreen.TwoPlayers.title) {
                TicTacToeScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onPlayAgain = {
                        timesPlayed ++
                        resetGame(
                            viewModel,
                            navController.popBackStack(GameScreen.TwoPlayers.title, inclusive = false),
                            timesPlayed
                        )
                    },
                    playerName = signupUiState.name
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
                            if ((timesPlayed % 2 == 1) && uiState.player_Turn == "O"){
                                viewModel.botTurn(uiState)
                            }
                        }
                    },
                    playerName = signupUiState.name
                )
            }

            //online game screen
            composable(route = GameScreen.Online.title) {
                OnlineTicTacToe(player = signupUiState.name, context = LocalContext.current, viewModel = codeGameViewModel, navController = navController)
            }

            //leaderboard screen
            composable(route = GameScreen.LeaderBoard.title) {
                LeaderBoardScreen(context = LocalContext.current, yourPlayer = signupUiState.name)
            }

            //profile screen
            composable(route = GameScreen.ProfileScreen.title) {
                ProfileScreen(player = signupUiState.name, context = LocalContext.current)
            }

            //codeGame
            composable(route = GameScreen.CodeGame.title) {
                codeGameScreen(codeGameViewModel = codeGameViewModel, codeGameUiState = onlineGameValuesUiState, navController = navController)
            }

            //open game with code
            composable(route = GameScreen.OpenGameWithCode.title) {
                OpenOnlineGameWithCode(context = LocalContext.current, player = signupUiState.name, viewModel = codeGameViewModel, navController = navController)
            }

            //enter game with code
            composable(route = GameScreen.EnterGameWithCode.title) {
                EnterOnlineGameWithCode(context = LocalContext.current, player = signupUiState.name, gameId = onlineGameValuesUiState.gameCode, viewModel = codeGameViewModel, navController = navController)
            }

            //show game final score
            composable(
                route = GameScreen.ShowGameFinalScore.title,
                exitTransition = {
                    slideOutVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
                },
                enterTransition = {
                    slideInVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
                }
            ) {
                ShowGameFinalScore(uiState = onlineGameValuesUiState, navController = navController)
            }

            // google sign in
            composable(GameScreen.GoogleSignIn.title) {
                GoogleSignInScreen(
                    viewModel = googleSignInViewModel,
                    onClick = {
                        val sharedPreferences = getSecuredSharedPreferences(context, "myPref1")

                        sharedPreferences.edit().putString("email", emailState.email).apply()
                        navController.navigate(GameScreen.Start.title)
                    },
                    navController = navController
                )
            }

            // choose new name
            composable(
                route = GameScreen.NewName.title
            ) {
                ChooseName(
                    onClick = {
                        val sharedPreferences = getSecuredSharedPreferences(context, "myPref1")

                        sharedPreferences.edit().putString("email", emailState.email).apply()
                        navController.navigate(GameScreen.Start.title)
                    },
                    viewModel = googleSignInViewModel,
                    context = LocalContext.current,
                    emailState = emailState

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