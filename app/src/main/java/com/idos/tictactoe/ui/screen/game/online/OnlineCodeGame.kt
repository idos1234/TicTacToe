package com.idos.tictactoe.ui.screen.game.online

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.data.CountDownTimerRead
import com.idos.tictactoe.data.CountDownTimerWrite
import com.idos.tictactoe.data.Draw
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.screen.GameScreen
import com.idos.tictactoe.ui.screen.menu.getPlayer

private var enteredGame = false

class CodeGameService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {}

    override fun onTaskRemoved(rootIntent: Intent?) {
        //get database
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("GamesWithCode")

        deleteGame(this, databaseReference)
    }
}

@Composable
fun PlayersBar(
    modifier: Modifier,
    screenWidth: Int,
    colors: ColorScheme,
    gameState: OnlineGameRememberedValues,
    databaseReference: DatabaseReference
) {
    gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(MyTurn == "X") {
                gameState.player1.CountDownTimerWrite(
                    playerNumber = 1,
                    gameState = gameState,
                    databaseReference = databaseReference,
                    modifier = Modifier.size(((screenWidth - 40) / 4).dp),
                    screenWidth = screenWidth,
                    screenHeight = LocalConfiguration.current.screenHeightDp,
                    shape = RoundedCornerShape((screenWidth * 20 / 400).dp),
                    border = BorderStroke(2.dp, if (gameState.game.playerTurn == "X") colors.tertiary else { colors.background})
                )
            } else {
                gameState.player1.CountDownTimerRead(
                    playerNumber = 1,
                    gameState = gameState,
                    databaseReference = databaseReference,
                    modifier = Modifier.size(((screenWidth - 40) / 4).dp),
                    screenWidth = screenWidth,
                    screenHeight = LocalConfiguration.current.screenHeightDp,
                    shape = RoundedCornerShape((screenWidth * 20 / 400).dp),
                    border = BorderStroke(2.dp, if (gameState.game.playerTurn == "X") colors.tertiary else { colors.background})
                )
            }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = colors.primary),
            modifier = Modifier
                .height(((screenWidth - 40) / 3 / 3).dp)
                .weight(1f)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "${gameState.game.player1Score} : ${gameState.game.player2Score}",
                    textAlign = TextAlign.Left,
                    fontSize = screenWidth.sp * 0.04,
                    color = colors.onPrimary
                )
            }
        }
        Column(
            Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(MyTurn == "O") {
                gameState.player2.CountDownTimerWrite(
                    playerNumber = 2,
                    gameState = gameState,
                    databaseReference = databaseReference,
                    modifier = Modifier.size(((screenWidth - 40) / 4).dp),
                    screenWidth = screenWidth,
                    screenHeight = LocalConfiguration.current.screenHeightDp,
                    shape = RoundedCornerShape((screenWidth * 20 / 400).dp),
                    border = BorderStroke(2.dp, if (gameState.game.playerTurn == "O") colors.tertiary else { colors.background})
                )
            } else {
                gameState.player2.CountDownTimerRead(
                    playerNumber = 2,
                    gameState = gameState,
                    databaseReference = databaseReference,
                    modifier = Modifier.size(((screenWidth - 40) / 4).dp),
                    screenWidth = screenWidth,
                    screenHeight = LocalConfiguration.current.screenHeightDp,
                    shape = RoundedCornerShape((screenWidth * 20 / 400).dp),
                    border = BorderStroke(2.dp, if (gameState.game.playerTurn == "O") colors.tertiary else { colors.background})
                )
            }
        }
    }
}

@Composable
fun EnterGameOnline(
    context: Context,
    player: String,
    navController: NavController,
    enable: Enable
) {
    val currentGame by remember {
        mutableStateOf(OnlineGameRememberedValues())
    }
    var times by remember {
        mutableIntStateOf(1)
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                for (Game in snapshot.children) {
                    val game = Game.getValue(OnlineGameUiState::class.java)
                    if ((game!!.id == onlineGameId)) {
                        val updatedGame = OnlineGameUiState(
                            id = game.id,
                            player1 = game.player1,
                            player2 = player,
                            winner = game.winner,
                            boxes = game.boxes
                        )
                        databaseReference.child(game.id).child("player2").setValue(player)
                        MyTurn = "O"
                        currentGame.game = updatedGame
                        break
                    }
                }
                times++
            }
            //find game
            try {
                currentGame.game = snapshot.children.find {
                    it.getValue(OnlineGameUiState::class.java)!!.id == onlineGameId
                }?.getValue(OnlineGameUiState::class.java)!!
            } catch (e: Exception) {
                currentGame.game = OnlineGameUiState()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(
                context,
                "Fail to get the data.",
                Toast.LENGTH_SHORT
            ).show()
        }
    })

    currentGame.player1 = getPlayer(email = currentGame.game.player1)
    currentGame.player2 = getPlayer(email = player)

    CodeGameWaitingRoomScreen(
        gameState = currentGame,
        isLeader = false,
        navController = navController,
        onLeaveGame = {
            databaseReference.child(onlineGameId).child("player2").setValue("")
            navController.popBackStack()
        },
        enable = enable
    )

}

