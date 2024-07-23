package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.R
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.GoogleSignIn.ChooseName
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
import com.idos.tictactoe.ui.screens.Shop.SetNewDeals
import com.idos.tictactoe.ui.screens.Shop.ShopScreen
import com.idos.tictactoe.ui.screens.Shop.refresh
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

@Composable
fun TopAppBar(
    navController: NavController,
    playerEmail: String
) {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Players")
    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            val list = snapshot.children
            player = try {
                list.find {
                    it.getValue(MainPlayerUiState::class.java)!!.email == playerEmail
                }?.getValue(MainPlayerUiState::class.java)!!
            } catch (e: Exception) {
                MainPlayerUiState()
            }

        }

        override fun onCancelled(error: DatabaseError) {}
    })




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
                    .height((screenHeight * 25 / 915).dp)

            )
            Text(
                text = "Level: ${player.level}",
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
                contentAlignment = AbsoluteAlignment.CenterRight,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((screenHeight * 25 / 915).dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(colors.secondary),
                    shape = RoundedCornerShape(20)
                ) {
                    Text(
                        text = player.coins.toString(),
                        textAlign = TextAlign.Center,
                        color = colors.onSecondary,
                        fontSize = 15.sp,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Card(
                    modifier = Modifier
                        .size((screenHeight * 25 / 915).dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(Color.Green.copy(alpha = 0.6f)),
                    onClick = {
                        refresh.value = true
                        navController.navigate(GameScreen.Store.title)
                    }
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
                    .size((screenHeight * 25 / 915).dp)
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
                    refresh.value = true
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
        "MyPref",
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
    val defaultTopAppBar: @Composable () -> Unit = {
        TopAppBar(
            navController = navController,
            playerEmail = email.value
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
                LeaderBoardScreen(yourPlayer = email.value)
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
                ProfileScreen(player = email.value)
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
                            "MyPref",
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
                            "MyPref",
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
                    playerName = email.value
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