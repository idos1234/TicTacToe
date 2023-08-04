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
                        //find player using database
                        if (p?.player2 == "") {
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