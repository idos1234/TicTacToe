package com.idos.tictactoe.ui.Online

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.tictactoe.ui.Screen.CheckOnlineWinner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.idos.tictactoe.R
import com.idos.tictactoe.data.Boxes
import com.idos.tictactoe.data.GetO
import com.idos.tictactoe.data.GetX
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.Screen.GameScoreDialog
import com.idos.tictactoe.ui.Screen.ShowPlayersDialog
import com.idos.tictactoe.ui.Screen.getPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var wasGameStarted = false
var onlineGameId: String = ""
var MyTurn: String = ""
var otherPlayerQuit = false

class OnlineGameService : Service() {
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
        val databaseReference = firebaseDatabase.getReference("Games")

        deleteGame(this, databaseReference)
    }
}

fun deleteGame(context: Context, databaseReference: DatabaseReference) {
    if(!wasGameStarted) {
        //deletes game
        CodeGameViewModel().removeGame(onlineGameId, databaseReference, 0) {
            context.stopService(
                Intent(
                    context,
                    OnlineGameService::class.java
                )
            )
        }
    } else {
        //other player quit
        if(otherPlayerQuit) {
            //deletes game
            wasGameStarted = false
            CodeGameViewModel().removeGame(onlineGameId, databaseReference, 0) {
                context.stopService(
                    Intent(
                        context,
                        OnlineGameService::class.java
                    )
                )
            }
        } else {
            //player quit
            CodeGameViewModel().playerQuit(MyTurn, databaseReference, onlineGameId, 0, context)
            context.stopService(
                Intent(
                    context,
                    OnlineGameService::class.java
                )
            )
        }
        wasGameStarted = false
        onlineGameId = ""
        MyTurn = ""
        otherPlayerQuit = false
    }
}

@Composable
fun SetBoxOnline(
    game: OnlineGameUiState,
    boxNumber: Int,
    playerTurn: String,
    databaseReference: DatabaseReference
) {

    if(game.id == "") {
        return
    }
    val boxes: Boxes? = when(boxNumber) {
        1 -> game.boxes.copy(Box1 = playerTurn)
        2 -> game.boxes.copy(Box2 = playerTurn)
        3 -> game.boxes.copy(Box3 = playerTurn)
        4 -> game.boxes.copy(Box4 = playerTurn)
        5 -> game.boxes.copy(Box5 = playerTurn)
        6 -> game.boxes.copy(Box6 = playerTurn)
        7 -> game.boxes.copy(Box7 = playerTurn)
        8 -> game.boxes.copy(Box8 = playerTurn)
        9 -> game.boxes.copy(Box9 = playerTurn)
        else -> null
    }

    databaseReference.child(onlineGameId).child("boxes").setValue(boxes)
    databaseReference.child(onlineGameId).child("times").setValue(game.times.plus(1))
}

@Composable
fun changePlayerTurn(game: OnlineGameUiState, databaseReference: DatabaseReference, onlineGameId: String): String {
    if(game.id == "") {
        return game.playerTurn
    }

    val PlayerTurn = if(game.playerTurn == "X") {
        "O"
    } else {
        "X"
    }

    databaseReference.child(onlineGameId).child("playerTurn").setValue(PlayerTurn)

    return PlayerTurn
}

