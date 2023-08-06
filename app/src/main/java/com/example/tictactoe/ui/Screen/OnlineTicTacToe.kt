package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.data.Boxes
import com.example.tictactoe.data.OnlineGameUiState
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@Composable
fun SetBoxOnline(game: OnlineGameUiState, boxNumber: Int, playerTurn: String) {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbGames: CollectionReference = db.collection("Games")

    val boxes: Boxes? = when(boxNumber) {
        1 -> game.boxes.copy(box1 = playerTurn)
        2 -> game.boxes.copy(box2 = playerTurn)
        3 -> game.boxes.copy(box3 = playerTurn)
        4 -> game.boxes.copy(box4 = playerTurn)
        5 -> game.boxes.copy(box5 = playerTurn)
        6 -> game.boxes.copy(box6 = playerTurn)
        7 -> game.boxes.copy(box7 = playerTurn)
        8 -> game.boxes.copy(box8 = playerTurn)
        9 -> game.boxes.copy(box9 = playerTurn)
        else -> null
    }

    val updatedGame = OnlineGameUiState(
        id = game.id,
        player1 = game.player1,
        player2 = game.player2,
        winner = game.winner,
        boxes = boxes!!
    )
    dbGames
        .whereEqualTo("id", game.id)
        .get().addOnSuccessListener {
            for (document in it) {
                dbGames.document(document.id).set(updatedGame, SetOptions.merge())
            }
        }
}

@Composable
fun OnlineGameButton(game: OnlineGameUiState, boxNumber: Int, box: String, playerTurn: String) {
    var setBox by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier.padding(8.dp),shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        TextButton(
            onClick = { setBox = true },
            enabled = box == "",
            modifier = Modifier.background(Primery)
        ) {
            Text(
                text = box,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .size(50.dp)
                    .fillMaxHeight()
            )
        }
    }

    if (setBox && box == "") {
        SetBoxOnline(game = game, boxNumber = boxNumber, playerTurn = playerTurn)
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun OnlineButtonGrid(context: Context, player: String) {
    var times by remember {
        mutableStateOf(1)
    }
    var game by remember {
        mutableStateOf<OnlineGameUiState?>(null)
    }
    var gamesList = mutableStateListOf<OnlineGameUiState>()
    //get database
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    while (times == 1) {
        //get Players collection from database
        db.collection("Games").get()
            //on success
            .addOnSuccessListener { queryDocumentSnapshots ->
                //check if collection is empty
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    for (d in list) {
                        val p: OnlineGameUiState? = d.toObject(OnlineGameUiState::class.java)
                        gamesList.add(p!!)
                        //find player using database
                        if (p.player2 == "") {
                            game = p
                        }

                    }
                }
                val dbGames: CollectionReference = db.collection("Games")
                if (game == null) {

                    val newGame = OnlineGameUiState(
                        id = gamesList.size.plus(1),
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = Boxes()
                    )

                    dbGames.add(newGame)
                        .addOnSuccessListener {
                            Toast.makeText(context,"Game started", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    val updatedGame = OnlineGameUiState(
                        id = game!!.id,
                        player1 = game!!.player1,
                        player2 = player,
                        winner = game!!.winner,
                        boxes = game!!.boxes
                    )
                    dbGames
                        .whereEqualTo("id", game!!.id)
                        .get().addOnSuccessListener {
                            for (document in it) {
                                dbGames.document(document.id).set(updatedGame, SetOptions.merge())
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
        times ++
    }
}

@Composable
fun OnlineTicTacToe(player: String) {
    OnlineButtonGrid(context = LocalContext.current, player = player)
}