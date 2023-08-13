package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.example.tictactoe.data.Boxes
import com.example.tictactoe.data.OnlineGameUiState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    val firebaseDatabase = FirebaseDatabase.getInstance();
    val databaseReference = firebaseDatabase.getReference("Games");
        //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            println("success to get collection")
            while (times == 1) {
                println("start searching for game")
                Loop@ for (Game in snapshot.children) {
                    val game = Game.getValue(OnlineGameUiState::class.java)
                    if ((game!!.player2 == "") && (game.winner == "")) {
                        println("Found game")
                        val updatedGame = OnlineGameUiState(
                            id = game.id,
                            player1 = game.player1,
                            player2 = player,
                            winner = game.winner,
                            boxes = game.boxes
                        )
                        databaseReference.child(game.id.toString()).child("player2").setValue(player)
                        println("joining game")
                        currentGame = updatedGame
                        foundPlayer = true
                        println("stop searching")
                        break@Loop
                    }
                }
                if (currentGame == OnlineGameUiState()) {
                    println("didn't find game")
                    val newGame = OnlineGameUiState(
                        id = snapshot.children.toList().size.plus(1),
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = Boxes()
                    )
                    println("adding new game")
                    databaseReference.child(snapshot.children.toList().size.plus(1).toString()).setValue(newGame)
                    currentGame = newGame
                }
                times++
            }
            println("searching for update in game")
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
            println("Fail to get collection")
            Toast.makeText(
                context,
                "Fail to get the data.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    )

    foundPlayer = currentGame.player2 != ""

    if (!foundPlayer) {
        println("still waiting for player")
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            dismissButton = {},
            text = {
                Row {
                    Text(text = "Waiting for player")
                    CircularProgressIndicator()
                }
            }
        )
    } else {
        println("found player")
        println("game started")
        Toast.makeText(context, "Game started", Toast.LENGTH_SHORT).show()
    }
    OnlineButtonGrid(player = player)
}