@Composable
fun OpenGameOnline(
    context: Context,
    player: String,
    navController: NavController,
    enable: Enable
) {
    val currentGame by remember {
        mutableStateOf(OnlineGameRememberedValues())
    }
    var times by remember {
        mutableIntStateOf(1)
    }
    currentGame.player1 = getPlayer(email = player)
    currentGame.player2 = getPlayer(email = currentGame.game.player2)

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")
    val db: DatabaseReference = firebaseDatabase.getReference("Players")

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1 && currentGame.player1 != MainPlayerUiState()) {
                if (currentGame.game == OnlineGameUiState()) {
                    val key: String = databaseReference.push().key!!.takeLast(5)
                    val newGame = OnlineGameUiState(
                        id = key,
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = com.idos.tictactoe.data.Boxes(),
                        player1TimeLeft = currentGame.player1.onlineTimeLimit,
                        player2TimeLeft = currentGame.player1.onlineTimeLimit
                    )
                    onlineGameId = key
                    databaseReference.child(key)
                        .setValue(newGame)
                    currentGame.game = newGame
                    MyTurn = "X"
                }
                times++
            }
            //find game
            currentGame.game = try {
                snapshot.children.find {
                    it.getValue(OnlineGameUiState::class.java)!!.id == onlineGameId
                }?.getValue(OnlineGameUiState::class.java)!!
            } catch (e: Exception) {
                OnlineGameUiState()
            }

            //get Players collection from database
            db.addValueEventListener(object : ValueEventListener {
                //on success
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children
                    try {
                        currentGame.player2 = list.find {
                            it.getValue(MainPlayerUiState::class.java)!!.email == currentGame.game.player2
                        }?.getValue(MainPlayerUiState::class.java)!!
                    } catch (e: Exception) {
                        currentGame.player2 = MainPlayerUiState()
                    }

                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(
                context,
                "Fail to get the data.",
                Toast.LENGTH_SHORT
            ).show()
        }
    })
    CodeGameWaitingRoomScreen(
        gameState = currentGame,
        isLeader = true,
        navController = navController,
        onLeaveGame = {
            deleteGame(
                context,
                databaseReference
            )
            navController.popBackStack()
        },
        enable = enable
    )
}

