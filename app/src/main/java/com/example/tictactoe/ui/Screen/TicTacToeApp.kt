@file:Suppress("DEPRECATION", "NAME_SHADOWING", "UNUSED_EXPRESSION")

package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
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
import androidx.security.crypto.MasterKeys
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.ui.AppViewModelProvider
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Secondery
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule




enum class GameScreen(@SuppressLint("SupportAnnotationUsage") @StringRes val title: String) {
    SignUp(title = "Sign Up"),
    LogIn(title = "Log In"),
    Start(title = "Home"),
    Settings(title = "Settings"),
    AboutUs(title = "About Us"),
    TwoPlayers(title = "Two players"),
    SinglePlayer(title = "Single Player")
}

/**
 * Provides Navigation graph for the application.
 */

@Composable
fun HomeScreenMenu(navController: NavHostController, modifier: Modifier, onChaneScreen: () -> Unit = {}, sharedPreferences: sharedPreferences) {

    Box(modifier = Modifier.background(Secondery).fillMaxWidth()) {
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
            sharedPreferences = sharedPreferences
        )
        ButtonHomeScreenMenu(
            modifier = Modifier.weight(5f),
            navController = navController,
            onChaneScreen = onChaneScreen
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TopHomeScreenMenu(modifier: Modifier, context: Context, sharedPreferences: sharedPreferences) {

    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    db.collection("Players").get()
        .addOnSuccessListener { queryDocumentSnapshots ->
            if (!queryDocumentSnapshots.isEmpty) {
                val list = queryDocumentSnapshots.documents
                for (d in list) {
                    val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
                    if (p?.name == sharedPreferences.name){
                        player = p
                    }

                }
            }
        }
        .addOnFailureListener {
            Toast.makeText(
                context,
                "Fail to get the data.",
                Toast.LENGTH_SHORT
            ).show()
        }

    Box(modifier = modifier
        .clickable(onClick = {})
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
                Image(imageVector = Icons.Default.TagFaces, contentDescription = null, contentScale = ContentScale.FillBounds)
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


    var timesPlayed by remember {
        mutableStateOf(0)
    }

    //share preferences
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    val sharedPreferences = EncryptedSharedPreferences.create(
        "preferences",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    val emailStr = sharedPreferences.getString("name", "")

    signupUiState.name = emailStr!!

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
                },
                sharedPreferences = signupUiState
            )
        }
    ) {
        innerPadding ->

        val startedDestination = if(signupUiState.name == "") {
            GameScreen.LogIn.name
        } else {
            GameScreen.Start.name
        }

        NavHost(
            navController = navController,
            startDestination = startedDestination,
            modifier = Modifier.padding(innerPadding)
        ){

            composable(route = GameScreen.SignUp.name) {
                SignUpScreen(
                    signUpViewModel,
                    context,
                    emailsharedPreferences = signupUiState,
                    onClick = {
                        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

                        val sharedPreferences = EncryptedSharedPreferences.create(
                            "preferences",
                            masterKeyAlias,
                            context,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        )
                        signUpViewModel.emailsharedPreferences.name = signUpViewModel.emailsharedPreferences.name2

                        sharedPreferences.edit().putString("name", signupUiState.name).apply()

                        navController.navigate(GameScreen.Start.name)
                    },
                    signUpViewModel = signUpViewModel,
                    onLogInClick = { navController.navigate(GameScreen.LogIn.name) }
                )
            }

            composable(route = GameScreen.LogIn.name) {
                LogInScreen(
                    signUpViewModel,
                    context,
                    emailsharedPreferences = signupUiState,
                    onClick = {
                        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

                        val sharedPreferences = EncryptedSharedPreferences.create(
                            "preferences",
                            masterKeyAlias,
                            context,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        )
                        signUpViewModel.emailsharedPreferences.name = signUpViewModel.emailsharedPreferences.name2

                        sharedPreferences.edit().putString("name", signupUiState.name).apply()

                        navController.navigate(GameScreen.Start.name)
                    },
                    onSignInClick = { navController.navigate(GameScreen.SignUp.name) }
                )
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
                    }
                )
            }

            composable(route= GameScreen.Settings.name) {
                SettingScreen(
                    onClearClick = {
                        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

                        val sharedPreferences = EncryptedSharedPreferences.create(
                            "preferences",
                            masterKeyAlias,
                            context,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        )
                        signUpViewModel.emailsharedPreferences.name = ""

                        sharedPreferences.edit().putString("name", signupUiState.name).apply()

                        signUpViewModel.clearPlayer()

                         navController.navigate(GameScreen.LogIn.name)

                    },
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