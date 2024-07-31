package com.idos.tictactoe.ui.Screen.Game.Online

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.idos.tictactoe.R
import com.idos.tictactoe.data.Boxes
import com.idos.tictactoe.data.GetO
import com.idos.tictactoe.data.GetX
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.Screen.Menu.getPlayer
import kotlinx.coroutines.delay

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
    if(onlineGameId != "") {

        if (!wasGameStarted) {
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
            if (otherPlayerQuit) {
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
    databaseReference: DatabaseReference,
    modifier: Modifier
) {
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
        }

        override fun onCancelled(error: DatabaseError) {}
    })

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
                enabled = (box == "") && enableState.enable && gameState.game.playerTurn == MyTurn && wasGameStarted,
            ),
        border = BorderStroke(1.dp, colors.onPrimary),
        colors = CardDefaults.cardColors(containerColor = colors.primary)
    ) {
        Image(
            painter = painterResource(
                id = when (box) {
                    "X" -> GetX(gameState.player1.currentX)
                    "O" -> GetO(gameState.player2.currentO)
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
    player: String,
    databaseReference: DatabaseReference,
    viewModel: CodeGameViewModel,
    navController: NavController,
    enableState: Enable,
    gameState: OnlineGameRememberedValues,
    modifier: Modifier,
    dbName: String,
    context: Context = LocalContext.current,
    lastScore: Int
) {

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
    var gameFinished by remember {
        mutableStateOf(false)
    }
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
                    .weight(1f)
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 2,
                setBox = {boxes.Box2 = it},
                box = boxes.Box2,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f)
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 3,
                setBox = {boxes.Box3 = it},
                box = boxes.Box3,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f)
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
                    .weight(1f)
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 5,
                setBox = {boxes.Box5 = it},
                box = boxes.Box5,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f)
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 6,
                setBox = {boxes.Box6 = it},
                box = boxes.Box6,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f)
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
                    .weight(1f)
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 8,
                setBox = {boxes.Box8 = it},
                box = boxes.Box8,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f)
            )
            OnlineGameButton(
                gameState = gameState,
                boxNumber = 9,
                setBox = {boxes.Box9 = it},
                box = boxes.Box9,
                enableState = enableState,
                databaseReference = databaseReference,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }

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

            if (!startedCountDown) {
                foundWinner = gameState.game.foundWinner
                times = gameState.game.times
            }
            gameFinished = (gameState.game.player1Score >= 2) || (gameState.game.player2Score >= 2)
            boxes = gameState.game.boxes
        }

        override fun onCancelled(error: DatabaseError) {}
    })

    //if can be a winner
    if(gameState.game.times >= 5 && wasGameStarted) {
        if (findGame(gameId = onlineGameId, databaseReference = databaseReference) != OnlineGameUiState()) {
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
                if (MyTurn == "X") {
                    //won
                    viewModel.updateFinalScoreScreenData("You won", gameState.game, gameState.player1,  gameState.player2)
                    gameState.FinalScoreText = "You won"
                    if(dbName == "Games") {
                        UpdateScore(
                            playerName = player,
                            addedScore = 1,
                            context = context,
                            gameState = gameState,
                            navController = navController,
                            codeGameViewModel = viewModel,
                            lastScore = lastScore
                        )
                    } else if(dbName == "GamesWithCode") {
                        GameScoreDialogFriendly(
                            gameState = gameState,
                            navController = navController,
                            context = context
                        )
                    }
                }
                else if (MyTurn == "O") {
                    //lose
                    viewModel.updateFinalScoreScreenData("You lost", gameState.game, gameState.player1,  gameState.player2)
                    gameState.FinalScoreText = "You lost"

                    if (dbName == "Games") {
                        UpdateScore(
                            playerName = player,
                            addedScore = -1,
                            context = context,
                            gameState = gameState,
                            navController = navController,
                            codeGameViewModel = viewModel,
                            lastScore = lastScore
                        )
                    } else if(dbName == "GamesWithCode") {
                        GameScoreDialogFriendly(
                            gameState = gameState,
                            navController = navController,
                            context = context
                        )
                    }
                }
                // show score screen
            } else if (gameState.game.player2Score == 2) {
                if (MyTurn == "O") {
                    //won
                    viewModel.updateFinalScoreScreenData("You won", gameState.game, gameState.player1,  gameState.player2)
                    gameState.FinalScoreText = "You won"
                    if (dbName == "Games") {
                        UpdateScore(
                            playerName = player,
                            addedScore = 1,
                            context = context,
                            gameState = gameState,
                            navController = navController,
                            codeGameViewModel = viewModel,
                            lastScore = lastScore
                        )
                    } else if(dbName == "GamesWithCode") {
                        GameScoreDialogFriendly(
                            gameState = gameState,
                            navController = navController,
                            context = context
                        )
                    }
                }
                else if (MyTurn == "X") {
                    //lose
                    viewModel.updateFinalScoreScreenData("You lost", gameState.game, gameState.player1,  gameState.player2)
                    gameState.FinalScoreText = "You lost"
                    if (dbName == "Games") {
                        UpdateScore(
                            playerName = player,
                            addedScore = -1,
                            context = context,
                            gameState = gameState,
                            navController = navController,
                            codeGameViewModel = viewModel,
                            lastScore = lastScore
                        )
                    } else if(dbName == "GamesWithCode") {
                        GameScoreDialogFriendly(
                            gameState = gameState,
                            navController = navController,
                            context = context
                        )
                    }
                }
                // show score screen
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
        }
    } else if(wasGameStarted) {
        if (findGame(gameId = onlineGameId, databaseReference =databaseReference) != OnlineGameUiState()) {
            databaseReference.child(onlineGameId).child("winner").setValue("")
        }
    }

    //show countDown dialog for winning
    if (foundWinner && !gameFinished) {
        startedCountDown = true
        gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
        gameState.player1 = getPlayer(email = gameState.game.player1)
        gameState.player2 = getPlayer(email = gameState.game.player2)

        NextRoundDialog(
            gameState = gameState,
            onZeroSecs = {
                times = 0
                foundWinner = false
                boxes = Boxes()
                startedCountDown = false
                ResetGame(
                    game = gameState.game,
                    databaseReference = databaseReference
                )
                viewModel.changeEnable(true)
            }
        )
    }

    //show countDown dialog for tie
    if (times == 9 && !foundWinner && !gameFinished) {
        startedCountDown = true
        gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
        gameState.player1 = getPlayer(email = gameState.game.player1)
        gameState.player2 = getPlayer(email = gameState.game.player2)

        NextRoundDialog(
            gameState = gameState,
            onZeroSecs = {
                times = 0
                foundWinner = false
                boxes = Boxes()
                startedCountDown = false
                ResetGame(
                    game = gameState.game,
                    databaseReference = databaseReference
                )
                viewModel.changeEnable(true)
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
fun findGame(gameId: String, databaseReference: DatabaseReference): OnlineGameUiState {
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
    dbName: String
) {
    currentGame.player1 = getPlayer(email = currentGame.game.player1)
    currentGame.player2 = getPlayer(email = currentGame.game.player2)

    var changeScore by remember {
        mutableStateOf(true)
    }
    var lastScore by remember {
        mutableIntStateOf(0)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference(dbName)

    //players database
    val playersDataBase = firebaseDatabase.getReference("Players")
    //get Players collection from database
    playersDataBase.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            val list = snapshot.children
            try {
                if (changeScore) {
                    changeScore = false

                    val playerUiState = list.find {
                        it.getValue(MainPlayerUiState::class.java)!!.email == player
                    }?.getValue(MainPlayerUiState::class.java)!!

                    lastScore = playerUiState.score

                    if (lastScore == 0) {
                        playersDataBase.child(playerUiState.key).child("score").setValue(0)
                    } else {
                        playersDataBase.child(playerUiState.key).child("score")
                            .setValue(lastScore - 1)
                    }
                }

            } catch (_: Exception) {
            }

        }

        override fun onCancelled(error: DatabaseError) {
        }
    })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(brush)
            .fillMaxSize()) {
        PlayersBar(
            modifier = Modifier.weight(2f),
            screenWidth = screenWidth.value.toInt(),
            colors = colors,
            gameState = currentGame,
            databaseReference = databaseReference
        )
        OnlineButtonGrid(
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
            dbName = dbName,
            lastScore = lastScore
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun UpdateScore(
    gameState: OnlineGameRememberedValues,
    navController: NavController,
    codeGameViewModel: CodeGameViewModel,
    playerName: String,
    context: Context,
    addedScore: Int,
    lastScore: Int
) {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var updatedPlayer by remember {
        mutableStateOf(MainPlayerUiState())
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
    var updateScore by remember {
        mutableStateOf(false)
    }
    var levelStatus by remember {
        mutableStateOf(LevelStatus.SameLevel)
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Players")

    if(!showScore) {

        //get Players collection from database
        databaseReference.addValueEventListener(object : ValueEventListener {
            //on success
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                try {
                    player = list.find {
                        it.getValue(MainPlayerUiState::class.java)!!.email == playerName
                    }?.getValue(MainPlayerUiState::class.java)!!

                    player.score = lastScore

                    val score =
                        if (player.score + addedScore < 0) 0 else player.score + addedScore
                    val newLevel = calculateNewLevel(score = score, currentLevel = player.level)

                    if (addedScore == 1) {
                        if (newLevel > player.level) {
                            levelStatus = LevelStatus.LevelUp
                            coins = ((10 + newLevel * 2)..(20 + newLevel * 2)).random()
                            updatedPlayer = player.copy(
                                score = score,
                                wins = player.wins + 1,
                                level = newLevel,
                                coins = player.coins + coins
                            )
                            showScore = true
                        } else {
                            levelStatus = LevelStatus.SameLevel
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
                        if (newLevel < player.level) {
                            levelStatus = LevelStatus.LevelDown
                            updatedPlayer = player.copy(
                                score = score,
                                wins = player.loses + 1,
                                level = newLevel,
                            )
                            showScore = true
                        } else {
                            levelStatus = LevelStatus.SameLevel
                            updatedPlayer = player.copy(
                                score = score,
                                wins = player.loses + 1,
                            )
                            showScore = true
                        }
                        showScore = true
                    }
                    if(!updateScore) {
                        updateScore = true
                        databaseReference.child(player.key).setValue(updatedPlayer)
                    }
                } catch (_: Exception) { failed = true }
            }

            override fun onCancelled(error: DatabaseError) { failed = true }
        })
    }

    if (failed) {
        UpdateScore(
            gameState = gameState,
            navController = navController,
            codeGameViewModel = codeGameViewModel,
            playerName = playerName,
            context = context,
            addedScore = addedScore,
            lastScore = lastScore
        )
        failed = false
    } else if(showScore) {
        GameScoreDialog(
            gameState = gameState,
            navController = navController,
            context = context,
            coins = coins,
            levelStatus = levelStatus,
            playerUiState = updatedPlayer,
            won = addedScore == 1
        )
    }
}

fun calculateNewLevel(
    score: Int,
    currentLevel: Int
): Int {
    var newLevel = 0

    when (score) {
        //level up
        25 -> if (currentLevel == 1) {
            newLevel =  2
        }

        50 -> if (currentLevel == 2) {
            newLevel = 3
        }

        100 -> if (currentLevel == 3) {
            newLevel = 4
        }

        150 -> if (currentLevel == 4) {
            newLevel = 5
        }

        200 -> if (currentLevel == 5) {
            newLevel = 6
        }

        300 -> if (currentLevel == 6) {
            newLevel = 7
        }

        400 -> if (currentLevel == 7) {
            newLevel = 8
        }

        500 -> if (currentLevel == 8) {
            newLevel = 9
        }

        600 -> if (currentLevel == 9) {
            newLevel = 10
        }

        700 -> if (currentLevel == 10) {
            newLevel = 11
        }

        800 -> if (currentLevel == 11) {
            newLevel = 12
        }

        900 -> if (currentLevel == 12) {
            newLevel = 13
        }

        1000 -> if (currentLevel == 13) {
            newLevel = 14
        }

        1200 -> if (currentLevel == 14) {
            newLevel = 15
        }

        //level down
        24 -> if (currentLevel == 2) {
            newLevel =  1
        }

        49 -> if (currentLevel == 3) {
            newLevel = 2
        }

        99 -> if (currentLevel == 4) {
            newLevel = 3
        }

        149 -> if (currentLevel == 5) {
            newLevel = 4
        }

        199 -> if (currentLevel == 6) {
            newLevel = 5
        }

        299 -> if (currentLevel == 7) {
            newLevel = 6
        }

        399 -> if (currentLevel == 8) {
            newLevel = 7
        }

        499 -> if (currentLevel == 9) {
            newLevel = 8
        }

        599 -> if (currentLevel == 10) {
            newLevel = 9
        }

        699 -> if (currentLevel == 11) {
            newLevel = 10
        }

        799 -> if (currentLevel == 12) {
            newLevel = 11
        }

        899 -> if (currentLevel == 13) {
            newLevel = 12
        }

        999 -> if (currentLevel == 14) {
            newLevel = 13
        }

        1199 -> if (currentLevel == 15) {
            newLevel = 14
        }
        else -> newLevel = currentLevel
    }

    return newLevel
}

@Composable
fun CheckExitGame(
    onQuitClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancelClick,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .padding(((LocalConfiguration.current.screenWidthDp * 16 / 412).dp))
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                Text(
                    text = "Are you sure you want to quit the game",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "?",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height((LocalConfiguration.current.screenWidthDp * 30 / 412).dp))
            Row(
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                Button(
                    onClick = onCancelClick,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.width((LocalConfiguration.current.screenWidthDp*0.8*0.8*0.4).dp)
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(
                    modifier = Modifier.width((LocalConfiguration.current.screenWidthDp*0.8*0.8*0.1).dp)
                )

                Button(
                    onClick = {
                    onQuitClick()
                    onCancelClick()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.width((LocalConfiguration.current.screenWidthDp*0.8*0.8*0.4).dp)
                ) {
                    Text(
                        text = "Quit",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}