@Composable
fun OnlineGameButton(
    gameState: OnlineGameRememberedValues,
    boxNumber: Int,
    setBox: (String) -> Unit,
    box: String,
    enableState: Enable,
    context: Context = LocalContext.current,
    databaseReference: DatabaseReference,
    modifier: Modifier,
    myTurn: String?
) {
    var player1 by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var player2 by remember {
        mutableStateOf(MainPlayerUiState())
    }

    //get Players collection from database
    player1 = getPlayer(email = player1.email, context = context)
    player2 = getPlayer(email = player2.email, context = context)

    val colors = MaterialTheme.colorScheme

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    var SetBox by remember {
        mutableStateOf(false)
    }

    Card(
        shape = RoundedCornerShape(20),
        modifier = modifier
            .padding(5.dp)
            .size(((screenWidth - 20 - 30) / 3).dp)
            .background(Color.Transparent)
            .clickable(
                onClick = {
                    enableState.enable = false
                    SetBox = true
                },
                enabled = (box == "") && enableState.enable && gameState.game.playerTurn == myTurn && wasGameStarted,
            ),
        border = BorderStroke(1.dp, colors.onPrimary),
        colors = CardDefaults.cardColors(containerColor = colors.primary)
    ) {
        Image(
            painter = painterResource(
                id = when (box) {
                    "X" -> GetX(player1.currentX)
                    "O" -> GetO(player2.currentO)
                    else -> {
                        R.drawable.o_1
                    }
                }
            ),
            contentDescription = null,
            alpha = if (box != "") DefaultAlpha else 0f,
            modifier = Modifier.fillMaxSize()
        )
    }

    if (SetBox) {
        val turn = changePlayerTurn(gameState.game, databaseReference = databaseReference, onlineGameId)
        setBox(gameState.game.playerTurn)
        SetBoxOnline(
            game = gameState.game,
            boxNumber = boxNumber,
            playerTurn = gameState.game.playerTurn,
            databaseReference = databaseReference,
        )
        gameState.game.playerTurn = turn
        enableState.enable = true
        SetBox = false
    }
}

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition",
    "SuspiciousIndentation"
)
@Composable
fun OnlineButtonGrid(
    myTurn: String?,
    gameStarted: Boolean,
    player: String,
    databaseReference: DatabaseReference,
    viewModel: CodeGameViewModel,
    navController: NavController,
    enableState: Enable,
    gameState: OnlineGameRememberedValues,
    modifier: Modifier,
    gameId: String
) {
    var setData by remember {
        mutableStateOf(false)
    }
    if (!setData) {
        setData = true
        wasGameStarted = gameStarted
        onlineGameId = gameId
    }

    var foundWinner by remember {
        mutableStateOf(false)
    }
    var times by remember {
        mutableStateOf(0)
    }
    var startedCountDown by remember {
        mutableStateOf(false)
    }
    var boxes by remember {
        mutableStateOf(gameState.game.boxes)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .background(Color.Transparent)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)) {
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 1,
                setBox = {boxes.Box1 = it},
                box = boxes.Box1,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f),
                myTurn = myTurn
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 2,
                setBox = {boxes.Box2 = it},
                box = boxes.Box2,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f),
                myTurn = myTurn
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 3,
                setBox = {boxes.Box3 = it},
                box = boxes.Box3,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f),
                myTurn = myTurn
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)) {
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 4,
                setBox = {boxes.Box4 = it},
                box = boxes.Box4,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f),
                myTurn = myTurn
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 5,
                setBox = {boxes.Box5 = it},
                box = boxes.Box5,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f),
                myTurn = myTurn
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 6,
                setBox = {boxes.Box6 = it},
                box = boxes.Box6,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f),
                myTurn = myTurn
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)) {
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 7,
                setBox = {boxes.Box7 = it},
                box = boxes.Box7,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f),
                myTurn = myTurn
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 8,
                setBox = {boxes.Box8 = it},
                box = boxes.Box8,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f),
                myTurn = myTurn
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 9,
                setBox = {boxes.Box9 = it},
                box = boxes.Box9,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f),
                myTurn = myTurn
            )
        }
    }

    gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
    boxes = gameState.game.boxes
    //if can be a winner
    if(gameState.game.times >= 5 && wasGameStarted) {
        if (findGame(gameId = onlineGameId, databaseReference =databaseReference) != OnlineGameUiState()) {
            databaseReference.child(onlineGameId).child("winner").setValue(CheckOnlineWinner(boxes))
        }
        //if has winner
        if(gameState.game.winner.isNotEmpty()) {
            //if no one found the winner
            if (!gameState.game.foundWinner) {
                if (findGame(gameId = onlineGameId, databaseReference =databaseReference) != OnlineGameUiState()) {
                    databaseReference.child(onlineGameId).child("foundWinner").setValue(true)
                }
                //update game score
                if (gameState.game.winner == "X") {
                    viewModel.changeEnable(false)
                    if (findGame(gameId = onlineGameId, databaseReference =databaseReference) != OnlineGameUiState()) {
                        databaseReference.child(onlineGameId).child("player1Score")
                            .setValue(gameState.game.player1Score.plus(1))
                        databaseReference.child(onlineGameId).child("rounds")
                            .setValue(gameState.game.rounds.plus(1))
                    }
                }
                if ((gameState.game.winner == "O")) {
                    viewModel.changeEnable(false)
                    if (findGame(gameId = onlineGameId, databaseReference =databaseReference) != OnlineGameUiState()) {
                        databaseReference.child(onlineGameId).child("player2Score")
                            .setValue(gameState.game.player2Score.plus(1))
                        databaseReference.child(onlineGameId).child("rounds")
                            .setValue(gameState.game.rounds.plus(1))
                    }
                }
            }

            // if has final winner
            if (gameState.game.player1Score == 2) {
                if (myTurn == "X") {
                    //won
                    viewModel.updateFinalScoreScreenData("You won", gameState.game, gameState.player1,  gameState.player2)
                    updateScore(
                        playerName = player,
                        addedScore = 1,
                        context = LocalContext.current,
                        gameState = gameState,
                        navController = navController,
                        codeGameViewModel = viewModel
                    )
                }
                else if (myTurn == "O") {
                    //lose
                    viewModel.updateFinalScoreScreenData("You lost", gameState.game, gameState.player1,  gameState.player2)
                    updateScore(
                        playerName = player,
                        addedScore = -1,
                        context = LocalContext.current,
                        gameState = gameState,
                        navController = navController,
                        codeGameViewModel = viewModel
                    )
                }
                // show score screen
            } else if (gameState.game.player2Score == 2) {
                if (myTurn == "O") {
                    //won
                    viewModel.updateFinalScoreScreenData("You won", gameState.game, gameState.player1,  gameState.player2)
                    updateScore(
                        playerName = player,
                        addedScore = 1,
                        context = LocalContext.current,
                        gameState = gameState,
                        navController = navController,
                        codeGameViewModel = viewModel
                    )
                }
                else if (myTurn == "X") {
                    //lose
                    viewModel.updateFinalScoreScreenData("You lost", gameState.game, gameState.player1,  gameState.player2)
                    updateScore(
                        playerName = player,
                        addedScore = -1,
                        context = LocalContext.current,
                        gameState = gameState,
                        navController = navController,
                        codeGameViewModel = viewModel
                    )
                }
                // show score screen
            } else {
                //new round
                gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
                boxes = gameState.game.boxes
                if (gameState.game.foundWinner && gameState.game.player1Score != 2 && gameState.game.player2Score != 2) {
                    scope.launch {
                        if (gameState.game.boxes != Boxes()) {
                            ResetGame(
                                game = gameState.game,
                                databaseReference = databaseReference
                            )
                        }
                        viewModel.changeEnable(true)
                    }
                }
            }
        }
        //tie
        else if (gameState.game.times == 9){
            //if didn't update round
            if (!gameState.game.editedRounds) {
                //update round
                if (findGame(gameId = onlineGameId, databaseReference =databaseReference) != OnlineGameUiState()) {
                    databaseReference.child(onlineGameId).child("editedRounds").setValue(true)
                    databaseReference.child(onlineGameId).child("rounds")
                        .setValue(gameState.game.rounds.plus(1))
                }
            }
            gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
            boxes = gameState.game.boxes
            //reset round
            if (gameState.game.player1Score != 2 && gameState.game.player2Score != 2) {
                scope.launch {
                    delay(3000)
                    if(gameState.game.boxes != Boxes()) {
                        ResetGame(
                            game = gameState.game,
                            databaseReference = databaseReference
                        )
                    }
                }
            }
        }
    } else if(wasGameStarted) {
        if (findGame(gameId = onlineGameId, databaseReference =databaseReference) != OnlineGameUiState()) {
            databaseReference.child(onlineGameId).child("winner").setValue("")
        }
    }


    if (!startedCountDown) {
        foundWinner = gameState.game.foundWinner
    }
    //show countDown dialog for winning
    if (foundWinner && (gameState.game.player1Score != 2) && (gameState.game.player2Score != 2)) {
        startedCountDown = true
        gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
        NextRoundDialog(
            gameState = gameState,
            onZeroSecs = {
                times = 0
                startedCountDown = false
            }
        )
    }
    if (!startedCountDown) {
        times = gameState.game.times
    }
    //show countDown dialog for tie
    if ((times == 9) && (gameState.game.player1Score != 2) && (gameState.game.player2Score != 2)) {
        startedCountDown = true
        gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
        NextRoundDialog(
            gameState = gameState,
            onZeroSecs = {
                times = 0
                startedCountDown = false
            }
        )
    }

    if(gameState.game.player1Quit || gameState.game.player2Quit) {
        otherPlayerQuit = true
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NextRoundDialog(gameState: OnlineGameRememberedValues, onZeroSecs: () -> Unit) {

    var secondsToNextRound by remember {
        mutableStateOf(3)
    }

    LaunchedEffect(key1 = secondsToNextRound) {
        delay(1000L)
        secondsToNextRound--
        if (secondsToNextRound <= 0) {
            onZeroSecs()
        }
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val colors = MaterialTheme.colorScheme
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .background(colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Next round in: $secondsToNextRound",
                fontWeight = FontWeight.Bold,
                fontSize = screenWidth.sp * 0.1,
                color = colors.onBackground
            )
            Spacer(modifier = Modifier.height((screenWidth/10).dp))
            //show players
            ShowPlayersDialog(gameState = gameState)
            Spacer(modifier = Modifier.height((screenWidth/20).dp))
        }
    }
}

