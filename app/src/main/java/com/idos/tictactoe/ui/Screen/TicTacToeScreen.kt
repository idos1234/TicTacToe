package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idos.tictactoe.R
import com.idos.tictactoe.data.UiState
import com.idos.tictactoe.ui.CheckWinner
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Primery
import com.idos.tictactoe.ui.theme.Secondery
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * Show a single button in the two players game grid
 */

@Composable
fun GameButton(box: String, onClick: () -> Unit = {}, context: Context = LocalContext.current, playerName: String) {
    var player by remember {
        mutableStateOf(com.idos.tictactoe.data.MainPlayerUiState())
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
                    val p: com.idos.tictactoe.data.MainPlayerUiState? = d.toObject(com.idos.tictactoe.data.MainPlayerUiState::class.java)
                    //find player using database
                    if (p?.email == playerName){
                        player = p
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

    Card(modifier = Modifier.padding(8.dp),shape = RoundedCornerShape(100.dp), border = BorderStroke(3.dp, color = Secondery)) {
        Image(
            painter = painterResource(
                id = when (box) {
                    "X" -> player.currentX
                    "O" -> player.currentO
                    else -> {
                        R.drawable.o_1}
                }
            ),
            contentDescription = null,
            alpha = if (box != "") DefaultAlpha else 0f,
            modifier = Modifier
                .background(Color.Gray)
                .size(100.dp)
                .clickable(
                    onClick = onClick,
                    enabled = box.isEmpty(),
                )
        )
    }
}

/**
 * Show the two players game grid
 */

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ButtonGrid(
    viewModel: TicTacToeViewModel,
    onPlayAgain: () -> Unit,
    uiState: UiState,
    playerName: String
) {

    //if need to check winner
    if(uiState.ToCheck) {
        uiState.winner = CheckWinner(uiState)
        if(uiState.winner != "") {

            //show winner
             showWinner(
                 winner = "Winner is: ${uiState.winner}",
                 text = "Congratulations for winning",
                 onPlayAgain = {
                     onPlayAgain()
                 }
             )
        }
        //show tie
        else if (uiState.times == 9){
            showWinner(winner = "Draw", text = "Try to win next time", onPlayAgain = onPlayAgain)
        }

    } else {
        uiState.winner = ""
    }

    val onClick = {
        viewModel.onClick()
    }



    Column() {
        Row() {
            //box1
            GameButton(
                box = uiState.Box1,
                onClick = {
                    if (uiState.Box1 == "") {
                        viewModel.SetBox(1)
                        onClick()
                    }
                },
                playerName = playerName
            )
            //box2
            GameButton(
                box = uiState.Box2,
                onClick = {
                    if (uiState.Box2 == "") {
                        viewModel.SetBox(2)
                        onClick()
                    }
                },
                playerName = playerName
            )
            //box3
            GameButton(
                box = uiState.Box3,
                onClick = {
                    if (uiState.Box3 == "") {
                        viewModel.SetBox(3)
                        onClick()
                    }
                },
                playerName = playerName
            )
        }
        Row() {
            //box4
            GameButton(
                box = uiState.Box4,
                onClick = {
                    if (uiState.Box4 == "") {
                        viewModel.SetBox(4)
                        onClick()
                    }
                },
                playerName = playerName
            )
            //box5
            GameButton(
                box = uiState.Box5,
                onClick = {
                    if (uiState.Box5 == "") {
                        viewModel.SetBox(5)
                        onClick()
                    }
                },
                playerName = playerName
            )
            //box6
            GameButton(
                box = uiState.Box6,
                onClick = {
                    if (uiState.Box6 == "") {
                        viewModel.SetBox(6)
                        onClick()
                    }
                },
                playerName = playerName
            )
        }
        Row() {
            //box7
            GameButton(
                box = uiState.Box7,
                onClick = {
                    if (uiState.Box7 == "") {
                        viewModel.SetBox(7)
                        onClick()
                    }
                },
                playerName = playerName
            )
            //box8
            GameButton(
                box = uiState.Box8,
                onClick = {
                    if (uiState.Box8 == "") {
                        viewModel.SetBox(8)
                        onClick()
                    }
                },
                playerName = playerName
            )
            //box9
            GameButton(
                box = uiState.Box9,
                onClick = {
                    if (uiState.Box9 == "") {
                        viewModel.SetBox(9)
                        onClick()
                    }
                },
                playerName = playerName
            )
        }
    }
}

/**
 * Show the winner
 */

@Composable
fun showWinner(winner: String, text: String, onPlayAgain: () -> Unit) {

    val activity = (LocalContext.current as Activity)

    AlertDialog(
        backgroundColor = Secondery,
        onDismissRequest = {},
        title = { Text(text = winner, fontWeight = FontWeight.ExtraBold, color = Color.Black, textAlign = TextAlign.Center)},
        text = { Text(text = text, color = Color.Black, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)},
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text("Exit")
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = "Play again")
            }
        }
    )
}

/**
 * Show the two players game screen
 */

@Composable
fun TicTacToeScreen(
    viewModel: TicTacToeViewModel,
    uiState: UiState,
    onPlayAgain: () -> Unit,
    playerName: String
) {


    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier.size(150.dp).padding(20.dp), elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (uiState.player_Turn == "X") Primery else { Secondery})) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("X", fontWeight = FontWeight.Bold, fontSize = 50.sp)
                }
            }
            Card(modifier = Modifier.size(150.dp).padding(20.dp), elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (uiState.player_Turn == "O") Primery else { Secondery})) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("O", fontWeight = FontWeight.Bold, fontSize = 50.sp)
                }
            }
        }
        Spacer(modifier = Modifier.weight(2f))
        ButtonGrid(
            viewModel = viewModel,
            onPlayAgain = {
                onPlayAgain()
            },
            uiState = uiState,
            playerName = playerName
        )
        Spacer(modifier = Modifier.weight(4f))
    }
}