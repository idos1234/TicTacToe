package com.idos.tictactoe.ui.Online

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.Screen.GameScreen
import com.idos.tictactoe.ui.Screen.getPlayer

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
fun playersBar(
    modifier: Modifier,
    databaseReference: DatabaseReference,
    context: Context = LocalContext.current,
    screenWidth: Int,
    colors: ColorScheme
) {
    var game by remember {
        mutableStateOf(OnlineGameUiState())
    }
    game = findGame(gameId = onlineGameId, databaseReference = databaseReference)

    val player1 = getPlayer(email = game.player1, context = context)
    val player2 = getPlayer(email = game.player2, context = context)

    val player1CurrentImage = GetXO(player1.currentImage)
    val player2CurrentImage = GetXO(player1.currentImage)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .size(((screenWidth - 40) / 4).dp),
                shape = RoundedCornerShape(20),
                border = BorderStroke(2.dp, if (game.playerTurn == "X") colors.tertiary else { colors.background})
            ) {
                Image(
                    painter = painterResource(id = player1CurrentImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20))
                )
            }
            Text(
                player1.name,
                fontSize = screenWidth.sp * 0.05,
                color = colors.onBackground
            )
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = colors.primary),
            modifier = Modifier
                .height(((screenWidth - 40) / 3 / 3).dp)
                .weight(1f)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "${game.player1Score} : ${game.player2Score}",
                    fontSize = screenWidth.sp * 0.04,
                    color = colors.onPrimary
                )
            }
        }
        Column(
            Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .size(((screenWidth - 40) / 4).dp),
                shape = RoundedCornerShape(20),
                border = BorderStroke(2.dp, if (game.playerTurn == "O") colors.tertiary else { colors.background})
            ) {
                Image(
                    painter = painterResource(id = player2CurrentImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20))
                )
            }
            Text(
                player2.name,
                fontSize = screenWidth.sp * 0.05,
                color = colors.onBackground

            )
        }
    }
}

