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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.Screen.GameScreen
import com.idos.tictactoe.ui.Screen.getPlayer
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Secondery
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

private var wasGameStarted = false
private var onlineGameId: String = ""

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

        if(!wasGameStarted) {
            CodeGameViewModel().removeGame(onlineGameId, databaseReference, 0) {
                stopService(
                    Intent(
                        baseContext,
                        OnlineGameService::class.java
                    )
                )
            }
        }
    }
}

@Composable
fun SetBoxOnline(
    game: OnlineGameUiState,
    boxNumber: Int,
    playerTurn: String,
    databaseReference: DatabaseReference
) {

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


    var SetBox by remember {
        mutableStateOf(false)
    }

    Card(modifier = modifier,shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        Image(
            painter = painterResource(
                id = when (box) {
                    "X" -> GetX( player1.currentX )
                    "O" -> GetO( player2.currentO )
                    else -> {R.drawable.o_1}
                }
            ),
            contentDescription = null,
            alpha = if (box != "") DefaultAlpha else 0f,
            modifier = Modifier
                .background(Color.Gray)
                .size(100.dp)
                .clickable(
                    onClick = {
                        enableState.enable = false
                        SetBox = true
                    },
                    enabled = (box == "") && enableState.enable && gameState.game.playerTurn == myTurn && wasGameStarted,
                )
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

    wasGameStarted = gameStarted
    onlineGameId = gameId

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

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val size = (screenWidth/19)*5

    val scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(0.5f))
        Row(modifier = Modifier.weight(5f)) {
            Spacer(modifier = Modifier.weight(1f))
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 1,
                setBox = {boxes.Box1 = it},
                box = boxes.Box1,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(5f)
                    .size(size),
                myTurn = myTurn
            )
            Spacer(modifier = Modifier.weight(1f))
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 2,
                setBox = {boxes.Box2 = it},
                box = boxes.Box2,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(5f)
                    .size(size),
                myTurn = myTurn
            )
            Spacer(modifier = Modifier.weight(1f))
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 3,
                setBox = {boxes.Box3 = it},
                box = boxes.Box3,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(5f)
                    .size(size),
                myTurn = myTurn
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Row(modifier = Modifier.weight(5f)) {
            Spacer(modifier = Modifier.weight(1f))
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 4,
                setBox = {boxes.Box4 = it},
                box = boxes.Box4,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(5f)
                    .size(size),
                myTurn = myTurn
            )
            Spacer(modifier = Modifier.weight(1f))
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 5,
                setBox = {boxes.Box5 = it},
                box = boxes.Box5,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(5f)
                    .size(size),
                myTurn = myTurn
            )
            Spacer(modifier = Modifier.weight(1f))
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 6,
                setBox = {boxes.Box6 = it},
                box = boxes.Box6,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(5f)
                    .size(size),
                myTurn = myTurn
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Row(modifier = Modifier.weight(5f)) {
            Spacer(modifier = Modifier.weight(1f))
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 7,
                setBox = {boxes.Box7 = it},
                box = boxes.Box7,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(5f)
                    .size(size),
                myTurn = myTurn
            )
            Spacer(modifier = Modifier.weight(1f))
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 8,
                setBox = {boxes.Box8 = it},
                box = boxes.Box8,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(5f)
                    .size(size),
                myTurn = myTurn
            )
            Spacer(modifier = Modifier.weight(1f))
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 9,
                setBox = {boxes.Box9 = it},
                box = boxes.Box9,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(5f)
                    .size(size),
                myTurn = myTurn
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
    boxes = gameState.game.boxes
    //if can be a winner
    if(gameState.game.times >= 5) {
        databaseReference.child(onlineGameId).child("winner").setValue(CheckOnlineWinner(boxes))
        //if has winner
        if(gameState.game.winner.isNotEmpty()) {
            //if no one found the winner
            if (!gameState.game.foundWinner) {
                databaseReference.child(onlineGameId).child("foundWinner").setValue(true)
                //update game score
                if (gameState.game.winner == "X") {
                    viewModel.changeEnable(false)
                    databaseReference.child(onlineGameId).child("player1Score").setValue(gameState.game.player1Score.plus(1))
                    databaseReference.child(onlineGameId).child("rounds").setValue(gameState.game.rounds.plus(1))
                }
                if ((gameState.game.winner == "O")) {
                    viewModel.changeEnable(false)
                    databaseReference.child(onlineGameId).child("player2Score").setValue(gameState.game.player2Score.plus(1))
                    databaseReference.child(onlineGameId).child("rounds").setValue(gameState.game.rounds.plus(1))
                }
            }

            // if has final winner
            if (gameState.game.player1Score == 2) {
                if (myTurn == "X") {
                    //show winner
                    //won
                    updateScore(playerName = player, addedScore = 1, context = LocalContext.current)
                    viewModel.updateFinalScoreScreenData("You won!", gameState.game, gameState.player1,  gameState.player2)
                } else if (myTurn == "O") {
                    //show winner
                    //lose
                    updateScore(playerName = player, addedScore = -1, context = LocalContext.current)
                    viewModel.updateFinalScoreScreenData("You lose", gameState.game, gameState.player1,  gameState.player2)
                }
                navController.navigate(GameScreen.ShowGameFinalScore.title)
            } else if (gameState.game.player2Score == 2) {
                if (myTurn == "O") {
                    //show winner
                    //won
                    updateScore(playerName = player, addedScore = 1, context = LocalContext.current)
                    viewModel.updateFinalScoreScreenData("You won!", gameState.game, gameState.player1,  gameState.player2)
                } else if (myTurn == "X") {
                    //show winner
                    //lose
                    updateScore(playerName = player, addedScore = -1, context = LocalContext.current)
                    viewModel.updateFinalScoreScreenData("You lose", gameState.game, gameState.player1,  gameState.player2)
                }
                // show score screen
                navController.navigate(GameScreen.ShowGameFinalScore.title)
            } else
                //new round
                gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
                boxes = gameState.game.boxes
                if (gameState.game.foundWinner && gameState.game.player1Score != 2 && gameState.game.player2Score != 2) {
                    scope.launch {
                        delay(3000)
                        if(gameState.game.boxes != Boxes()) {
                            ResetGame(
                                game = gameState.game,
                                databaseReference = databaseReference,
                                onFinish = {
                                    enableState.enable = true
                                }
                            )
                        }
                        viewModel.changeEnable(true)
                    }
            }
        }
        //tie
        else if (gameState.game.times == 9){
            //if didn't update round
            if (!gameState.game.editedRounds) {
                //update round
                databaseReference.child(onlineGameId).child("editedRounds").setValue(true)
                databaseReference.child(onlineGameId).child("rounds").setValue(gameState.game.rounds.plus(1))
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
                            databaseReference = databaseReference,
                            onFinish = {
                                enableState.enable = true
                            }
                        )
                    }
                }
            }
        }
    }


    if (!startedCountDown) {
        foundWinner = gameState.game.foundWinner
    }
    //show countDown dialog for winning
    if (foundWinner && (gameState.game.player1Score != 2) && (gameState.game.player2Score != 2)) {
        startedCountDown = true
        NextRoundDialog(
            game = findGame(gameId = onlineGameId, databaseReference = databaseReference),
            player1 = gameState.player1,
            player2 = gameState.player2,
            onZeroSecs = {
                foundWinner = false
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
        NextRoundDialog(
            game = findGame(gameId = onlineGameId, databaseReference = databaseReference),
            player1 = gameState.player1,
            player2 = gameState.player2,
            onZeroSecs = {
                times = 0
                startedCountDown = false
            }
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NextRoundDialog(game: OnlineGameUiState, player1: MainPlayerUiState, player2: MainPlayerUiState, onZeroSecs: () -> Unit) {
    val player1CurrentImage = GetXO(player1.currentImage)
    val player2CurrentImage = GetXO(player1.currentImage)

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

    Dialog(onDismissRequest = {}) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth(0.98f)
            .background(Color.White)) {
            Text(text = "Next round in: $secondsToNextRound", fontWeight = FontWeight.Bold, fontSize = 30.sp, color = Color.Black)
            Row() {
                Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(
                            shape = RoundedCornerShape(125.dp),
                            elevation = 10.dp,
                            modifier = Modifier
                                .size(90.dp)
                        ) {
                            Image(
                                painter = painterResource(id = player1CurrentImage),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(
                            text = player1.name,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = game.player1Score.toString(),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
                Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(
                            shape = RoundedCornerShape(125.dp),
                            elevation = 10.dp,
                            modifier = Modifier
                                .size(90.dp)
                        ) {
                            Image(
                                painter = painterResource(id = player2CurrentImage),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(
                            text = player2.name,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = game.player2Score.toString(),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
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

fun ResetGame(game: OnlineGameUiState, databaseReference: DatabaseReference, onFinish: () -> Unit) {
    databaseReference.child(onlineGameId).child("winner").setValue("")
    databaseReference.child(onlineGameId).child("boxes").setValue(Boxes())
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

@Composable
fun OnlineTicTacToe(
    player: String,
    context: Context,
    viewModel: CodeGameViewModel,
    navController: NavController,
    enableState: Enable
) {
    val currentGame by remember {
        mutableStateOf(OnlineGameRememberedValues())
    }

    var foundPlayer by remember {
        mutableStateOf(false)
    }
    var times by remember {
        mutableStateOf(1)
    }
    var myTurn by remember {
        mutableStateOf<String?>(null)
    }
    var waitingTimeFlag by remember {
        mutableStateOf(true)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val size = (screenWidth/10)*3

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")

    if(waitingTimeFlag) {
        waitingTimeFlag = false

        Timer().schedule(60000) {
            if(currentGame.player2.email == "" && navController.currentDestination?.route == GameScreen.Online.title) {
                viewModel.removeGame(onlineGameId, databaseReference, 0){
                    navController.navigate(GameScreen.TimeUp.title)
                }
            }
        }
    }

    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                Loop@ for (Game in snapshot.children) {
                    val game = Game.getValue(OnlineGameUiState::class.java)
                    //enter room
                    if ((game!!.player2 == "")) {
                        val updatedGame = OnlineGameUiState(
                            id = game.id,
                            player1 = game.player1,
                            player2 = player,
                            winner = game.winner,
                            boxes = game.boxes
                        )
                        onlineGameId = game.id
                        databaseReference.child(game.id).child("player2").setValue(player)
                        currentGame.game = updatedGame
                        foundPlayer = true
                        myTurn = "O"
                        break@Loop
                    }
                }
                //open room
                if (currentGame.game == OnlineGameUiState()) {
                    val key: String = databaseReference.push().key!!.takeLast(5)
                    val newGame = OnlineGameUiState(
                        id = key,
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = Boxes()
                    )
                    onlineGameId = key
                    databaseReference.child(key).setValue(newGame)
                    currentGame.game = newGame
                    myTurn = "X"
                }
                times++
            }
            for (Game in snapshot.children) {
                val game = Game.getValue(OnlineGameUiState::class.java)
                if (game!!.id == onlineGameId) {
                    currentGame.game = game
                    if (currentGame.game.player2 != "") {
                        foundPlayer = true
                    }
                }
            }
        }

        //on failure
        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(
                context,
                "Fail to get the data.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    )

    currentGame.player1 = getPlayer(email = currentGame.game.player1, context = context)
    currentGame.player2 = getPlayer(email = currentGame.game.player2, context = context)

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        playersBar(
            size = size,
            modifier = Modifier.weight(2f),
            gameId = onlineGameId,
            databaseReference = databaseReference
        )
        Spacer(modifier = Modifier.weight(1f))
        OnlineButtonGrid(
            myTurn = myTurn,
            gameStarted = foundPlayer,
            player = player,
            databaseReference = databaseReference,
            viewModel = viewModel,
            navController = navController,
            enableState = enableState,
            gameState = currentGame,
            modifier = Modifier.weight(6f),
            onlineGameId
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun updateScore(playerName: String, context: Context, addedScore: Int) {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var score by remember {
        mutableStateOf(0)
    }
    var levelUp by remember {
        mutableStateOf(false)
    }
    var lockedPhotos by remember {
        mutableStateOf<List<String>>(listOf())
    }
    var unlockedPhotos by remember {
        mutableStateOf<List<String>>(listOf())
    }
    var updatedPlayer by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var newLevel by remember {
        mutableStateOf(0)
    }
    var lockedX by remember {
        mutableStateOf<List<String>>(listOf())
    }
    var lockedO by remember {
        mutableStateOf<List<String>>(listOf())
    }
    var unlockedX by remember {
        mutableStateOf<List<String>>(listOf())
    }
    var unlockedO by remember {
        mutableStateOf<List<String>>(listOf())
    }

    var failed by remember {
        mutableStateOf(false)
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
                Loop@ for (d in list) {
                    val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
                    //find player using database
                    if (p?.email == playerName){
                        player = p
                        score = if (player.score + addedScore < 0) 0 else player.score + addedScore

                        if (addedScore == 1) {
                            when (score) {
                                25 -> if (player.level == 1) {
                                    lockedPhotos = player.lockedImages.minus("xo_2")
                                    unlockedPhotos = player.unlockedImages.plus("xo_2")
                                    lockedX = player.lockedX.minus("x_2")
                                    unlockedX = player.unlockedX.plus("x_2")
                                    lockedO = player.lockedO.minus("o_2")
                                    unlockedO = player.unlockedO.plus("o_2")
                                    newLevel = 2
                                    levelUp = true
                                }
                                50 -> if (player.level == 2) {
                                    lockedPhotos = player.lockedImages.minus("xo_3")
                                    unlockedPhotos = player.unlockedImages.plus("xo_3")
                                    lockedX = player.lockedX.minus("x_3")
                                    unlockedX = player.unlockedX.plus("x_3")
                                    lockedO = player.lockedO.minus("o_3")
                                    unlockedO = player.unlockedO.plus("o_3")
                                    newLevel = 3
                                    levelUp = true
                                }
                                100 -> if (player.level == 3) {
                                    lockedPhotos = player.lockedImages.minus("xo_4")
                                    unlockedPhotos = player.unlockedImages.plus("xo_4")
                                    lockedX = player.lockedX.minus("x_4")
                                    unlockedX = player.unlockedX.plus("x_4")
                                    lockedO = player.lockedO.minus("o_4")
                                    unlockedO = player.unlockedO.plus("o_4")
                                    newLevel = 4
                                    levelUp = true
                                }
                                150 -> if (player.level == 4) {
                                    lockedPhotos = player.lockedImages.minus("xo_5_1") - "xo_5_2"
                                    unlockedPhotos = player.unlockedImages.plus("xo_5_1") + "xo_5_2"
                                    lockedX = player.lockedX.minus("x_5")
                                    unlockedX = player.unlockedX.plus("x_5")
                                    lockedO = player.lockedO.minus("o_5")
                                    unlockedO = player.unlockedO.plus("o_5")
                                    newLevel = 5
                                    levelUp = true
                                }
                                200 -> if (player.level == 5) {
                                    lockedPhotos = player.lockedImages.minus("xo_6")
                                    unlockedPhotos = player.unlockedImages.plus("xo_6")
                                    lockedX = player.lockedX.minus("x_6")
                                    unlockedX = player.unlockedX.plus("x_6")
                                    lockedO = player.lockedO.minus("o_6")
                                    unlockedO = player.unlockedO.plus("o_6")
                                    newLevel = 6
                                    levelUp = true
                                }
                                300 -> if (player.level == 6) {
                                    lockedPhotos = player.lockedImages.minus("xo_7")
                                    unlockedPhotos = player.unlockedImages.plus("xo_7")
                                    lockedX = player.lockedX.minus("x_7")
                                    unlockedX = player.unlockedX.plus("x_7")
                                    lockedO = player.lockedO.minus("o_7")
                                    unlockedO = player.unlockedO.plus("o_7")
                                    newLevel = 7
                                    levelUp = true
                                }
                                400 -> if (player.level == 7) {
                                    lockedPhotos = player.lockedImages.minus("xo_8")
                                    unlockedPhotos = player.unlockedImages.plus("xo_8")
                                    lockedX = player.lockedX.minus("x_8")
                                    unlockedX = player.unlockedX.plus("x_8")
                                    lockedO = player.lockedO.minus("o_8")
                                    unlockedO = player.unlockedO.plus("o_8")
                                    newLevel = 8
                                    levelUp = true
                                }
                                500 -> if (player.level == 8) {
                                    lockedPhotos = player.lockedImages.minus("xo_9")
                                    unlockedPhotos = player.unlockedImages.plus("xo_9")
                                    lockedX = player.lockedX.minus("x_9")
                                    unlockedX = player.unlockedX.plus("x_9")
                                    lockedO = player.lockedO.minus("o_9")
                                    unlockedO = player.unlockedO.plus("o_9")
                                    newLevel = 9
                                    levelUp = true
                                }
                                600 -> if (player.level == 9) {
                                    lockedPhotos = player.lockedImages.minus("xo_10_1") - "xo_10_2"
                                    unlockedPhotos = player.unlockedImages.plus("xo_10_1") + "xo_10_2"
                                    lockedX = player.lockedX.minus("x_10")
                                    unlockedX = player.unlockedX.plus("x_10")
                                    lockedO = player.lockedO.minus("o_10")
                                    unlockedO = player.unlockedO.plus("o_10")
                                    newLevel = 10
                                    levelUp = true
                                }
                                700 -> if (player.level == 10) {
                                    lockedPhotos = player.lockedImages.minus("xo_11")
                                    unlockedPhotos = player.unlockedImages.plus("xo_11")
                                    lockedX = player.lockedX.minus("x_11")
                                    unlockedX = player.unlockedX.plus("x_11")
                                    lockedO = player.lockedO.minus("o_11")
                                    unlockedO = player.unlockedO.plus("o_11")
                                    newLevel = 11
                                    levelUp = true
                                }
                                800 -> if (player.level == 11) {
                                    lockedPhotos = player.lockedImages.minus("xo_12")
                                    unlockedPhotos = player.unlockedImages.plus("xo_12")
                                    lockedX = player.lockedX.minus("x_12")
                                    unlockedX = player.unlockedX.plus("x_12")
                                    lockedO = player.lockedO.minus("o_12")
                                    unlockedO = player.unlockedO.plus("o_12")
                                    newLevel = 12
                                    levelUp = true
                                }
                                900 -> if (player.level == 12) {
                                    lockedPhotos = player.lockedImages.minus("xo_13")
                                    unlockedPhotos = player.unlockedImages.plus("xo_13")
                                    lockedX = player.lockedX.minus("x_13")
                                    unlockedX = player.unlockedX.plus("x_13")
                                    lockedO = player.lockedO.minus("o_13")
                                    unlockedO = player.unlockedO.plus("o_13")
                                    newLevel = 13
                                    levelUp = true
                                }
                                1000 -> if (player.level == 13) {
                                    lockedPhotos = player.lockedImages.minus("xo_14")
                                    unlockedPhotos = player.unlockedImages.plus("xo_14")
                                    lockedX = player.lockedX.minus("x_14")
                                    unlockedX = player.unlockedX.plus("x_14")
                                    lockedO = player.lockedO.minus("o_14")
                                    unlockedO = player.unlockedO.plus("o_14")
                                    newLevel = 14
                                    levelUp = true
                                }
                                1200 -> if (player.level == 14) {
                                    lockedPhotos = player.lockedImages.minus("xo_15_1") - "xo_15_2"
                                    unlockedPhotos = player.unlockedImages.plus("xo_15_1") + "xo_15_2"
                                    lockedX = player.lockedX.minus("x_15")
                                    unlockedX = player.unlockedX.plus("x_15")
                                    lockedO = player.lockedO.minus("o_15")
                                    unlockedO = player.unlockedO.plus("o_15")
                                    newLevel = 15
                                    levelUp = true
                                }

                            }
                            if (levelUp) {
                                updatedPlayer = MainPlayerUiState(
                                    name = player.name,
                                    email = player.email,
                                    score = score,
                                    currentImage = player.currentImage,
                                    unlockedImages = unlockedPhotos,
                                    lockedImages = lockedPhotos,
                                    currentX = player.currentX,
                                    currentO = player.currentO,
                                    lockedX = lockedX,
                                    lockedO = lockedO,
                                    unlockedX = unlockedX,
                                    unlockedO = unlockedO,
                                    wins = player.wins + 1,
                                    loses = player.loses,
                                    level = newLevel
                                )
                            } else {
                                updatedPlayer = player.copy(score = score, wins = player.wins + 1)
                            }
                        } else if (addedScore == -1) {
                            updatedPlayer = player.copy(score = score, loses = player.loses + 1)

                        }

                        db.collection("Players")
                            .whereEqualTo("email", playerName)
                            .get()
                            .addOnSuccessListener {
                                for (document in it) {
                                    db.collection("Players").document(document.id).set(updatedPlayer,
                                        SetOptions.merge())
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

    if (failed) {
        updateScore(playerName = playerName, context = context, addedScore = addedScore)
        failed = false
    }
}

@Composable
fun CheckExitOnlineGame(onQuitClick: () -> Unit, onCancelClick: () -> Unit, navController: NavController) {

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
                        navController.navigateUp()
                    }
                } else {
                    navController.navigateUp()
                }
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