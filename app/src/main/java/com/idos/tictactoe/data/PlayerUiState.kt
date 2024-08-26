package com.idos.tictactoe.data

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.R
import com.idos.tictactoe.ui.Screen.Game.Online.MyTurn
import com.idos.tictactoe.ui.Screen.Game.Online.OnlineGameRememberedValues
import com.idos.tictactoe.ui.Screen.Game.Online.onlineGameId
import java.util.Timer
import kotlin.concurrent.schedule

//data class for every player in database
data class MainPlayerUiState(
    //database key
    var key: String = "",
    //player's name
    var name: String = "",
    //player's email
    var email: String = "",
    //player's score
    var score: Int = 0,
    //coins
    var coins: Int = 0,
    //player's image
    var currentImage: String = "xo_1",
    //images that the player got
    var unlockedImages: List<String> = listOf("xo_1"),
    //images that the player didn't got
    var lockedImages: List<String> = listOf(
        "xo_2",
        "xo_3",
        "xo_4",
        "xo_5",
        "xo_6",
        "xo_7",
        "xo_8",
        "xo_9",
        "xo_10",
        "xo_11",
        "xo_12",
        "xo_13",
        "xo_14",
        "xo_15",
    ),
    var wins: Int = 0,
    var loses: Int = 0,
    var level: Int = 1,
    var unlockedX: List<String> = listOf("x_1"),
    var unlockedO: List<String> = listOf("o_1"),
    var lockedX: List<String> = listOf(
        "x_2",
        "x_3",
        "x_4",
        "x_5",
        "x_6",
        "x_7",
        "x_8",
        "x_9",
        "x_10",
        "x_11",
        "x_12",
        "x_13",
        "x_14",
        "x_15"
        ),
    var lockedO: List<String> = listOf(
        "o_2",
        "o_3",
        "o_4",
        "o_5",
        "o_6",
        "o_7",
        "o_8",
        "o_9",
        "o_10",
        "o_11",
        "o_12",
        "o_13",
        "o_14",
        "o_15"
    ),
    var currentX: String = "x_1",
    var currentO: String= "o_1",
    var onlineTimeLimit: Int = 40
)

fun GetXO(xo: String): Int {
    return when(xo) {
        "xo_1"-> R.drawable.xo_1
        "xo_2"-> R.drawable.xo_2
        "xo_3"-> R.drawable.xo_3
        "xo_4"-> R.drawable.xo_4
        "xo_5"-> R.drawable.xo_5
        "xo_6"-> R.drawable.xo_6
        "xo_7"-> R.drawable.xo_7
        "xo_8"-> R.drawable.xo_8
        "xo_9"-> R.drawable.xo_9
        "xo_10"-> R.drawable.xo_10
        "xo_11"-> R.drawable.xo_11
        "xo_12"-> R.drawable.xo_12
        "xo_13"-> R.drawable.xo_13
        "xo_14"-> R.drawable.xo_14
        "xo_15"-> R.drawable.xo_15
        else -> 0
    }
}

fun GetX(x: String): Int {
    return when(x) {
        "x_1"-> R.drawable.x_1
        "x_2"-> R.drawable.x_2
        "x_3"-> R.drawable.x_3
        "x_4"-> R.drawable.x_4
        "x_5"-> R.drawable.x_5
        "x_6"-> R.drawable.x_6
        "x_7"-> R.drawable.x_7
        "x_8"-> R.drawable.x_8
        "x_9"-> R.drawable.x_9
        "x_10"-> R.drawable.x_10
        "x_11"-> R.drawable.x_11
        "x_12"-> R.drawable.x_12
        "x_13"-> R.drawable.x_13
        "x_14"-> R.drawable.x_14
        "x_15"-> R.drawable.x_15
        else -> 0
    }
}

fun GetO(o: String): Int {
    return when(o) {
        "o_1"-> R.drawable.o_1
        "o_2"-> R.drawable.o_2
        "o_3"-> R.drawable.o_3
        "o_4"-> R.drawable.o_4
        "o_5"-> R.drawable.o_5
        "o_6"-> R.drawable.o_6
        "o_7"-> R.drawable.o_7
        "o_8"-> R.drawable.o_8
        "o_9"-> R.drawable.o_9
        "o_10"-> R.drawable.o_10
        "o_11"-> R.drawable.o_11
        "o_12"-> R.drawable.o_12
        "o_13"-> R.drawable.o_13
        "o_14"-> R.drawable.o_14
        "o_15"-> R.drawable.o_15
        else -> 0
    }
}

