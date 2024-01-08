package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.tictactoe.ui.Screen.CheckOnlineWinner
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.idos.tictactoe.R
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Primery
import com.idos.tictactoe.ui.theme.Secondery
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SetBoxOnline(
    game: OnlineGameUiState,
    boxNumber: Int,
    playerTurn: String,
    databaseReference: DatabaseReference,
    gameId: String
) {

    val boxes: com.idos.tictactoe.data.Boxes? = when(boxNumber) {
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

    databaseReference.child(gameId).child("boxes").setValue(boxes)
    databaseReference.child(gameId).child("times").setValue(game.times.plus(1))
}

@Composable
fun changePlayerTurn(game: OnlineGameUiState, databaseReference: DatabaseReference, gameId: String) {
    val PlayerTurn = if(game.playerTurn == "X") {
        "O"
    } else {
        "X"
    }

    databaseReference.child(gameId).child("playerTurn").setValue(PlayerTurn)
}

@Composable
fun OnlineGameButton(
    game: OnlineGameUiState,
    boxNumber: Int,
    box: String,
    enabled: Boolean,
    context: Context = LocalContext.current,
    databaseReference: DatabaseReference,
    FindGame: (OnlineGameUiState) -> Unit,
    gameId: String,
    enableClick: (Boolean) -> Unit

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


    var setBox by remember {
        mutableStateOf(false)
    }

    Card(modifier = Modifier.padding(8.dp),shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        Image(
            painter = painterResource(
                id = when (box) {
                    "X" -> player1.currentX
                    "O" -> player2.currentO
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
                        enableClick(false)
                        setBox = true
                    },
                    enabled = (box == "") && enabled,
                )
        )
    }

    if (setBox) {
        changePlayerTurn(game, databaseReference = databaseReference, gameId)
        SetBoxOnline(
            game = game,
            boxNumber = boxNumber,
            playerTurn = game.playerTurn,
            databaseReference = databaseReference,
            gameId
        )
        FindGame(findGame(gameId = gameId, databaseReference = databaseReference))
        enableClick(true)
        setBox = false
    }
}

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition",
    "SuspiciousIndentation"
)
@Composable
fun OnlineButtonGrid(gameId: String, myTurn: String?, gameStarted: Boolean, player: String, player1: MainPlayerUiState, player2: MainPlayerUiState, databaseReference: DatabaseReference, viewModel: CodeGameViewModel, navController: NavController, enableState: Enable) {
    var game by remember {
        mutableStateOf(OnlineGameUiState())
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

    val scope = rememberCoroutineScope()

    Column {
        Row {
            OnlineGameButton(game = game, boxNumber = 1, box = game.boxes.Box1, enabled = enableState.enable && myTurn == game.playerTurn  && gameStarted, databaseReference = databaseReference, FindGame = {game = it}, gameId = gameId) {
                viewModel.changeEnable(it)
            }
            OnlineGameButton(game = game, boxNumber = 2, box = game.boxes.Box2, enabled = enableState.enable && myTurn == game.playerTurn  && gameStarted, databaseReference = databaseReference, FindGame = {game = it}, gameId = gameId) {
                viewModel.changeEnable(it)
            }
            OnlineGameButton(game = game, boxNumber = 3, box = game.boxes.Box3, enabled = enableState.enable && myTurn == game.playerTurn  && gameStarted, databaseReference = databaseReference, FindGame = {game = it}, gameId = gameId) {
                viewModel.changeEnable(it)
            }
        }
        Row {
            OnlineGameButton(game = game, boxNumber = 4, box = game.boxes.Box4, enabled = enableState.enable && myTurn == game.playerTurn  && gameStarted, databaseReference = databaseReference, FindGame = {game = it}, gameId = gameId) {
                viewModel.changeEnable(it)
            }
            OnlineGameButton(game = game, boxNumber = 5, box = game.boxes.Box5, enabled = enableState.enable && myTurn == game.playerTurn  && gameStarted, databaseReference = databaseReference, FindGame = {game = it}, gameId = gameId) {
                viewModel.changeEnable(it)
            }
            OnlineGameButton(game = game, boxNumber = 6, box = game.boxes.Box6, enabled = enableState.enable && myTurn == game.playerTurn  && gameStarted, databaseReference = databaseReference, FindGame = {game = it}, gameId = gameId) {
                viewModel.changeEnable(it)
            }
        }
        Row {
            OnlineGameButton(game = game, boxNumber = 7, box = game.boxes.Box7, enabled = enableState.enable && myTurn == game.playerTurn  && gameStarted, databaseReference = databaseReference, FindGame = {game = it}, gameId = gameId) {
                viewModel.changeEnable(it)
            }
            OnlineGameButton(game = game, boxNumber = 8, box = game.boxes.Box8, enabled = enableState.enable && myTurn == game.playerTurn  && gameStarted, databaseReference = databaseReference, FindGame = {game = it}, gameId = gameId) {
                viewModel.changeEnable(it)
            }
            OnlineGameButton(game = game, boxNumber = 9, box = game.boxes.Box9, enabled = enableState.enable && myTurn == game.playerTurn  && gameStarted, databaseReference = databaseReference, FindGame = {game = it}, gameId = gameId) {
                viewModel.changeEnable(it)
            }
        }
    }

    game = findGame(gameId = gameId, databaseReference = databaseReference)
    //if can be a winner
    if(game.times >= 5) {
        databaseReference.child(gameId).child("winner").setValue(CheckOnlineWinner(game.boxes))
        //if has winner
        if(game.winner.isNotEmpty()) {
            //if no one found the winner
            if (!game.foundWinner) {
                databaseReference.child(gameId).child("foundWinner").setValue(true)
                //update game score
                if (game.winner == "X") {
                    viewModel.changeEnable(false)
                    databaseReference.child(gameId).child("player1Score").setValue(game.player1Score.plus(1))
                    databaseReference.child(gameId).child("rounds").setValue(game.rounds.plus(1))
                }
                if ((game.winner == "O")) {
                    viewModel.changeEnable(false)
                    databaseReference.child(gameId).child("player2Score").setValue(game.player2Score.plus(1))
                    databaseReference.child(gameId).child("rounds").setValue(game.rounds.plus(1))
                }
            }

            // if has final winner
            if (game.player1Score == 2) {
                if (myTurn == "X") {
                    //show winner
                    //won
                    updateScore(playerName = player, addedScore = 1, context = LocalContext.current)
                    viewModel.updateFinalScoreScreenData("You won!", game, player1,  player2)
                } else if (myTurn == "O") {
                    //show winner
                    //lose
                    updateScore(playerName = player, addedScore = -1, context = LocalContext.current)
                    viewModel.updateFinalScoreScreenData("You lose", game, player1,  player2)
                }
                navController.navigate(GameScreen.ShowGameFinalScore.title)
            } else if (game.player2Score == 2) {
                if (myTurn == "O") {
                    //show winner
                    //won
                    updateScore(playerName = player, addedScore = 1, context = LocalContext.current)
                    viewModel.updateFinalScoreScreenData("You won!", game, player1,  player2)
                } else if (myTurn == "X") {
                    //show winner
                    //lose
                    updateScore(playerName = player, addedScore = -1, context = LocalContext.current)
                    viewModel.updateFinalScoreScreenData("You lose", game, player1,  player2)
                }
                // show score screen
                navController.navigate(GameScreen.ShowGameFinalScore.title)
            } else
                //new round
                game = findGame(gameId = gameId, databaseReference = databaseReference)
                if (game.foundWinner && game.player1Score != 2 && game.player2Score != 2) {
                    scope.launch {
                        delay(3000)
                        ResetGame(game = game, databaseReference = databaseReference, gameId)
                        viewModel.changeEnable(true)
                    }
            }
        }
        //tie
        else if (game.times == 9){
            //if didn't update round
            if (!game.editedRounds) {
                //update round
                databaseReference.child(gameId).child("editedRounds").setValue(true)
                databaseReference.child(gameId).child("rounds").setValue(game.rounds.plus(1))
            }
            game = findGame(gameId = gameId, databaseReference = databaseReference)
            //reset round
            if (game.player1Score != 2 && game.player2Score != 2) {
                scope.launch {
                    delay(3000)
                    ResetGame(game = game, databaseReference = databaseReference, gameId)
                    viewModel.changeEnable(true)
                }
            }
        }
    }


    if (!startedCountDown) {
        foundWinner = game.foundWinner
    }
    //show countDown dialog for winning
    if (foundWinner && (game.player1Score != 2) && (game.player2Score != 2)) {
        startedCountDown = true
        NextRoundDialog(
            game = findGame(gameId = gameId, databaseReference = databaseReference),
            player1 = player1,
            player2 = player2,
            onZeroSecs = {
                foundWinner = false
                startedCountDown = false
            }
        )
    }
    if (!startedCountDown) {
        times = game.times
    }
    //show countDown dialog for tie
    if ((times == 9) && (game.player1Score != 2) && (game.player2Score != 2)) {
        startedCountDown = true
        NextRoundDialog(
            game = findGame(gameId = gameId, databaseReference = databaseReference),
            player1 = player1,
            player2 = player2,
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
                                painter = painterResource(id = player1.currentImage),
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
                                painter = painterResource(id = player2.currentImage),
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
            try {
                game = list.find {
                    it.getValue(OnlineGameUiState::class.java)!!.id == gameId
                }?.getValue(OnlineGameUiState::class.java)!!
            } catch (e: Exception) {
                game = OnlineGameUiState()
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

fun ResetGame(game: OnlineGameUiState, databaseReference: DatabaseReference, gameId: String) {
    databaseReference.child(gameId).child("winner").setValue("")
    databaseReference.child(gameId).child("boxes").setValue(com.idos.tictactoe.data.Boxes())
    databaseReference.child(gameId).child("times").setValue(0)
    databaseReference.child(gameId).child("foundWinner").setValue(false)
    databaseReference.child(gameId).child("editedRounds").setValue(false)
    val playerTurn = if ((game.rounds % 2) == 0) {
        "O"
    } else if ((game.rounds % 2) == 1) {
        "X"
    } else null
    databaseReference.child(gameId).child("playerTurn").setValue(playerTurn)
}

@Composable
fun OnlineTicTacToe(
    player: String,
    context: Context,
    viewModel: CodeGameViewModel,
    navController: NavController,
    enableState: Enable
) {
    var currentGame by remember {
        mutableStateOf(OnlineGameUiState())
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
    var player1 by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var player2 by remember {
        mutableStateOf(MainPlayerUiState())
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                Loop@ for (Game in snapshot.children) {
                    val game = Game.getValue(OnlineGameUiState::class.java)
                    if ((game!!.player2 == "")) {
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
                        currentGame = updatedGame
                        foundPlayer = true
                        myTurn = "O"
                        break@Loop
                    }
                }
                if (currentGame == OnlineGameUiState()) {
                    val key: String = databaseReference.push().key!!.takeLast(5)
                    val newGame = OnlineGameUiState(
                        id = key,
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = com.idos.tictactoe.data.Boxes()
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
                                    if (p?.email == player){
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
                    databaseReference.child(key).setValue(newGame)
                    currentGame = newGame
                    myTurn = "X"
                }
                times++
            }
            for (Game in snapshot.children) {
                val game = Game.getValue(OnlineGameUiState::class.java)
                if (game!!.id == currentGame.id) {
                    currentGame = game
                    if (currentGame.player2 != "") {
                        foundPlayer = true
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

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (player1 != MainPlayerUiState()) {
                    Card(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(20.dp),
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
                            painter = painterResource(id = player1.currentImage),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text(player1.name, fontSize = 10.sp, color = Color.White)
                } else {
                    Card(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(20.dp),
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

            Text("${currentGame.player1Score} : ${currentGame.player2Score}", fontWeight = FontWeight.Bold, color = Color.White)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (foundPlayer) {
                    Card(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(20.dp),
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
                            painter = painterResource(id = player2.currentImage),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text(player2.name, fontSize = 10.sp, color = Color.White)
                } else {
                    Card(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(20.dp),
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
        }
        Spacer(modifier = Modifier.weight(2f))
        OnlineButtonGrid(gameId = currentGame.id, myTurn = myTurn, gameStarted = foundPlayer, player = player, player1 = player1, player2 = player2, databaseReference = databaseReference, viewModel = viewModel, navController = navController, enableState = enableState)
        Spacer(modifier = Modifier.weight(4f))
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
        mutableStateOf<List<Int>>(listOf())
    }
    var unlockedPhotos by remember {
        mutableStateOf<List<Int>>(listOf())
    }
    var updatedPlayer by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var newLevel by remember {
        mutableStateOf(0)
    }
    var lockedX by remember {
        mutableStateOf<List<Int>>(listOf())
    }
    var lockedO by remember {
        mutableStateOf<List<Int>>(listOf())
    }
    var unlockedX by remember {
        mutableStateOf<List<Int>>(listOf())
    }
    var unlockedO by remember {
        mutableStateOf<List<Int>>(listOf())
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
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_2)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_2)
                                    lockedX = player.lockedX.minus(R.drawable.x_2)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_2)
                                    lockedO = player.lockedO.minus(R.drawable.o_2)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_2)
                                    newLevel = 2
                                    levelUp = true
                                }
                                50 -> if (player.level == 2) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_3)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_3)
                                    lockedX = player.lockedX.minus(R.drawable.x_3)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_3)
                                    lockedO = player.lockedO.minus(R.drawable.o_3)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_3)
                                    newLevel = 3
                                    levelUp = true
                                }
                                100 -> if (player.level == 3) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_4)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_4)
                                    lockedX = player.lockedX.minus(R.drawable.x_4)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_4)
                                    lockedO = player.lockedO.minus(R.drawable.o_4)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_4)
                                    newLevel = 4
                                    levelUp = true
                                }
                                150 -> if (player.level == 4) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_5_1) - R.drawable.xo_5_2
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_5_1) + R.drawable.xo_5_2
                                    lockedX = player.lockedX.minus(R.drawable.x_5)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_5)
                                    lockedO = player.lockedO.minus(R.drawable.o_5)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_5)
                                    newLevel = 5
                                    levelUp = true
                                }
                                200 -> if (player.level == 5) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_6)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_6)
                                    lockedX = player.lockedX.minus(R.drawable.x_6)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_6)
                                    lockedO = player.lockedO.minus(R.drawable.o_6)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_6)
                                    newLevel = 6
                                    levelUp = true
                                }
                                300 -> if (player.level == 6) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_7)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_7)
                                    lockedX = player.lockedX.minus(R.drawable.x_7)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_7)
                                    lockedO = player.lockedO.minus(R.drawable.o_7)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_7)
                                    newLevel = 7
                                    levelUp = true
                                }
                                400 -> if (player.level == 7) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_8)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_8)
                                    lockedX = player.lockedX.minus(R.drawable.x_8)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_8)
                                    lockedO = player.lockedO.minus(R.drawable.o_8)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_8)
                                    newLevel = 8
                                    levelUp = true
                                }
                                500 -> if (player.level == 8) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_9)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_9)
                                    lockedX = player.lockedX.minus(R.drawable.x_9)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_9)
                                    lockedO = player.lockedO.minus(R.drawable.o_9)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_9)
                                    newLevel = 9
                                    levelUp = true
                                }
                                600 -> if (player.level == 9) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_10_1) - R.drawable.xo_10_2
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_10_1) + R.drawable.xo_10_2
                                    lockedX = player.lockedX.minus(R.drawable.x_10)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_10)
                                    lockedO = player.lockedO.minus(R.drawable.o_10)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_10)
                                    newLevel = 10
                                    levelUp = true
                                }
                                700 -> if (player.level == 10) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_11)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_11)
                                    lockedX = player.lockedX.minus(R.drawable.x_11)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_11)
                                    lockedO = player.lockedO.minus(R.drawable.o_11)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_11)
                                    newLevel = 11
                                    levelUp = true
                                }
                                800 -> if (player.level == 11) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_12)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_12)
                                    lockedX = player.lockedX.minus(R.drawable.x_12)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_12)
                                    lockedO = player.lockedO.minus(R.drawable.o_12)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_12)
                                    newLevel = 12
                                    levelUp = true
                                }
                                900 -> if (player.level == 12) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_13)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_13)
                                    lockedX = player.lockedX.minus(R.drawable.x_13)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_13)
                                    lockedO = player.lockedO.minus(R.drawable.o_13)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_13)
                                    newLevel = 13
                                    levelUp = true
                                }
                                1000 -> if (player.level == 13) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_14)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_14)
                                    lockedX = player.lockedX.minus(R.drawable.x_14)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_14)
                                    lockedO = player.lockedO.minus(R.drawable.o_14)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_14)
                                    newLevel = 14
                                    levelUp = true
                                }
                                1200 -> if (player.level == 14) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_15_1) - R.drawable.xo_15_2
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_15_1) + R.drawable.xo_15_2
                                    lockedX = player.lockedX.minus(R.drawable.x_15)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_15)
                                    lockedO = player.lockedO.minus(R.drawable.o_15)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_15)
                                    newLevel = 15
                                    levelUp = true
                                }

                            }
                            if (levelUp) {
                                updatedPlayer = MainPlayerUiState(
                                    name = player.name,
                                    email = player.email,
                                    score = score,
                                    password = player.password,
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