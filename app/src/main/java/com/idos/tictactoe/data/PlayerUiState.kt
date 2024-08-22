package com.idos.tictactoe.data

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
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
import kotlin.math.absoluteValue

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
    var currentO: String= "o_1"
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
    imageSize: Dp,
    currentGame: OnlineGameRememberedValues,
    reset: Boolean,
    databaseReference: DatabaseReference,
    playerNumber: Int
) {
    val startDurationInSeconds = 10
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val colors = MaterialTheme.colorScheme

    var currentTime by remember {
        mutableStateOf(startDurationInSeconds)
    }

    var targetValue by remember {
        mutableStateOf(100f)
    }

    val progress by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(currentTime * 1000, easing = LinearEasing)
    )
    if(!reset) {
        currentTime = progress.toInt() / 10
    }

    databaseReference.child(onlineGameId).child("player${playerNumber}Progress").setValue(progress)

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            //find game
            try {
                currentGame.game = snapshot.children.find {
                    it.getValue(OnlineGameUiState::class.java)!!.id == onlineGameId
                }?.getValue(OnlineGameUiState::class.java)!!

                if(currentGame.game.foundWinner) {
                    if(reset) {
                        currentTime = 0
                    }
                    targetValue = 100f
                }
            } catch (_: Exception) {}

        }

        override fun onCancelled(error: DatabaseError) {}
    })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // This is the progress path which wis changed using path measure
        val pathWithProgress by remember {
            mutableStateOf(Path())
        }

        // using path
        val pathMeasure by remember { mutableStateOf(PathMeasure()) }

        val path = remember {
            Path()
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(imageSize)) {
            Canvas(modifier = Modifier.size(imageSize)) {

                if (path.isEmpty) {
                    path.addRoundRect(
                        RoundRect(
                            Rect(offset = Offset.Zero, size),
                            cornerRadius = CornerRadius(
                                (screenWidth*20/400).dp.toPx(),
                                (screenWidth*20/400).dp.toPx(),
                            )
                        )
                    )
                }
                pathWithProgress.reset()

                pathMeasure.setPath(path, forceClosed = false)
                pathMeasure.getSegment(
                    startDistance = 0f,
                    stopDistance = pathMeasure.length * progress / 100f,
                    pathWithProgress,
                    startWithMoveTo = true
                )

                drawPath(
                    path = path,
                    style = Stroke(
                        (screenWidth * 10 / 400).dp.toPx(),
                    ),
                    color = Color.Gray
                )

                drawPath(
                    path = pathWithProgress,
                    style = Stroke(
                        (screenWidth * 10 / 400).dp.toPx(),
                    ),
                    color = colors.tertiary
                )
            }

            Card(
                modifier = Modifier.size(imageSize),
                shape = RoundedCornerShape((screenWidth*20/400).dp)
            ) {
                Image(
                    painter = painterResource(id = GetXO(currentImage)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape((screenWidth * 20 / 400).dp))
                )
            }
        }

        Spacer(modifier = Modifier.height((screenHeight*10/900).dp))

        Text(
            text = name,
            fontSize = screenWidth.sp * 0.075,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height((screenHeight*5/900).dp))

        Text(
            text = "%.1f".format(progress / 10),
            fontSize = screenWidth.sp * 0.075,
            color = colors.onBackground
        )

    }

    if (progress > 0f) {
        if (MyTurn == currentGame.game.playerTurn && !reset) {
            targetValue = 0f
        } else {
            targetValue = progress.absoluteValue
        }
    }
}


@Composable
fun MainPlayerUiState.CountDownTimerRead(
    imageSize: Dp,
    currentGame: OnlineGameRememberedValues,
    databaseReference: DatabaseReference,
    playerNumber: Int
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val colors = MaterialTheme.colorScheme

    var progress by remember {
        mutableStateOf(100f)
    }

    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            //find game
            try {
                currentGame.game = snapshot.children.find {
                    it.getValue(OnlineGameUiState::class.java)!!.id == onlineGameId
                }?.getValue(OnlineGameUiState::class.java)!!
            } catch (_: Exception) { }

            if (playerNumber == 1) {
                progress = currentGame.game.player1Progress
            } else {
                progress = currentGame.game.player2Progress
            }

        }

        override fun onCancelled(error: DatabaseError) {}
    })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // This is the progress path which wis changed using path measure
        val pathWithProgress by remember {
            mutableStateOf(Path())
        }

        // using path
        val pathMeasure by remember { mutableStateOf(PathMeasure()) }

        val path = remember {
            Path()
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(imageSize)) {
            Canvas(modifier = Modifier.size(imageSize)) {

                if (path.isEmpty) {
                    path.addRoundRect(
                        RoundRect(
                            Rect(offset = Offset.Zero, size),
                            cornerRadius = CornerRadius(
                                (screenWidth * 20 / 400).dp.toPx(),
                                (screenWidth * 20 / 400).dp.toPx(),
                            )
                        )
                    )
                }
                pathWithProgress.reset()

                pathMeasure.setPath(path, forceClosed = false)
                pathMeasure.getSegment(
                    startDistance = 0f,
                    stopDistance = pathMeasure.length * progress / 100f,
                    pathWithProgress,
                    startWithMoveTo = true
                )

                drawPath(
                    path = path,
                    style = Stroke(
                        (screenWidth * 10 / 400).dp.toPx(),
                    ),
                    color = Color.Gray
                )

                drawPath(
                    path = pathWithProgress,
                    style = Stroke(
                        (screenWidth * 10 / 400).dp.toPx(),
                    ),
                    color = colors.tertiary
                )
            }

            Card(
                modifier = Modifier.size(imageSize),
                shape = RoundedCornerShape((screenWidth * 20 / 400).dp)
            ) {
                Image(
                    painter = painterResource(id = GetXO(currentImage)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape((screenWidth * 20 / 400).dp))
                )
            }
        }

        Spacer(modifier = Modifier.height((screenHeight * 10 / 900).dp))

        Text(
            text = name,
            fontSize = screenWidth.sp * 0.075,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height((screenHeight * 5 / 900).dp))

        Text(
            text = "%.1f".format(progress / 10),
            fontSize = screenWidth.sp * 0.075,
            color = colors.onBackground
        )

    }
}