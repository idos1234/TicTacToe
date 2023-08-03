package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import com.example.tictactoe.data.OnlineGameUiState
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun OnlineGameButton() {

}

@Composable
fun OnlineButtonGrid() {

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun OnlineTicTacToe(context: Context) {
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
        }
        //on failure
        .addOnFailureListener {
            Toast.makeText(
                context,
                "Fail to get the data.",
                Toast.LENGTH_SHORT
            ).show()
        }

    var waitForPlayer by remember {
        mutableStateOf(true)
    }

    for (g in GamesList) {
        if (g?.player2 == "") {
            waitForPlayer = false
        }
    }
}