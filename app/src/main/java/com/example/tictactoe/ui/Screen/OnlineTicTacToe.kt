package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.data.Boxes
import com.example.tictactoe.data.OnlineGameUiState
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
    var PlayerTurn = if(game.playerTurn == "X") {
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
fun OnlineGameButton(game: OnlineGameUiState, boxNumber: Int, box: String, enabled: Boolean) {

    var setBox by remember {
        mutableStateOf(false)
    }

    Card(modifier = Modifier.padding(8.dp),shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        TextButton(
            onClick = { setBox = true },
            enabled = (box == "") && enabled,
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

    if (setBox) {
        SetBoxOnline(game = game, boxNumber = boxNumber, playerTurn = game.playerTurn)
        changePlayerTurn(game)
        setBox = false
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun OnlineButtonGrid(game: OnlineGameUiState, myTurn: String?) {

    val enabled = myTurn == game.playerTurn

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

    if(game.times >= 5) {
        databaseReference.child(game.id.toString()).child("winner").setValue(CheckOnlineWinner(game.boxes))
        if(game.winner!!.isNotEmpty()) {
            if (game.winner == myTurn) {
                //show winner
                ShowOnlineWinner(text1 = "You won")
            } else if (game.winner != myTurn) {
                //show winner
                ShowOnlineWinner(text1 = "You lose")
            }
        }
        //show tie
        else if (game.times == 9){
            ShowOnlineWinner(text1 = "Tie")
        }

    }
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
                    if ((game!!.player2 == "") && (game.winner == null)) {
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
                        winner = null,
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

    if (!foundPlayer) {
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
    }
    OnlineButtonGrid(game = currentGame, myTurn = myTurn)
}

@Composable
fun ShowOnlineWinner(text1: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = text1, fontSize = 25.sp, fontWeight = FontWeight.Bold)
    }
}