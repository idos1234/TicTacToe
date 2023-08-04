package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.tictactoe.data.Boxes
import com.example.tictactoe.data.OnlineGameUiState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@Composable
fun OnlineGameButton() {

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun OnlineButtonGrid(context: Context, player: String) {
    var waitForPlayer by remember {
        mutableStateOf(true)
    }
    var game by remember {
        mutableStateOf(OnlineGameUiState())
    }
    while (waitForPlayer) {
        //games in database
        var GamesList = mutableStateListOf<OnlineGameUiState?>()
        //database
        var db: FirebaseFirestore = FirebaseFirestore.getInstance()

        //get Players collection from database
        db.collection("Games").get()
            //on success
            .addOnSuccessListener { queryDocumentSnapshots ->
                //check if collection is empty
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    for (d in list) {
                        //add every player to player list
                        val p: OnlineGameUiState? = d.toObject(OnlineGameUiState::class.java)
                        GamesList.add(p)

                    }
                }
                var id: Int = GamesList.size

                for (g in GamesList) {
                    if (g?.player2 == "") {
                        waitForPlayer = false
                        game = g
                    }
                }

                if (waitForPlayer) {
                    db.collection("Games").get()
                        .addOnSuccessListener {
                            val dbGames: CollectionReference = db.collection("Games")

                            val newGame = OnlineGameUiState(
                                id = id + 1,
                                player1 = player,
                                player2 = "",
                                winner = "",
                                boxes = Boxes()
                            )
                            dbGames.add(newGame)
                        }
                } else {
                    val newGame = OnlineGameUiState(
                        id = game.id,
                        player1 = game.player1,
                        player2 = player,
                        winner = "",
                        boxes = Boxes()
                    )
                    db.collection("Games")
                        .whereEqualTo("id", game.id)
                        .get()
                        .addOnSuccessListener {
                            for (document in it) {
                                db.collection("Games").document(document.id).set(newGame, SetOptions.merge())
                            }
                        }
                }
                waitForPlayer = false
            }
            //on failure
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Fail to get the data.",
                    Toast.LENGTH_SHORT
                ).show()
                waitForPlayer = false
            }
    }



}

@Composable
fun OnlineTicTacToe(player: String) {
    OnlineButtonGrid(context = LocalContext.current, player = player)
}