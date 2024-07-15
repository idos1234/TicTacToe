package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.idos.tictactoe.R
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.GoogleSignIn.GoogleSignInScreen
import com.idos.tictactoe.ui.GoogleSignIn.GoogleSignInViewModel
import com.idos.tictactoe.ui.Screen.Game.Online.CheckExitGame
import com.idos.tictactoe.ui.Screen.Game.Online.CodeGameScreen
import com.idos.tictactoe.ui.Screen.Game.Online.CodeGameViewModel
import com.idos.tictactoe.ui.Screen.Game.Online.EnterGameOnline
import com.idos.tictactoe.ui.Screen.Game.Online.OnlineTicTacToe
import com.idos.tictactoe.ui.Screen.Game.Online.OpenGameOnline
import com.idos.tictactoe.ui.Screen.Game.Online.SearchGameScreen
import com.idos.tictactoe.ui.Screen.Game.Online.deleteGame
import com.idos.tictactoe.ui.Screen.Game.TicTacToeScreen
import com.idos.tictactoe.ui.Screen.Game.TicTacToeSinglePlayerScreen
import com.idos.tictactoe.ui.Screen.Game.TicTacToeViewModel
import com.idos.tictactoe.ui.Screen.Menu.CustomLinearProgressIndicator
import com.idos.tictactoe.ui.Screen.Menu.HomeScreen
import com.idos.tictactoe.ui.Screen.Menu.LeaderBoardScreen
import com.idos.tictactoe.ui.Screen.Menu.ProfileScreen
import com.idos.tictactoe.ui.Screen.Menu.getPlayer
import com.idos.tictactoe.ui.screens.Shop.SetNewDeals
import com.idos.tictactoe.ui.screens.Shop.ShopScreen
import com.idos.tictactoe.ui.theme.Secondery
import com.idos.tictactoe.ui.theme.Shapes
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
    SearchGame("SearchGame"),
    Store("Store")
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

