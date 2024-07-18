package com.idos.tictactoe.ui.Screen.Game.Online

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.R
import com.idos.tictactoe.data.Boxes
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.Screen.GameScreen
import com.idos.tictactoe.ui.Screen.Menu.getPlayer
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SearchGameScreen(
    navController: NavHostController,
    player: String,
    currentGame: OnlineGameRememberedValues,
    viewModel: CodeGameViewModel,
    ) {

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")

    var times by remember {
        mutableStateOf(1)
    }

    var waitingTimeFlag by remember {
        mutableStateOf(true)
    }
    var foundPlayer by remember {
        mutableStateOf(false)
    }

    if(waitingTimeFlag) {
        waitingTimeFlag = false

        Timer().schedule(30000) {
            if(currentGame.game.player2 == "" && navController.currentDestination?.route == GameScreen.SearchGame.title) {
                viewModel.removeGame(onlineGameId, databaseReference, 0){
                    navController.navigate(GameScreen.TimeUp.title)
                }
            }
        }
    }


    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            while (times == 1) {
                Loop@ for (Game in snapshot.children) {
                    val game = Game.getValue(OnlineGameUiState::class.java)
                    //enter room
                    if ((game!!.player2 == "")) {
                        val updatedGame = OnlineGameUiState(
                            id = game.id,
                            player1 = game.player1,
                            player2 = player,
                            winner = game.winner,
                            boxes = game.boxes
                        )
                        onlineGameId = game.id
                        if (game.id != "") {
                            databaseReference.child(game.id).child("player2").setValue(player)
                        }
                        currentGame.game = updatedGame
                        MyTurn = "O"
                        break@Loop
                    }
                }
                //open room
                if (currentGame.game == OnlineGameUiState()) {
                    val key: String = databaseReference.push().key!!.takeLast(5)
                    val newGame = OnlineGameUiState(
                        id = key,
                        player1 = player,
                        player2 = "",
                        winner = "",
                        boxes = Boxes()
                    )
                    onlineGameId = key
                    databaseReference.child(key).setValue(newGame)
                    currentGame.game = newGame
                    MyTurn = "X"
                }
                times++
            }
            //find game
            try {
                currentGame.game = snapshot.children.find {
                    it.getValue(OnlineGameUiState::class.java)!!.id == onlineGameId
                }?.getValue(OnlineGameUiState::class.java)!!
            } catch (e: Exception) {
                currentGame.game = OnlineGameUiState()
            }

            if(currentGame.game.player2 != "") {
                foundPlayer = true
            }
        }

        //on failure
        override fun onCancelled(error: DatabaseError) {
        }
    }
    )

    if (foundPlayer && !wasGameStarted) {
        wasGameStarted = true
        currentGame.player1 = getPlayer(email = currentGame.game.player1)
        currentGame.player2 = getPlayer(email = currentGame.game.player2)
        navController.navigate("${GameScreen.Online.title}/Games")
    }

    SearchGameUi(navController)
}

@Composable
private fun SearchGameUi(
    navController: NavHostController,
    context: Context = LocalContext.current
) {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp
    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))


    val rotationState = rememberInfiniteTransition(label = "")
    val rotation by rotationState.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5000
                0f at 0 with LinearEasing
                360f at 5000 with LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = brush), contentAlignment = Alignment.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cloud),
                    contentDescription = "Search",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(0.12f),
                    tint = colors.primary
                )
                Icon(
                    painter = painterResource(id = R.drawable.cloud),
                    contentDescription = "Search",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(0.15f),
                    tint = colors.primary
                )
                Icon(
                    painter = painterResource(id = R.drawable.cloud),
                    contentDescription = "Search",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(0.1f),
                    tint = colors.primary
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = size.width / 4
                val amplitude = size.width / 8

                val angle = Math.toRadians(rotation.toDouble())
                val x = centerX + (radius * cos(angle)).toFloat()
                val y = centerY + (amplitude * sin(angle * 2)).toFloat()

                val iconSize = (screenWidth.value / 2).dp
                val strokeWidth = 10f
                val glassSize = iconSize.value / 2

                drawCircle(
                    color = colors.onPrimary,
                    radius = glassSize,
                    center = Offset(x, y),
                    style = Stroke(width = strokeWidth)
                )
                drawCircle(
                    color = colors.onPrimary,
                    radius = glassSize,
                    center = Offset(x, y),
                    colorFilter = ColorFilter.tint(Color.White.copy(0.5f))
                )

                drawLine(
                    color = colors.onPrimary,
                    start = Offset(x + glassSize / 2, y + glassSize / 2),
                    end = Offset(x + glassSize * 1.5f, y + glassSize * 1.5f),
                    strokeWidth = strokeWidth
                )
            }
        }
        Column {
            Button(
                onClick = {
                    deleteGame(
                        context,
                        FirebaseDatabase.getInstance().getReference("Games")
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(0.3f),

                colors = ButtonDefaults.buttonColors(containerColor = colors.error)
            ) {
                Text(
                    text = "Leave",
                    fontSize = LocalConfiguration.current.screenHeightDp.sp * 0.03,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onError
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
        }
    }
}