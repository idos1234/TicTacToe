package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.R
import com.example.tictactoe.data.Boxes
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.data.OnlineGameUiState
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SetBoxOnline(game: OnlineGameUiState, boxNumber: Int, playerTurn: String) {
    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")

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

    databaseReference.child(game.id.toString()).child("boxes").setValue(boxes)
    databaseReference.child(game.id.toString()).child("times").setValue(game.times.plus(1))

}

@Composable
fun changePlayerTurn(game: OnlineGameUiState) {
    val PlayerTurn = if(game.playerTurn == "X") {
        "O"
    } else {
        "X"
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")

    databaseReference.child(game.id.toString()).child("playerTurn").setValue(PlayerTurn)

}

@Composable
fun OnlineGameButton(game: OnlineGameUiState, boxNumber: Int, box: String, enabled: Boolean, context: Context = LocalContext.current) {
    var player1 by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var player2 by remember {
        mutableStateOf(MainPlayerUiState())
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
                for (d in list) {
                    val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
                    //find player using database
                    if (p?.name == game.player1){
                        player1 = p
                    }
                    if (p?.name == game.player2){
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
                .background(Primery)
                .size(100.dp)
                .clickable(
                    onClick = { setBox = true },
                    enabled = (box == "") && enabled,
                )
        )
    }

    if (setBox) {
        SetBoxOnline(game = game, boxNumber = boxNumber, playerTurn = game.playerTurn)
        changePlayerTurn(game)
        setBox = false
    }
}

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Composable
fun OnlineButtonGrid(gameId: Int, myTurn: String?, gameStarted: Boolean, player: String) {
    var game by remember {
        mutableStateOf(OnlineGameUiState())
    }
    val enabled = if (gameStarted) {
        myTurn == game.playerTurn
    } else {
        false
    }
    val scope = rememberCoroutineScope()
    var foundWinner by remember {
        mutableStateOf(false)
    }

    Column {
        Row {
            OnlineGameButton(game = game, boxNumber = 1, box = game.boxes.Box1, enabled = enabled)
            OnlineGameButton(game = game, boxNumber = 2, box = game.boxes.Box2, enabled = enabled)
            OnlineGameButton(game = game, boxNumber = 3, box = game.boxes.Box3, enabled = enabled)
        }
        Row {
            OnlineGameButton(game = game, boxNumber = 4, box = game.boxes.Box4, enabled = enabled)
            OnlineGameButton(game = game, boxNumber = 5, box = game.boxes.Box5, enabled = enabled)
            OnlineGameButton(game = game, boxNumber = 6, box = game.boxes.Box6, enabled = enabled)
        }
        Row {
            OnlineGameButton(game = game, boxNumber = 7, box = game.boxes.Box7, enabled = enabled)
            OnlineGameButton(game = game, boxNumber = 8, box = game.boxes.Box8, enabled = enabled)
            OnlineGameButton(game = game, boxNumber = 9, box = game.boxes.Box9, enabled = enabled)
        }
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")

    game = findGame(gameId = gameId, databaseReference = databaseReference)
    if(game.times >= 5) {
        databaseReference.child(game.id.toString()).child("winner").setValue(CheckOnlineWinner(game.boxes))
        if(game.winner.isNotEmpty()) {
            if (!foundWinner) {
                if (game.winner == "X" && !foundWinner) {
                    databaseReference.child(game.id.toString()).child("player1Score").setValue(game.player1Score.plus(1))
                    foundWinner = true
                }
                if (game.winner == "O" && !foundWinner) {
                    databaseReference.child(game.id.toString()).child("player2Score").setValue(game.player2Score.plus(1))
                    foundWinner = true
                }
            }
            if (game.player1Score == 2) {
                if (myTurn == "X") {
                    //show winner
                    ShowOnlineWinner(text1 = "You won")
                    updateScore(playerName = player, addedScore = 1, context = LocalContext.current)
                } else if (myTurn == "O") {
                    //show winner
                    ShowOnlineWinner(text1 = "You lose")
                    updateScore(playerName = player, addedScore = -1, context = LocalContext.current)
                }
            } else if (game.player2Score == 2) {
                if (myTurn == "O") {
                    //show winner
                    ShowOnlineWinner(text1 = "You won")
                    updateScore(playerName = player, addedScore = 1, context = LocalContext.current)
                } else if (myTurn == "X") {
                    //show winner
                    ShowOnlineWinner(text1 = "You lose")
                    updateScore(playerName = player, addedScore = -1, context = LocalContext.current)
                }
            } else if (foundWinner) {
                scope.launch {
                    delay(3000)
                    ResetGame(game = game, databaseReference = databaseReference)
                    foundWinner = false
                }
            }
        }
        //show tie
        else if (game.times == 9){
            if (foundWinner) {
                scope.launch {
                    delay(3000)
                    ResetGame(game = game, databaseReference = databaseReference)
                    foundWinner = false
                }
            }
        }
    }
}

@Composable
fun findGame(gameId: Int, databaseReference: DatabaseReference, context: Context = LocalContext.current): OnlineGameUiState {
    var foundGame by remember {
        mutableStateOf(OnlineGameUiState())
    }
    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            for (Game in snapshot.children) {
                var game = Game.getValue(OnlineGameUiState::class.java)
                if (game!!.id == gameId) {
                    foundGame = game
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
    return foundGame
}

fun ResetGame(game: OnlineGameUiState, databaseReference: DatabaseReference) {
    val resetGame = game.copy(
        winner = "",
        boxes = Boxes(),
        times = 0,
    )
    databaseReference.child(game.id.toString()).setValue(resetGame)

}

@Composable
fun OnlineTicTacToe(player: String, context: Context) {
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


    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")

    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                Loop@ for (Game in snapshot.children) {
                    val game = Game.getValue(OnlineGameUiState::class.java)
                    if ((game!!.player2 == "") && (game.winner == "")) {
                        val updatedGame = OnlineGameUiState(
                            id = game.id,
                            player1 = game.player1,
                            player2 = player,
                            winner = game.winner,
                            boxes = game.boxes
                        )
                        databaseReference.child(game.id.toString()).child("player2").setValue(player)
                        currentGame = updatedGame
                        foundPlayer = true
                        myTurn = "O"
                        break@Loop
                    }
                }
                if (currentGame == OnlineGameUiState()) {
                    val newGame = OnlineGameUiState(
                        id = snapshot.children.toList().size.plus(1),
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = Boxes()
                    )
                    databaseReference.child(snapshot.children.toList().size.plus(1).toString()).setValue(newGame)
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

    foundPlayer = currentGame.player2 != ""

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier
                .size(150.dp)
                .padding(20.dp), elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (currentGame.playerTurn == "X") Primery else { Secondery})) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("X", fontWeight = FontWeight.Bold, fontSize = 35.sp)
                    Text(currentGame.player1, fontSize = 10.sp)
                }
            }
            Card(modifier = Modifier
                .size(150.dp)
                .padding(20.dp), elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (currentGame.playerTurn == "O") Primery else { Secondery})) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (foundPlayer) {
                        Text("O", fontWeight = FontWeight.Bold, fontSize = 35.sp)
                        Text(currentGame.player2, fontSize = 10.sp)
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(2f))
        OnlineButtonGrid(gameId = currentGame.id, myTurn = myTurn, gameStarted = foundPlayer, player = player)
        Spacer(modifier = Modifier.weight(4f))
    }
}