@SuppressLint("UnrememberedMutableState")
@Composable
fun TopHomeScreenMenu(modifier: Modifier, context: Context, email: String, navController: NavHostController, onChangeScreen: () -> Unit) {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
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
fun TopAppBar(
    navController: NavController,
    profile: MainPlayerUiState
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val colors = MaterialTheme.colorScheme

    Row(
        Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(top = (screenHeight * 5 / 915).dp),
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(2f)) {
            CustomLinearProgressIndicator(
                progress = 0.7f,
                progressColor = colors.secondary,
                backgroundColor = Color.DarkGray,
                clipShape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((screenHeight * 20 / 915).dp)

            )
            Text(
                text = "Level: ${profile.level}",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .weight(2f)
                .clip(RoundedCornerShape(50)),
            contentAlignment = AbsoluteAlignment.CenterLeft
        ) {
            Box(
                modifier = Modifier
                    .height((screenHeight * 20 / 915).dp),
                contentAlignment = AbsoluteAlignment.CenterRight
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(colors.secondary),
                    shape = RoundedCornerShape(20)
                ) {
                    Text(
                        text = profile.coins.toString(),
                        textAlign = TextAlign.Center,
                        color = colors.onSecondary,
                        fontSize = 15.sp,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Card(
                    modifier = Modifier
                        .size((screenHeight * 20 / 915).dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(Color.Green.copy(alpha = 0.6f)),
                    onClick = { navController.navigate(GameScreen.Store.title) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = "coin",
                Modifier
                    .size((screenHeight * 20 / 915).dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun BackTopBar(
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.Left,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "back",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size((LocalConfiguration.current.screenWidthDp * 50 / 412).dp)
                .clickable(onClick = onClick)
        )
    }
}

@Composable
fun BottomAppBar(
    navController: NavController,
    leadBoardColor: Color,
    homeColor: Color,
    profileColor: Color,
    storeColor: Color
) {
    Row(horizontalArrangement = Arrangement.Absolute.Left) {
        Box(modifier = Modifier
            .weight(1f)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(
                onClick = {
                    navController.navigate(GameScreen.Store.title)
                }
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.store),
                contentDescription = "Store",
                tint = storeColor
            )
        }
        Box(modifier = Modifier
            .weight(1f)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(
                onClick = {
                    navController.navigate(GameScreen.LeaderBoard.title)
                }
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.leaderboard),
                contentDescription = "LeadBoard",
                tint = leadBoardColor
            )
        }
        Box(modifier = Modifier
            .weight(1f)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(
                onClick = {
                    navController.navigate(GameScreen.Start.title)
                }
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Home,
                contentDescription = "Home",
                tint = homeColor
            )
        }
        Box(modifier = Modifier
            .weight(1f)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(
                onClick = {
                    navController.navigate(GameScreen.ProfileScreen.title)
                }
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = "Account",
                tint = profileColor
            )
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

    // viewModel and uiState
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


    //menu bar
    val colors = MaterialTheme.colorScheme

    var leadBoardColor by remember {
        mutableStateOf( colors.onPrimary )
    }
    var homeColor by remember {
        mutableStateOf( colors.onPrimary )
    }
    var profileColor by remember {
        mutableStateOf( colors.onPrimary )
    }
    var storeColor by remember {
        mutableStateOf( colors.onPrimary )
    }

    val defaultBottomBar: @Composable () -> Unit = {
        BottomAppBar(
            navController,
            leadBoardColor,
            homeColor,
            profileColor,
            storeColor
        )
    }
    var bottomAppBar: @Composable () -> Unit by remember {
        mutableStateOf(defaultBottomBar)
    }
    var profile by remember {
        mutableStateOf(MainPlayerUiState())
    }
    profile = getPlayer(email = email.value, context = context)
    val defaultTopAppBar: @Composable () -> Unit = {
        TopAppBar(
            navController = navController,
            profile = profile
        )
    }
    var topAppBar: @Composable () -> Unit by remember {
        mutableStateOf(defaultTopAppBar)
    }
    var backClick by remember {
        mutableStateOf( false )
    }
    var onClick by remember {
        mutableStateOf({})
    }


    Scaffold(
        bottomBar = bottomAppBar,
        topBar = topAppBar
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
                bottomAppBar = defaultBottomBar
                topAppBar = defaultTopAppBar

                leadBoardColor = colors.onPrimary
                homeColor = colors.secondary
                profileColor = colors.onPrimary
                storeColor = colors.onPrimary

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
                bottomAppBar = {}
                topAppBar = { BackTopBar(onClick = {backClick = true}) }
                onClick = { navController.popBackStack() }

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
                bottomAppBar = {}
                topAppBar = { BackTopBar(onClick = {backClick = true}) }
                onClick = { navController.popBackStack() }

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
                bottomAppBar = {}
                topAppBar = { BackTopBar(onClick = {backClick = true}) }
                onClick = {
                    deleteGame(
                        context = context,
                        databaseReference = FirebaseDatabase.getInstance().getReference(it.arguments?.getString("dbName") ?: "")
                    )
                    onlineGameValuesUiState.game = OnlineGameUiState()
                    navController.navigate(GameScreen.Start.title)
                }

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
                bottomAppBar = defaultBottomBar
                topAppBar = defaultTopAppBar

                leadBoardColor = colors.secondary
                homeColor = colors.onPrimary
                profileColor = colors.onPrimary
                storeColor = colors.onPrimary

                BackHandler {}
                LeaderBoardScreen(context = LocalContext.current, yourPlayer = email.value)
            }

            //profile screen
            composable(route = GameScreen.ProfileScreen.title) {
                bottomAppBar = defaultBottomBar
                topAppBar = defaultTopAppBar

                leadBoardColor = colors.onPrimary
                homeColor = colors.onPrimary
                profileColor = colors.secondary
                storeColor = colors.onPrimary

                BackHandler {}
                ProfileScreen(player = email.value, context = LocalContext.current)
            }

            //codeGame
            composable(route = GameScreen.CodeGame.title) {
                bottomAppBar = {}
                topAppBar = { BackTopBar(onClick = {navController.popBackStack()}) }

                BackHandler {}
                CodeGameScreen(
                    navController = navController,
                    context = LocalContext.current
                )
            }

            //open game with code
            composable(route = GameScreen.OpenGameWithCode.title) {
                bottomAppBar = {}
                topAppBar = {}


                BackHandler {}
                OpenGameOnline(
                    context = LocalContext.current,
                    player = email.value,
                    navController = navController,
                )
            }

            //enter game with code
            composable(route = GameScreen.EnterGameWithCode.title) {
                bottomAppBar = {}
                topAppBar = {}

                BackHandler {}
                EnterGameOnline(
                    context = LocalContext.current,
                    player = email.value,
                    navController = navController,
                )
            }

            // google sign in
            composable(GameScreen.GoogleSignIn.title) {
                bottomAppBar = {}
                topAppBar = {}

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

                        SetNewDeals(context, email.value)
                    }
                )
            }

            // choose new name
            composable(
                route = GameScreen.NewName.title
            ) {
                bottomAppBar = {}
                topAppBar = {}

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

                        SetNewDeals(context, email.value)
                    }
                )
            }
            
            composable(GameScreen.TimeUp.title) {
                bottomAppBar = {}
                topAppBar = {}

                BackHandler {}
                TimeUp(navController = navController)
            }
            
            composable(GameScreen.SearchGame.title) {
                bottomAppBar = {}
                topAppBar = {}

                BackHandler {}
                SearchGameScreen(
                    navController = navController,
                    player = email.value,
                    currentGame = onlineGameValuesUiState,
                    context = context,
                    viewModel = codeGameViewModel
                )
            }

            composable(GameScreen.Store.title) {
                bottomAppBar = defaultBottomBar
                topAppBar = defaultTopAppBar

                BackHandler {}
                leadBoardColor = colors.onPrimary
                homeColor = colors.onPrimary
                profileColor = colors.onPrimary
                storeColor = colors.secondary

                ShopScreen(
                    playerName = email.value,
                    onBuy = {
                        //database
                        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

                        //get Players collection from database
                        db.collection("Players").get()
                            //on success
                            .addOnSuccessListener { queryDocumentSnapshots ->
                                //check if collection is empty
                                if (!queryDocumentSnapshots.isEmpty) {
                                    val list = queryDocumentSnapshots.documents
                                    try {
                                        profile = list.find {
                                            it.toObject(MainPlayerUiState::class.java)!!.email == email.value
                                        }?.toObject(MainPlayerUiState::class.java)!!
                                    } catch (_: Exception) {}
                                }
                            }
                    }
                )
            }

        }
        if(backClick) {
            CheckExitGame(
                onQuitClick = onClick,
                onCancelClick = { backClick = false }
            )
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