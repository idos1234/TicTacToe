package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.example.tictactoe.data.Boxes
import com.example.tictactoe.data.OnlineGameUiState
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

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun OnlineButtonGrid(player: String) {

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
                        val game: OnlineGameUiState? = d.toObject(OnlineGameUiState::class.java)
                        if ((game!!.player2 == "") && (game.winner == "")) {
                            val updatedGame = OnlineGameUiState(
                                id = game.id,
                                player1 = game.player1,
                                player2 = player,
                                winner = game.winner,
                                boxes = game.boxes
                            )
                            db.collection("Games")
                                .whereEqualTo("id", game.id)
                                .get().addOnSuccessListener {
                                    for (document in it) {
                                        db.collection("Games")
                                            .document(document.id)
                                            .set(updatedGame, SetOptions.merge())
                                    }
                                }
                            currentGame = updatedGame
                            foundPlayer = true
                            break
                        }
                    }
                    if (currentGame == OnlineGameUiState()) {
                        val newGame = OnlineGameUiState(
                            id = list.size.plus(1),
                            player1 = player,
                            player2 = "",
                            winner = "",
                            boxes = Boxes()
                        )

                        db.collection("Games").add(newGame)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Game started", Toast.LENGTH_SHORT).show()
                            }
                        currentGame = newGame
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

    db.collection("Games").get()
        .addOnSuccessListener { queryDocumentSnapshots ->
            if (!queryDocumentSnapshots.isEmpty) {
                val list = queryDocumentSnapshots.documents
                for (d in list) {
                    val game: OnlineGameUiState? = d.toObject(OnlineGameUiState::class.java)
                    if (game!!.id == currentGame.id) {
                        currentGame = game
                    }
                }
            }
        }

    foundPlayer = currentGame.player2 != ""

    if (!foundPlayer) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            dismissButton = {},
            text = {
                Row {
                   Text(text = "Waiting for player")
                }
                CircularProgressIndicator()
            }
        )
    }
    OnlineButtonGrid(player = player)
}