@Composable
fun findGame(gameId: String, databaseReference: DatabaseReference, context: Context = LocalContext.current): OnlineGameUiState {
    var game by remember {
        mutableStateOf(OnlineGameUiState())
    }
    //get Players collection from database
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

        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(
                context,
                "Fail to get the data.",
                Toast.LENGTH_SHORT
            ).show()
        }
    })
    return game
}

fun ResetGame(game: OnlineGameUiState, databaseReference: DatabaseReference) {
    if (game.id != "") {
        databaseReference.child(onlineGameId).child("boxes").setValue(Boxes())
        databaseReference.child(onlineGameId).child("winner").setValue("")
        databaseReference.child(onlineGameId).child("times").setValue(0)
        databaseReference.child(onlineGameId).child("foundWinner").setValue(false)
        databaseReference.child(onlineGameId).child("editedRounds").setValue(false)
        val playerTurn = if ((game.rounds % 2) == 0) {
            "O"
        } else if ((game.rounds % 2) == 1) {
            "X"
        } else null
        databaseReference.child(onlineGameId).child("playerTurn").setValue(playerTurn)
    }
}

@Composable
fun OnlineTicTacToe(
    player: String,
    viewModel: CodeGameViewModel,
    navController: NavController,
    enableState: Enable,
    currentGame: OnlineGameRememberedValues,
    myTurn: String?
) {
    currentGame.player1 = getPlayer(email = currentGame.game.player1, context = LocalContext.current)
    currentGame.player2 = getPlayer(email = currentGame.game.player2, context = LocalContext.current)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(brush)
            .fillMaxSize()) {
        playersBar(
            modifier = Modifier.weight(2f),
            gameId = onlineGameId,
            databaseReference = databaseReference,
            screenWidth = screenWidth.value.toInt(),
            colors = colors
        )
        OnlineButtonGrid(
            myTurn = myTurn,
            gameStarted = true,
            player = player,
            databaseReference = databaseReference,
            viewModel = viewModel,
            navController = navController,
            enableState = enableState,
            gameState = currentGame,
            modifier = Modifier
                .padding(10.dp)
                .weight(3f)
                .fillMaxSize(),
            onlineGameId
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun updateScore(
    gameState: OnlineGameRememberedValues,
    navController: NavController,
    codeGameViewModel: CodeGameViewModel,
    playerName: String,
    context: Context,
    addedScore: Int
) {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var score by remember {
        mutableStateOf(0)
    }
    var levelUp by remember {
        mutableStateOf(false)
    }
    var updatedPlayer by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var newLevel by remember {
        mutableStateOf(0)
    }
    var failed by remember {
        mutableStateOf(false)
    }
    var coins by remember {
        mutableIntStateOf(0)
    }
    var showScore by remember {
        mutableStateOf(false)
    }

    //get database
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    if(!showScore) {
        //get Players collection from database
        db.collection("Players").get()
            //on success
            .addOnSuccessListener { queryDocumentSnapshots ->
                //check if collection is empty
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    Loop@ for (d in list) {
                        val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
                        //find player using database
                        if (p?.email == playerName) {
                            player = p
                            score =
                                if (player.score + addedScore < 0) 0 else player.score + addedScore

                            if (addedScore == 1) {
                                when (score) {
                                    25 -> if (player.level == 1) {
                                        newLevel = 2
                                        levelUp = true
                                    }

                                    50 -> if (player.level == 2) {
                                        newLevel = 3
                                        levelUp = true
                                    }

                                    100 -> if (player.level == 3) {
                                        newLevel = 4
                                        levelUp = true
                                    }

                                    150 -> if (player.level == 4) {
                                        newLevel = 5
                                        levelUp = true
                                    }

                                    200 -> if (player.level == 5) {
                                        newLevel = 6
                                        levelUp = true
                                    }

                                    300 -> if (player.level == 6) {
                                        newLevel = 7
                                        levelUp = true
                                    }

                                    400 -> if (player.level == 7) {
                                        newLevel = 8
                                        levelUp = true
                                    }

                                    500 -> if (player.level == 8) {
                                        newLevel = 9
                                        levelUp = true
                                    }

                                    600 -> if (player.level == 9) {
                                        newLevel = 10
                                        levelUp = true
                                    }

                                    700 -> if (player.level == 10) {
                                        newLevel = 11
                                        levelUp = true
                                    }

                                    800 -> if (player.level == 11) {
                                        newLevel = 12
                                        levelUp = true
                                    }

                                    900 -> if (player.level == 12) {
                                        newLevel = 13
                                        levelUp = true
                                    }

                                    1000 -> if (player.level == 13) {
                                        newLevel = 14
                                        levelUp = true
                                    }

                                    1200 -> if (player.level == 14) {
                                        newLevel = 15
                                        levelUp = true
                                    }

                                }
                                if (levelUp) {
                                    coins = ((10 + newLevel * 2)..(20 + newLevel * 2)).random()
                                    updatedPlayer = player.copy(
                                        score = score,
                                        wins = player.wins + 1,
                                        level = newLevel,
                                        coins = player.coins + coins
                                    )
                                    showScore = true
                                } else {
                                    coins = ((5 + newLevel * 2)..(20 + newLevel * 2)).random()
                                    updatedPlayer = player.copy(
                                        score = score,
                                        wins = player.wins + 1,
                                        coins = player.coins + coins
                                    )
                                    showScore = true
                                }
                            } else if (addedScore == -1) {
                                coins = 0
                                updatedPlayer = player.copy(
                                    score = score,
                                    loses = player.loses + 1,
                                )
                                showScore = true
                            }

                            db.collection("Players")
                                .whereEqualTo("email", playerName)
                                .get()
                                .addOnSuccessListener {
                                    for (document in it) {
                                        db.collection("Players").document(document.id).set(
                                            updatedPlayer,
                                            SetOptions.merge()
                                        )
                                    }
                                }
                                .addOnFailureListener {
                                    failed = true
                                }
                            break@Loop
                        }

                    }
                }
            }
            //on failure
            .addOnFailureListener {
                failed = true
            }
    }

    if (failed) {
        updateScore(
            gameState = gameState,
            navController = navController,
            codeGameViewModel = codeGameViewModel,
            playerName = playerName,
            context = context,
            addedScore = addedScore
        )
        failed = false
    } else if(showScore) {
        GameScoreDialog(
            gameState = gameState,
            navController = navController,
            codeGameViewModel = codeGameViewModel,
            context = context,
            coins = coins,
            levelUp = levelUp,
            playerUiState = updatedPlayer,
            won = addedScore == 1
        )
    }
}

@Composable
fun CheckExitOnlineGame(onQuitClick: () -> Unit, onCancelClick: () -> Unit, navController: NavController, context: Context = LocalContext.current) {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")

    AlertDialog(
        onDismissRequest = {},
        title = {},
        text = { Text(text = "Are you sure you want to quit the game") },
        dismissButton = {
            Button(onClick = {
                onQuitClick()
                if(!wasGameStarted) {
                    CodeGameViewModel().removeGame(onlineGameId, databaseReference, 0) {
                        context.stopService(
                            Intent(
                                context,
                                OnlineGameService::class.java
                            )
                        )
                    }
                } else {
                    //other player quit
                    if(otherPlayerQuit) {
                        wasGameStarted = false
                        //deletes game
                        CodeGameViewModel().removeGame(onlineGameId, databaseReference, 0) {
                            context.stopService(
                                Intent(
                                    context,
                                    OnlineGameService::class.java
                                )
                            )
                        }
                    } else {
                        //player quit
                        CodeGameViewModel().playerQuit(MyTurn, databaseReference, onlineGameId, 0, context)
                        context.stopService(
                            Intent(
                                context,
                                OnlineGameService::class.java
                            )
                        )
                    }
                    otherPlayerQuit = false
                    wasGameStarted = false
                }
                navController.navigateUp()
            }) {
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