@Composable
fun OpenOnlineGameWithCode(context: Context, player: String, viewModel: CodeGameViewModel, navController: NavController, enableState: Enable, codeGameViewModel: CodeGameViewModel) {
    val currentGame by remember {
        mutableStateOf(OnlineGameRememberedValues())
    }
    var times by remember {
        mutableStateOf(1)
    }
    var foundPlayer by remember {
        mutableStateOf(false)
    }
    var removeGame by remember() {
        mutableStateOf(false)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                if (currentGame.game == OnlineGameUiState()) {
                    val key: String = databaseReference.push().key!!.takeLast(5)
                    val newGame = OnlineGameUiState(
                        id = key,
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = com.idos.tictactoe.data.Boxes()
                    )
                    onlineGameId = key
                    databaseReference.child(key)
                        .setValue(newGame)
                    currentGame.game = newGame
                    //get Players collection from database
                    db.collection("Players").get()
                        //on success
                        .addOnSuccessListener { queryDocumentSnapshots ->
                            //check if collection is empty
                            if (!queryDocumentSnapshots.isEmpty) {
                                val list = queryDocumentSnapshots.documents
                                for (d in list) {
                                    val p: MainPlayerUiState? =
                                        d.toObject(MainPlayerUiState::class.java)
                                    if (p?.email == player) {
                                        currentGame.player1 = p
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
                }
                times++
            }
            for (Game in snapshot.children) {
                val game = Game.getValue(OnlineGameUiState::class.java)
                if (game!!.id == onlineGameId) {
                    currentGame.game = game
                    if (currentGame.game.player2 != "") {
                        foundPlayer = true
                        wasGameStarted = true
                        //get Players collection from database
                        db.collection("Players").get()
                            //on success
                            .addOnSuccessListener { queryDocumentSnapshots ->
                                //check if collection is empty
                                if (!queryDocumentSnapshots.isEmpty) {
                                    val list = queryDocumentSnapshots.documents
                                    for (d in list) {
                                        val p: MainPlayerUiState? = d.toObject(
                                            MainPlayerUiState::class.java)
                                        if (p?.name == currentGame.game.player2){
                                            currentGame.player2 = p
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
                    }
                    break
                }
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(brush)
            .fillMaxSize()) {
        playersBar(
            modifier = Modifier.weight(2f),
            databaseReference = databaseReference,
            screenWidth = screenWidth.value.toInt(),
            colors = colors
        )
        Spacer(modifier = Modifier.weight(1f))
        OnlineButtonGrid(
            myTurn = "X",
            player = player,
            databaseReference = databaseReference,
            viewModel = viewModel,
            navController = navController,
            enableState = enableState,
            gameState = currentGame,
            modifier = Modifier.weight(6f),
            dbName = "GamesWithCode"
        )
        Spacer(modifier = Modifier.weight(1f))
    }
    
    if(!foundPlayer) {
        Dialog(
            onDismissRequest = {}
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.3f)
            ) {
                Box(modifier = Modifier.weight(2f)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(Color.White)
                    ) {
                        Text(
                            text = "Game code:",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = onlineGameId,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Button(onClick = {removeGame = true}, modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp, top = 16.dp)
                    .height(20.dp)
                    .fillMaxWidth(0.9f)) {
                    Text(
                        text = "Quit",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }

    if(removeGame) {
        viewModel.removeGame(onlineGameId, databaseReference, 0) {
            //clears code after quit game
            codeGameViewModel.clearCode()
            navController.navigate(GameScreen.CodeGame.title)
        }
    }
}

@Composable
fun EnterOnlineGameWithCode(context: Context, player: String, gameId: String, viewModel: CodeGameViewModel, navController: NavController, enableState: Enable) {
    val currentGame by remember {
        mutableStateOf(OnlineGameRememberedValues())
    }
    var times by remember {
        mutableIntStateOf(1)
    }
    var foundPlayer by remember {
        mutableStateOf(false)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                for (Game in snapshot.children) {
                    val game = Game.getValue(OnlineGameUiState::class.java)
                    if ((game!!.id == gameId)) {
                        val updatedGame = OnlineGameUiState(
                            id = game.id,
                            player1 = game.player1,
                            player2 = player,
                            winner = game.winner,
                            boxes = game.boxes
                        )
                        databaseReference.child(game.id).child("player2").setValue(player)
                        foundPlayer = true
                        currentGame.game = updatedGame
                        break
                    }
                }
                times++
            }
            for (Game in snapshot.children) {
                val game = Game.getValue(OnlineGameUiState::class.java)
                if (game!!.id == onlineGameId) {
                    currentGame.game = game
                    }
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

    currentGame.player1 = getPlayer(email = currentGame.game.player1, context = context)
    currentGame.player2 = getPlayer(email = currentGame.game.player2, context = context)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(brush)
            .fillMaxSize()) {
        playersBar(
            modifier = Modifier.weight(2f),
            databaseReference = databaseReference,
            screenWidth = screenWidth.value.toInt(),
            colors = colors
        )
        Spacer(modifier = Modifier.weight(1f))
        OnlineButtonGrid(
            myTurn = "O",
            player = player,
            databaseReference = databaseReference,
            viewModel = viewModel,
            navController = navController,
            enableState = enableState,
            gameState = currentGame,
            modifier = Modifier.weight(6f),
            dbName = "GamesWithCode"
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun openNewGameButton(modifier: Modifier, navController: NavController, context: Context, screenHeight: Dp, colors: ColorScheme) {
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
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = "Create",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = screenHeight.value.sp * 0.03,
                    textAlign = TextAlign.Center,
                    color = colors.onPrimary,
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
fun enterGameWithCodeButton(
    modifier: Modifier,
    codeGameViewModel: CodeGameViewModel,
    codeGameUiState: OnlineGameRememberedValues,
    navController: NavController,
    screenHeight: Dp,
    colors: ColorScheme
) {
    var checkGame by remember {
        mutableStateOf(false)
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
                value = codeGameUiState.gameCode,
                onValueChange = { codeGameViewModel.updateGameCode(it) },
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
        CheckForGame(
            gameId = codeGameUiState.gameCode,
            context = context,
            onFindGame = {
                //start service
                context.startService(Intent(context, CodeGameService::class.java))

                checkGame = false
                navController.navigate(GameScreen.EnterGameWithCode.title)
            },
            notFindGame = { checkGame = false }
        )
    }
}

@Composable
fun CheckForGame(gameId: String, context: Context, onFindGame: () -> Unit, notFindGame: () -> Unit) {
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
                    it.getValue(OnlineGameUiState::class.java)!!.id == gameId
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
                        onlineGameId = gameId
                        onFindGame()
                    }
                }
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
}

@Composable
fun codeGameScreen(codeGameViewModel: CodeGameViewModel, codeGameUiState: OnlineGameRememberedValues, navController: NavController, context: Context) {
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
        enterGameWithCodeButton(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(10.dp),
            codeGameUiState = codeGameUiState,
            codeGameViewModel = codeGameViewModel,
            navController = navController,
            screenHeight = screenHeight,
            colors = colors
        )
        openNewGameButton(
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