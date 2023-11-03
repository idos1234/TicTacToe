package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import kotlin.concurrent.schedule

/**
 * Show a single button in the single player game grid
 */

@Composable
fun SinglePlayerGameButton(box: String, onClick: () -> Unit = {}, viewModel: TicTacToeViewModel, context: Context = LocalContext.current, playerName: String) {
    val uiState by viewModel.uiState.collectAsState()
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
                    if (p?.name == playerName){
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
                    onClick = {
                        onClick()
                        if (box.isEmpty()) {
                            Timer().schedule(1000) {
                                if (uiState.winner == "") {
                                    viewModel.botTurn(uiState)
                                }
                            }
                        }
                    },
                    enabled = uiState.isenabled
                )
        )
    }
}

/**
 * Show the single player game grid
 */

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SinglePlayerButtonGrid(
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
        else if (uiState.times >= 9){
            showWinner(winner = "Draw", text = "Try to win next time", onPlayAgain = onPlayAgain)
        }

    } else {
        uiState.winner = ""
    }

    val onClick = {
        viewModel.onSinglePlayerClick()
        viewModel.check_ToCheck()
        if (uiState.winner == "") {
            viewModel.changePlayer()
        }
    }



    Column {
        Row {
            //box1
            SinglePlayerGameButton(
                box = uiState.Box1,
                onClick = {
                    if (uiState.Box1 == "") {
                        viewModel.SetBox(1)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName
            )
            //box2
            SinglePlayerGameButton(
                box = uiState.Box2,
                onClick = {
                    if (uiState.Box2 == "") {
                        viewModel.SetBox(2)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName
            )
            //box3
            SinglePlayerGameButton(
                box = uiState.Box3,
                onClick = {
                    if (uiState.Box3 == "") {
                        viewModel.SetBox(3)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName
            )
        }
        Row() {
            //box4
            SinglePlayerGameButton(
                box = uiState.Box4,
                onClick = {
                    if (uiState.Box4 == "") {
                        viewModel.SetBox(4)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName
            )
            //box5
            SinglePlayerGameButton(
                box = uiState.Box5,
                onClick = {
                    if (uiState.Box5 == "") {
                        viewModel.SetBox(5)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName
            )
            //box6
            SinglePlayerGameButton(
                box = uiState.Box6,
                onClick = {
                    if (uiState.Box6 == "") {
                        viewModel.SetBox(6)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName
            )
        }
        Row() {
            //box7
            SinglePlayerGameButton(
                box = uiState.Box7,
                onClick = {
                    if (uiState.Box7 == "") {
                        viewModel.SetBox(7)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName
            )
            //box8
            SinglePlayerGameButton(
                box = uiState.Box8,
                onClick = {
                    if (uiState.Box8 == "") {
                        viewModel.SetBox(8)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName
            )
            //box9
            SinglePlayerGameButton(
                box = uiState.Box9,
                onClick = {
                    if (uiState.Box9 == "") {
                        viewModel.SetBox(9)
                        onClick()
                    }
                },
                viewModel = viewModel,
                playerName = playerName
            )
        }
    }
}

/**
 * Show the single player game screen
 */

@Composable
fun TicTacToeSinglePlayerScreen(
    viewModel: TicTacToeViewModel,
    uiState: UiState,
    onPlayAgain: () -> Unit,
    playerName: String
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(BackGround)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier
                .size(150.dp)
                .padding(20.dp), elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (uiState.player_Turn == "X") Primery else { Secondery})) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("X", fontWeight = FontWeight.Bold, fontSize = 50.sp)
                }
            }
            Card(modifier = Modifier
                .size(150.dp)
                .padding(20.dp), elevation = 5.dp, backgroundColor = Secondery, border = BorderStroke(2.dp, if (uiState.player_Turn == "O") Primery else { Secondery})) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("O", fontWeight = FontWeight.Bold, fontSize = 50.sp)
                }
            }
        }
        Spacer(modifier = Modifier.weight(2f))
        SinglePlayerButtonGrid(
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