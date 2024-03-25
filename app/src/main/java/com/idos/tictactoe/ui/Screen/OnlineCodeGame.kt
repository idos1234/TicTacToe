package com.idos.tictactoe.ui.Screen

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Primery
import com.idos.tictactoe.ui.theme.Secondery

private var enteredGame = false
private var gameStarted = false
private var codeGameId: String = ""


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

        if(!gameStarted) {
            CodeGameViewModel().removeGame(codeGameId, databaseReference, 0) {
                stopService(
                    Intent(
                        baseContext,
                        CodeGameService::class.java
                    )
                )
            }
        }
    }
}

@Composable
fun playersBar(player1: MainPlayerUiState, player2: MainPlayerUiState, size: Dp, modifier: Modifier, currentGame: OnlineGameUiState, foundPlayer: Boolean) {
    val player1CurrentImage = GetXO(player1.currentImage)
    val player2CurrentImage = GetXO(player1.currentImage)

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(3f)) {
            if (player1 != MainPlayerUiState()) {
                Card(
                    modifier = Modifier.size(size),
                    elevation = 5.dp,
                    backgroundColor = Secondery,
                    border = BorderStroke(
                        5.dp,
                        if (currentGame.playerTurn == "X") Primery else {
                            Secondery
                        }
                    )
                ) {
                    Image(
                        painter = painterResource(id = player1CurrentImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Text(player1.name, fontSize = 10.sp, color = Color.White)
            } else {
                Card(
                    modifier = Modifier.size(size),
                    elevation = 5.dp,
                    backgroundColor = Secondery,
                    border = BorderStroke(
                        2.dp,
                        if (currentGame.playerTurn == "O") Primery else {
                            Secondery
                        }
                    )
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text("${currentGame.player1Score} : ${currentGame.player2Score}", fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(3f)) {
            if (foundPlayer) {
                Card(
                    modifier = Modifier.size(size),
                    elevation = 5.dp,
                    backgroundColor = Secondery,
                    border = BorderStroke(
                        5.dp,
                        if (currentGame.playerTurn == "O") Primery else {
                            Secondery
                        }
                    )
                ) {
                    Image(
                        painter = painterResource(id = player2CurrentImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Text(player2.name, fontSize = 10.sp, color = Color.White)
            } else {
                Card(
                    modifier = Modifier.size(size),
                    elevation = 5.dp,
                    backgroundColor = Secondery,
                    border = BorderStroke(
                        2.dp,
                        if (currentGame.playerTurn == "O") Primery else {
                            Secondery
                        }
                    )
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun OpenOnlineGameWithCode(context: Context, player: String, viewModel: CodeGameViewModel, navController: NavController, enableState: Enable, codeGameViewModel: CodeGameViewModel) {
    var currentGame by remember {
        mutableStateOf(OnlineGameUiState())
    }
    var times by remember {
        mutableStateOf(1)
    }
    var foundPlayer by remember {
        mutableStateOf(false)
    }
    var myTurn by remember {
        mutableStateOf<String?>(null)
    }
    var player1 by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var player2 by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var removeGame by remember() {
        mutableStateOf(false)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val size = (screenWidth/10)*3

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                if (currentGame == OnlineGameUiState()) {
                    val key: String = databaseReference.push().key!!.takeLast(5)
                    val newGame = OnlineGameUiState(
                        id = key,
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = com.idos.tictactoe.data.Boxes()
                    )
                    codeGameId = key
                    databaseReference.child(key)
                        .setValue(newGame)
                    currentGame = newGame
                    myTurn = "X"
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
                                        player1 = p
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
                if (game!!.id == currentGame.id) {
                    currentGame = game
                    if (currentGame.player2 != "") {
                        foundPlayer = true
                        gameStarted = true
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
                                        if (p?.name == currentGame.player2){
                                            player2 = p
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

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        playersBar(
            player1 = player1,
            player2 = player2,
            size = size,
            modifier = Modifier.weight(2f),
            currentGame = currentGame,
            foundPlayer = foundPlayer
        )
        Spacer(modifier = Modifier.weight(1f))
        OnlineButtonGrid(
            gameId = currentGame.id,
            myTurn = myTurn,
            gameStarted = foundPlayer,
            player = player,
            player1 = player1,
            player2 = player2,
            databaseReference = databaseReference,
            viewModel = viewModel,
            navController = navController,
            enableState = enableState,
            modifier = Modifier.weight(6f)
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
                            text = currentGame.id,
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
        viewModel.removeGame(currentGame.id, databaseReference, 0) {
            //clears code after quit game
            codeGameViewModel.clearCode()
            navController.navigate(GameScreen.CodeGame.title)
        }
    }
}

@Composable
fun EnterOnlineGameWithCode(context: Context, player: String, gameId: String, viewModel: CodeGameViewModel, navController: NavController, enableState: Enable) {
    var currentGame by remember {
        mutableStateOf(OnlineGameUiState())
    }
    var times by remember {
        mutableIntStateOf(1)
    }
    var myTurn by remember {
        mutableStateOf<String?>(null)
    }
    var player1 by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var player2 by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var foundPlayer by remember {
        mutableStateOf(false)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val size = (screenWidth/10)*3

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
                                        //find player using database
                                        if (p?.email == game.player1){
                                            player1 = p
                                        }
                                        if (p?.email == player){
                                            player2 = p
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
                        databaseReference.child(game.id).child("player2").setValue(player)
                        foundPlayer = true
                        currentGame = updatedGame
                        myTurn = "O"
                        break
                    }
                }
                times++
            }
            for (Game in snapshot.children) {
                val game = Game.getValue(OnlineGameUiState::class.java)
                if (game!!.id == currentGame.id) {
                    currentGame = game
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

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        playersBar(
            player1 = player1,
            player2 = player2,
            size = size,
            modifier = Modifier.weight(2f),
            currentGame = currentGame,
            foundPlayer = foundPlayer
        )
        Spacer(modifier = Modifier.weight(1f))
        OnlineButtonGrid(
            gameId = currentGame.id,
            myTurn = myTurn,
            gameStarted = foundPlayer,
            player = player,
            player1 = player1,
            player2 = player2,
            databaseReference = databaseReference,
            viewModel = viewModel,
            navController = navController,
            enableState = enableState,
            modifier = Modifier.weight(6f)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun openNewGameButton(modifier: Modifier, navController: NavController, context: Context) {
    var openGame by remember {
        mutableStateOf(false)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(BackGround)) {
        Button(onClick = {openGame = true}, colors = ButtonDefaults.buttonColors(backgroundColor = Primery)) {
            Text(text = "Open new game", fontSize = 30.sp)
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
fun enterGameWithCodeButton(modifier: Modifier, codeGameViewModel: CodeGameViewModel, codeGameUiState: OnlineGameRememberedValues, navController: NavController) {
    var checkGame by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    //controls the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(BackGround)
    ) {
        TextField(
            value = codeGameUiState.gameCode,
            onValueChange = { codeGameViewModel.updateGameCode(it) },
            placeholder = { Text(text = "Code:")},
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Secondery),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                enteredGame = false
                checkGame = true
                      },
            colors = ButtonDefaults.buttonColors(backgroundColor = Primery)) {
            Text(text = "Enter game", fontSize = 30.sp)
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
                        gameStarted = true
                        enteredGame = true
                        codeGameId = gameId
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(BackGround)
            .pointerInput(key1 = null){
                // hide keyboard on tap
                detectTapGestures(
                    onTap = { keyboardController?.hide() }
                )
            }
    ) {
        enterGameWithCodeButton(modifier = Modifier.weight(1f), codeGameUiState = codeGameUiState, codeGameViewModel = codeGameViewModel, navController = navController)
        openNewGameButton(modifier = Modifier.weight(1f), navController = navController, context = context)
    }
}