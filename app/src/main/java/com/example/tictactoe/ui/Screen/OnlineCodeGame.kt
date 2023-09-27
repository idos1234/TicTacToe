package com.example.tictactoe.ui.Screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoe.data.Boxes
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.data.OnlineGameUiState
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun OpenOnlineGameWithCode(context: Context, player: String) {
    var currentGame by remember {
        mutableStateOf(OnlineGameUiState())
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
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                if (currentGame == OnlineGameUiState()) {
                    val newGame = OnlineGameUiState(
                        id = snapshot.children.toList().size.plus(1),
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = Boxes()
                    )
                    databaseReference.child(snapshot.children.toList().size.plus(1).toString())
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
                                    if (p?.name == player) {
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
                var game = Game.getValue(OnlineGameUiState::class.java)
                if (game!!.id == currentGame.id) {
                    currentGame = game
                    if (currentGame.player2 != "") {
                        //get Players collection from database
                        db.collection("Players").get()
                            //on success
                            .addOnSuccessListener { queryDocumentSnapshots ->
                                //check if collection is empty
                                if (!queryDocumentSnapshots.isEmpty) {
                                    val list = queryDocumentSnapshots.documents
                                    for (d in list) {
                                        val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
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
}

@Composable
fun EnterOnlineGameWithCode(context: Context, player: String, gameId: String) {
    var currentGame by remember {
        mutableStateOf(OnlineGameUiState())
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
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                for (Game in snapshot.children) {
                    val game = Game.getValue(OnlineGameUiState::class.java)
                    if ((game!!.id.toString() == gameId)) {
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
                                        val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
                                        //find player using database
                                        if (p?.name == game.player1){
                                            player1 = p
                                        }
                                        if (p?.name == player){
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
                        databaseReference.child(game.id.toString()).child("player2").setValue(player)
                        currentGame = updatedGame
                        myTurn = "O"
                        break
                    }
                }
                times++
            }
            for (Game in snapshot.children) {
                var game = Game.getValue(OnlineGameUiState::class.java)
                if (game!!.id == currentGame.id) {
                    currentGame = game
                    if (currentGame.player2 != "") {
                        //get Players collection from database
                        db.collection("Players").get()
                            //on success
                            .addOnSuccessListener { queryDocumentSnapshots ->
                                //check if collection is empty
                                if (!queryDocumentSnapshots.isEmpty) {
                                    val list = queryDocumentSnapshots.documents
                                    for (d in list) {
                                        val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
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

}

@Composable
fun openNewGameButton(modifier: Modifier, navController: NavController) {
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
        openGame = false
        navController.navigate(GameScreen.OpenGameWithCode.name)
    }
}

@Composable
fun enterGameWithCodeButton(modifier: Modifier, codeGameViewModel: CodeGameViewModel, codeGameUiState: CodeGame, navController: NavController) {
    var enterGame by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(BackGround)) {
        TextField(
            value = codeGameUiState.gameCode,
            onValueChange = { codeGameViewModel.updateGameCode(it) },
            placeholder = { Text(text = "Code:")},
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Secondery),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (codeGameUiState.gameCode != "") {
                enterGame = true
            } else {
                Toast.makeText(context,"You did not put game code", Toast.LENGTH_SHORT).show()
            }},
            colors = ButtonDefaults.buttonColors(backgroundColor = Primery)) {
            Text(text = "Enter game", fontSize = 30.sp)
        }
    }
    if (enterGame) {
        CheckForGame(
            gameId = codeGameUiState.gameCode,
            context = context,
            onFindGame = {
                enterGame = false
                navController.navigate(GameScreen.EnterGameWithCode.name)
            },
            onNotFindGame = { enterGame = false }
        )
    }
}

@Composable
fun CheckForGame(gameId: String, context: Context, onFindGame: () -> Unit, onNotFindGame: () -> Unit) {
    var foundGame by remember {
        mutableStateOf(false)
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")
    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            for (Game in snapshot.children) {
                var game = Game.getValue(OnlineGameUiState::class.java)
                if (game!!.id.toString() == gameId) {
                    foundGame = true
                    if (game.player2 == "") {
                        onFindGame()
                    } else {
                        Toast.makeText(
                            context,
                            "The game already started.",
                            Toast.LENGTH_SHORT
                        ).show()
                        onNotFindGame()
                    }
                    break
                }
            }
            if (!foundGame) {
                Toast.makeText(
                    context,
                    "Did not find game.",
                    Toast.LENGTH_SHORT
                ).show()
                onNotFindGame()
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
fun codeGameScreen(codeGameViewModel: CodeGameViewModel, codeGameUiState: CodeGame, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(BackGround)
    ) {
        enterGameWithCodeButton(modifier = Modifier.weight(1f), codeGameUiState = codeGameUiState, codeGameViewModel = codeGameViewModel, navController = navController)
        openNewGameButton(modifier = Modifier.weight(1f), navController = navController)
    }
}