@Composable
private fun CodeGameWaitingRoomScreen(
    gameState: OnlineGameRememberedValues,
    isLeader: Boolean,
    navController: NavController,
    onLeaveGame: () -> Unit,
    enable: Enable
) {
    var foundPlayer by remember {
        mutableStateOf(false)
    }
    var changedScreen by remember {
        mutableStateOf(false)
    }

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")
    val db = firebaseDatabase.getReference("Players")

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            //find game
            try {
                gameState.game = snapshot.children.find {
                    it.getValue(OnlineGameUiState::class.java)!!.id == onlineGameId
                }?.getValue(OnlineGameUiState::class.java)!!
            } catch (e: Exception) {
                gameState.game = OnlineGameUiState()
            }
            if(gameState.game.wasGameStarted && !changedScreen) {
                changedScreen = true
                wasGameStarted = true
                enable.enable = true
                navController.navigate("${GameScreen.Online.title}/GamesWithCode")
            }

            //get Players collection from database
            db.addValueEventListener(object : ValueEventListener {
                //on success
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children
                    try {
                        gameState.player2 = list.find {
                            it.getValue(MainPlayerUiState::class.java)!!.email == gameState.game.player2
                        }?.getValue(MainPlayerUiState::class.java)!!

                        foundPlayer = true
                    } catch (e: Exception) {
                        gameState.player2 = MainPlayerUiState()
                        foundPlayer = false
                    }

                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
        override fun onCancelled(error: DatabaseError) {}
    })

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Column(
        modifier = Modifier
            .background(brush)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isLeader) {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        }
        //game code
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Code",
                fontSize = screenHeight.sp * 0.05,
                color = colors.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = ": ",
                fontSize = screenHeight.sp * 0.05,
                color = colors.onBackground,
                textAlign = TextAlign.Center
            )
            if(onlineGameId != "") {
                Text(
                    text = " $onlineGameId",
                    fontSize = screenHeight.sp * 0.05,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center
                )
            } else {
                DotsFlashing(size = screenWidth / 30)
            }
        }

        Spacer(modifier = Modifier.fillMaxHeight(0.1f))

        //player1
        gameState.player1.Draw(
            modifier = Modifier
                .size((screenWidth / 3).dp),
            screenWidth = screenWidth,
            shape = RoundedCornerShape(20),
            border = BorderStroke(2.dp, colors.onBackground),
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        Text(
            text = "VS",
            fontSize = screenHeight.sp * 0.075,
            color = colors.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))

        //player2
        if (!foundPlayer && isLeader) {
            Card(
                modifier = Modifier
                    .size((screenWidth / 3).dp),
                shape = RoundedCornerShape(20),
                border = BorderStroke(2.dp, colors.onBackground),
                colors = CardDefaults.cardColors(colors.background)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.fillMaxHeight(0.2f))
                    Text(
                        text = "waiting for player",
                        fontSize = screenHeight.sp * 0.025,
                        color = colors.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.fillMaxHeight(0.2f))
                    DotsFlashing(size = screenWidth / 30)
                }
            }
        } else {
            gameState.player2.Draw(
                modifier = Modifier
                    .size((screenWidth / 3).dp),
                screenWidth = screenWidth,
                shape = RoundedCornerShape(20),
                border = BorderStroke(2.dp, colors.onBackground),
            )
        }

        Spacer(modifier = Modifier.fillMaxHeight(0.3f))

        if(isLeader) {
            Button(
                onClick = {
                    databaseReference.child(onlineGameId).child("wasGameStarted").setValue(true)
                          },
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(0.5f),

                enabled = gameState.player2 != MainPlayerUiState(),
                colors = ButtonDefaults.buttonColors(containerColor = colors.onPrimary)
            ) {
                Text(
                    text = "Start",
                    fontSize = screenHeight.sp*0.03,
                    fontWeight = FontWeight.SemiBold,
                    color = if(gameState.player2 != MainPlayerUiState()) {colors.primary} else {colors.onPrimary}
                )
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))

        Button(
            onClick = onLeaveGame,
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth(0.3f),

            enabled = gameState.player2 == MainPlayerUiState() || !isLeader,
            colors = ButtonDefaults.buttonColors(containerColor = colors.error)
        ) {
            Text(
                text = "Leave",
                fontSize = screenHeight.sp*0.03,
                fontWeight = FontWeight.SemiBold,
                color = if(gameState.player2 == MainPlayerUiState() || !isLeader) {colors.onError} else {colors.onPrimary}
            )
        }
    }
}

@Composable
fun DotsFlashing(
    size: Int
) {
    val minAlpha = 0.1f

    @Composable
    fun Dot(
        alpha: Float
    ) = Spacer(
        Modifier
            .size(size.dp)
            .alpha(alpha)
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = CircleShape
            )
    )

    val infiniteTransition = rememberInfiniteTransition(label = "")

    @Composable
    fun animateAlphaWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = minAlpha,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 300 * 4
                minAlpha at delay with LinearEasing
                1f at delay + 300 with LinearEasing
                minAlpha at delay + 300 * 2
            }
        ), label = ""
    )

    val alpha1 by animateAlphaWithDelay(0)
    val alpha2 by animateAlphaWithDelay(300)
    val alpha3 by animateAlphaWithDelay(300 * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val spaceSize = 2.dp

        Dot(alpha1)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha2)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha3)
    }
}