@Composable
fun MainPlayerUiState.Draw(
    modifier: Modifier,
    screenWidth: Int,
    shape: RoundedCornerShape,
    border: BorderStroke
) {
    val colors = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        shape = shape,
        border = border
    ) {
        Image(
            painter = painterResource(id = GetXO(currentImage)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
        )
    }
    Text(
        text = name,
        fontSize = screenWidth.sp * 0.05,
        color = colors.onBackground
    )
}

@Composable
fun MainPlayerUiState.CountDownTimerWrite(
    playerNumber: Int,
    gameState: OnlineGameRememberedValues,
    databaseReference: DatabaseReference,
    modifier: Modifier,
    screenWidth: Int,
    screenHeight: Int,
    shape: RoundedCornerShape,
    border: BorderStroke,
) {
    val colors = MaterialTheme.colorScheme

    var timeLeft by remember {
        mutableIntStateOf(10)
    }
    var setDelay by remember {
        mutableStateOf(true)
    }
    var reset by remember {
        mutableStateOf(false)
    }

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            //find game
            try {
                gameState.game = snapshot.children.find {
                    it.getValue(OnlineGameUiState::class.java)!!.id == onlineGameId
                }?.getValue(OnlineGameUiState::class.java)!!
            } catch (_: Exception) { }

            if (playerNumber == 1) {
                timeLeft = gameState.game.player1TimeLeft
            } else {
                timeLeft = gameState.game.player2TimeLeft
            }

            reset = gameState.game.foundWinner || gameState.game.times == 9

        }

        override fun onCancelled(error: DatabaseError) {}
    })



    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = modifier,
            shape = shape,
            border = border
        ) {
            Image(
                painter = painterResource(id = GetXO(currentImage)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
            )
        }

        Spacer(modifier = Modifier.height((screenHeight * 20 / 900).dp))

        Text(
            text = name,
            fontSize = screenWidth.sp * 0.075,
            color = colors.onBackground
        )
        Spacer(modifier = Modifier.height((screenHeight * 5 / 900).dp))
        Text(
            text = timeLeft.toString(),
            fontSize = screenWidth.sp * 0.075,
            color = colors.onBackground
        )
    }

    if (timeLeft > 0) {
        if (setDelay && MyTurn == gameState.game.playerTurn && !reset) {
            setDelay = false
            Timer().schedule(1000){
                if(MyTurn == gameState.game.playerTurn && !reset) {
                    databaseReference
                        .child(onlineGameId)
                        .child("player${playerNumber}TimeLeft")
                        .setValue(timeLeft-1)
                }
                setDelay = true
            }
        }
    }
}


@Composable
fun MainPlayerUiState.CountDownTimerRead(
    playerNumber: Int,
    gameState: OnlineGameRememberedValues,
    databaseReference: DatabaseReference,
    modifier: Modifier,
    screenWidth: Int,
    screenHeight: Int,
    shape: RoundedCornerShape,
    border: BorderStroke
) {
    val colors = MaterialTheme.colorScheme

    var timeLeft by remember {
        mutableIntStateOf(
            if (playerNumber == 1) {
                gameState.game.player1TimeLeft
            } else {
                gameState.game.player2TimeLeft
            }
        )
    }

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            //find game
            try {
                gameState.game = snapshot.children.find {
                    it.getValue(OnlineGameUiState::class.java)!!.id == onlineGameId
                }?.getValue(OnlineGameUiState::class.java)!!
            } catch (_: Exception) { }

            if (playerNumber == 1) {
                timeLeft = gameState.game.player1TimeLeft
            } else {
                timeLeft = gameState.game.player2TimeLeft
            }

        }

        override fun onCancelled(error: DatabaseError) {}
    })



    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = modifier,
            shape = shape,
            border = border
        ) {
            Image(
                painter = painterResource(id = GetXO(currentImage)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
            )
        }

        Spacer(modifier = Modifier.height((screenHeight * 20 / 900).dp))

        Text(
            text = name,
            fontSize = screenWidth.sp * 0.075,
            color = colors.onBackground
        )
        Spacer(modifier = Modifier.height((screenHeight * 5 / 900).dp))
        Text(
            text = timeLeft.toString(),
            fontSize = screenWidth.sp * 0.075,
            color = colors.onBackground
        )
    }
}