@Composable
fun ShowOnlineWinner(text1: String) {
    AlertDialog(
        onDismissRequest = {},
        dismissButton = {},
        confirmButton = {},
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = text1, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            }
        }
    )
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
                    if (p?.name == playerName){
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
                                    lockedX = player.lockedX.minus(R.drawable.x_5_6)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_5_6)
                                    lockedO = player.lockedO.minus(R.drawable.o_5)
                                    unlockedO = player.unlockedO.plus(R.drawable.o_5)
                                    newLevel = 5
                                    levelUp = true
                                }
                                200 -> if (player.level == 5) {
                                    lockedPhotos = player.lockedImages.minus(R.drawable.xo_6)
                                    unlockedPhotos = player.unlockedImages.plus(R.drawable.xo_6)
                                    lockedX = player.lockedX.minus(R.drawable.x_5_6)
                                    unlockedX = player.unlockedX.plus(R.drawable.x_5_6)
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
                                    draws = player.draws,
                                    level = newLevel
                                )
                            } else {
                                updatedPlayer = MainPlayerUiState(
                                    name = player.name,
                                    email = player.email,
                                    score = score,
                                    password = player.password,
                                    currentImage = player.currentImage,
                                    unlockedImages = player.unlockedImages,
                                    lockedImages = player.lockedImages,
                                    currentX = player.currentX,
                                    currentO = player.currentO,
                                    lockedX = player.lockedX,
                                    lockedO = player.lockedX,
                                    unlockedX = player.lockedX,
                                    unlockedO = player.lockedX,
                                    wins = player.wins + 1,
                                    loses = player.loses,
                                    draws = player.draws,
                                    level = player.level
                                )
                            }
                        } else if (addedScore == -1) {
                            updatedPlayer = MainPlayerUiState(
                                name = player.name,
                                email = player.email,
                                score = score,
                                password = player.password,
                                currentImage = player.currentImage,
                                unlockedImages = player.unlockedImages,
                                lockedImages = player.lockedImages,
                                currentX = player.currentX,
                                currentO = player.currentO,
                                lockedX = player.lockedX,
                                lockedO = player.lockedX,
                                unlockedX = player.lockedX,
                                unlockedO = player.lockedX,
                                wins = player.wins,
                                loses = player.loses + 1,
                                draws = player.draws,
                                level = player.level
                            )
                        } else if (addedScore == 0) {
                            updatedPlayer = MainPlayerUiState(
                                name = player.name,
                                email = player.email,
                                score = score,
                                password = player.password,
                                currentImage = player.currentImage,
                                unlockedImages = player.unlockedImages,
                                lockedImages = player.lockedImages,
                                currentX = player.currentX,
                                currentO = player.currentO,
                                lockedX = player.lockedX,
                                lockedO = player.lockedX,
                                unlockedX = player.lockedX,
                                unlockedO = player.lockedX,
                                wins = player.wins,
                                loses = player.loses,
                                draws = player.draws + 1,
                                level = player.level
                            )
                        }

                        db.collection("Players")
                            .whereEqualTo("name", playerName)
                            .get()
                            .addOnSuccessListener {
                                for (document in it) {
                                    db.collection("Players").document(document.id).set(updatedPlayer,
                                        SetOptions.merge())
                                }
                            }
                        break@Loop
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