@Composable
fun OpenNewGameButton(modifier: Modifier, navController: NavController, context: Context, screenHeight: Dp, colors: ColorScheme) {
    var openGame by remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Create Room",
                fontWeight = FontWeight.SemiBold,
                fontSize = screenHeight.value.sp * 0.035,
                textAlign = TextAlign.Center,
                color = colors.onSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.secondary)
                    .weight(1f)
            )
            Text(
                text = "Open room\nReceive a code\nShare the code with your friends and play together",
                fontWeight = FontWeight.SemiBold,
                fontSize = screenHeight.value.sp * 0.02,
                color = colors.onBackground,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(4f)
            )
            Spacer(modifier = Modifier.weight(2.2f))
            Button(
                onClick = {openGame = true},
                colors = ButtonDefaults.buttonColors(containerColor = colors.onBackground),
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = "Create",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = screenHeight.value.sp * 0.03,
                    textAlign = TextAlign.Center,
                    color = colors.background,
                    modifier = Modifier
                )
            }
        }
    }
    if (openGame) {
        //start service
        context.startService(Intent(context, CodeGameService::class.java))
        openGame = false
        enteredGame = true
        navController.navigate(GameScreen.OpenGameWithCode.title)
    }
}

@Composable
fun EnterGameWithCodeButton(
    modifier: Modifier,
    navController: NavController,
    screenHeight: Dp,
    colors: ColorScheme
) {
    var checkGame by remember {
        mutableStateOf(false)
    }
    var code by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    //controls the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Join Room",
                fontWeight = FontWeight.SemiBold,
                fontSize = screenHeight.value.sp*0.035,
                textAlign = TextAlign.Center,
                color = colors.onSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.secondary)
                    .weight(1f)
            )
            Text(
                text = "Enter room code",
                fontWeight = FontWeight.SemiBold,
                fontSize = screenHeight.value.sp*0.025,
                color = colors.onPrimary,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(4f)
            )
            TextField(
                value = code,
                onValueChange = {
                    code = it
                                },
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(0.9f),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colors.onPrimary.copy(0.5f),
                    focusedContainerColor = colors.onPrimary,
                    focusedTextColor = colors.primary,
                    unfocusedTextColor = colors.primary
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                maxLines = 1,
                placeholder = {
                    Text(
                        text = "Code:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = screenHeight.value.sp*0.02,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    enteredGame = false
                    checkGame = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = colors.onPrimary),
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = "Join Room",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = screenHeight.value.sp*0.03,
                    textAlign = TextAlign.Center,
                    color = colors.primary
                )
            }
        }
    }

    if(checkGame) {
        onlineGameId = code
        CheckForGame(
            context = context,
            onFindGame = {
                //start service
                context.startService(Intent(context, CodeGameService::class.java))
                checkGame = false
                navController.navigate(GameScreen.EnterGameWithCode.title)
            },
            notFindGame = {
                checkGame = false
                onlineGameId = ""
            }
        )
    }
}

@Composable
fun CheckForGame(context: Context, onFindGame: () -> Unit, notFindGame: () -> Unit) {
    var game by remember {
        mutableStateOf(OnlineGameUiState())
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            val list = snapshot.children
            game = try {
                list.find {
                    it.getValue(OnlineGameUiState::class.java)!!.id == onlineGameId
                }?.getValue(OnlineGameUiState::class.java)!!
            } catch (e: Exception) {
                OnlineGameUiState()
            }
            if (game == OnlineGameUiState()) {
                if(!enteredGame) {
                    Toast.makeText(context, "Game was not found", Toast.LENGTH_SHORT).show()
                    notFindGame()
                }
            } else {
                if (game.player2 != "") {
                    if(!enteredGame) {
                        Toast.makeText(context, "Game has already started", Toast.LENGTH_SHORT)
                            .show()
                        notFindGame()
                    }
                } else {
                    if(!enteredGame) {
                        wasGameStarted = true
                        enteredGame = true
                        onFindGame()
                    }
                }
            }
        }
        override fun onCancelled(error: DatabaseError) {
        }
    })
}

@Composable
fun CodeGameScreen(
    navController: NavController,
    context: Context
) {
    //controls the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brush)
            .pointerInput(key1 = null) {
                // hide keyboard on tap
                detectTapGestures(
                    onTap = { keyboardController?.hide() }
                )
            }
    ) {
        EnterGameWithCodeButton(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(10.dp),
            navController = navController,
            screenHeight = screenHeight,
            colors = colors
        )
        OpenNewGameButton(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(10.dp),
            navController = navController,
            context = context,
            screenHeight = screenHeight,
            colors = colors
        )
    }
}