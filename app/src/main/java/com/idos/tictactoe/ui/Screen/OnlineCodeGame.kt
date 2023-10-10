package com.idos.tictactoe.ui.Screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Primery
import com.idos.tictactoe.ui.theme.Secondery
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun OpenOnlineGameWithCode(context: Context, player: String, viewModel: CodeGameViewModel, navController: NavController) {
    var currentGame by remember {
        mutableStateOf(com.idos.tictactoe.data.OnlineGameUiState())
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
        mutableStateOf(com.idos.tictactoe.data.MainPlayerUiState())
    }
    var player2 by remember {
        mutableStateOf(com.idos.tictactoe.data.MainPlayerUiState())
    }
    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                if (currentGame == com.idos.tictactoe.data.OnlineGameUiState()) {
                    val key: String = databaseReference.push().key!!.takeLast(5)
                    val newGame = com.idos.tictactoe.data.OnlineGameUiState(
                        id = key,
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = com.idos.tictactoe.data.Boxes()
                    )
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
                                    val p: com.idos.tictactoe.data.MainPlayerUiState? =
                                        d.toObject(com.idos.tictactoe.data.MainPlayerUiState::class.java)
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
                val game = Game.getValue(com.idos.tictactoe.data.OnlineGameUiState::class.java)
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
                                        val p: com.idos.tictactoe.data.MainPlayerUiState? = d.toObject(
                                            com.idos.tictactoe.data.MainPlayerUiState::class.java)
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
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (player1 != com.idos.tictactoe.data.MainPlayerUiState()) {
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
        OnlineButtonGrid(
            gameId = currentGame.id,
            myTurn = myTurn,
            gameStarted = foundPlayer,
            player = player,
            player1 = player1,
            player2 = player2,
            databaseReference = databaseReference,
            viewModel = viewModel,
            navController = navController
        )
        Spacer(modifier = Modifier.weight(4f))
    }
    
    if(!foundPlayer) {
        Dialog(
            onDismissRequest = {}
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(Color.White)) {
                Text(
                    text = "Game code:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = currentGame.id,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EnterOnlineGameWithCode(context: Context, player: String, gameId: String, viewModel: CodeGameViewModel, navController: NavController) {
    var currentGame by remember {
        mutableStateOf(com.idos.tictactoe.data.OnlineGameUiState())
    }
    var times by remember {
        mutableStateOf(1)
    }
    var myTurn by remember {
        mutableStateOf<String?>(null)
    }
    var player1 by remember {
        mutableStateOf(com.idos.tictactoe.data.MainPlayerUiState())
    }
    var player2 by remember {
        mutableStateOf(com.idos.tictactoe.data.MainPlayerUiState())
    }
    var foundPlayer by remember {
        mutableStateOf(false)
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
                    val game = Game.getValue(com.idos.tictactoe.data.OnlineGameUiState::class.java)
                    if ((game!!.id == gameId)) {
                        val updatedGame = com.idos.tictactoe.data.OnlineGameUiState(
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
                                        val p: com.idos.tictactoe.data.MainPlayerUiState? = d.toObject(
                                            com.idos.tictactoe.data.MainPlayerUiState::class.java)
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
                val game = Game.getValue(com.idos.tictactoe.data.OnlineGameUiState::class.java)
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
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (player1 != com.idos.tictactoe.data.MainPlayerUiState()) {
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
        OnlineButtonGrid(
            gameId = currentGame.id,
            myTurn = myTurn,
            gameStarted = foundPlayer,
            player = player,
            player1 = player1,
            player2 = player2,
            databaseReference = databaseReference,
            viewModel = viewModel,
            navController = navController
        )
        Spacer(modifier = Modifier.weight(4f))
    }
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
fun enterGameWithCodeButton(modifier: Modifier, codeGameViewModel: CodeGameViewModel, codeGameUiState: OnlineGameRememberedValues, navController: NavController) {
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
            }
        )
    }
}

@Composable
fun CheckForGame(gameId: String, context: Context, onFindGame: () -> Unit) {
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
                val game = Game.getValue(com.idos.tictactoe.data.OnlineGameUiState::class.java)
                if (game!!.id == gameId) {
                    foundGame = true
                    if (game.player2 == "") {
                        onFindGame()
                    } else {

                    }
                    break
                }
            }
            if (!foundGame) {

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
fun codeGameScreen(codeGameViewModel: CodeGameViewModel, codeGameUiState: OnlineGameRememberedValues, navController: NavController) {
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