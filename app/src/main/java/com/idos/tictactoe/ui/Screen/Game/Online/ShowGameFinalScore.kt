package com.idos.tictactoe.ui.Screen.Game.Online

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.idos.tictactoe.R
import com.idos.tictactoe.data.Draw
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.OnlineGameUiState
import com.idos.tictactoe.ui.Screen.GameScreen
import com.idos.tictactoe.ui.Screen.Menu.getNextLevelScore
import com.idos.tictactoe.ui.Screen.Menu.getPrevLevelScore
import kotlinx.coroutines.delay

@Composable
fun GameScoreDialogFriendly(
    gameState: OnlineGameRememberedValues,
    navController: NavController,
    context: Context,
) {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("GamesWithCode")
    gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
    if(gameState.game.player2Quit || gameState.game.player1Quit) {
        otherPlayerQuit = true
    }

    val colors = MaterialTheme.colorScheme

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .background(color = Color.Transparent),
            colors = CardDefaults.cardColors(colors.background),
            shape = RoundedCornerShape(12),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = (LocalConfiguration.current.screenHeightDp / 100).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.Absolute.Left) {
                    Text(
                        text = gameState.FinalScoreText,
                        fontSize = LocalConfiguration.current.screenHeightDp.sp * 0.03,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground
                    )
                    Text(
                        text = "!",
                        fontSize = LocalConfiguration.current.screenHeightDp.sp * 0.03,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground
                    )
                }
                ShowPlayersDialog(gameState = gameState)
                Spacer(modifier = Modifier.height(LocalConfiguration.current.screenWidthDp.dp / 3))
                Button(
                    onClick = {
                        deleteGame(context, databaseReference)

                        gameState.game = OnlineGameUiState()
                        navController.navigate(GameScreen.Home.title)
                    },
                    colors = ButtonDefaults.buttonColors(colors.primary),
                    modifier = Modifier.fillMaxWidth(0.4f)
                ) {
                    Text(
                        text = "Home",
                        fontSize = LocalConfiguration.current.screenHeightDp.sp * 0.03,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimary
                    )
                }
            }
        }
    }
}

enum class LevelStatus {
    LevelUp,
    LevelDown,
    SameLevel
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun GameScoreDialog(
    gameState: OnlineGameRememberedValues,
    navController: NavController,
    context: Context,
    coins: Int,
    levelStatus: LevelStatus,
    playerUiState: MainPlayerUiState,
    won: Boolean
) {
    var delete by remember {
        mutableStateOf(false)
    }


    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")
    gameState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
    if(gameState.game.player2Quit || gameState.game.player1Quit) {
        otherPlayerQuit = true
    }

    val colors = MaterialTheme.colorScheme

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .background(color = Color.Transparent),
            colors = CardDefaults.cardColors(colors.background),
            shape = RoundedCornerShape(12),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = (LocalConfiguration.current.screenHeightDp / 100).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.Absolute.Left) {
                    Text(
                        text = gameState.FinalScoreText,
                        fontSize = LocalConfiguration.current.screenHeightDp.sp * 0.03,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground
                    )
                    Text(
                        text = "!",
                        fontSize = LocalConfiguration.current.screenHeightDp.sp * 0.03,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground
                    )
                }
                //show players
                ShowPlayersDialog(gameState = gameState)
                //level
                val level = when (levelStatus) {
                    LevelStatus.LevelUp -> playerUiState.level - 1
                    LevelStatus.LevelDown -> playerUiState.level + 1
                    LevelStatus.SameLevel -> playerUiState.level
                }
                val maxValue = when (levelStatus) {
                    LevelStatus.LevelUp -> getNextLevelScore(level) - getPrevLevelScore(level)
                    LevelStatus.LevelDown -> getNextLevelScore(level-1) - getPrevLevelScore(level-1)
                    LevelStatus.SameLevel -> getNextLevelScore(level) - getPrevLevelScore(level)
                }
                val currentValue = when (levelStatus) {
                    LevelStatus.LevelUp -> playerUiState.score - getPrevLevelScore(level)
                    LevelStatus.LevelDown -> playerUiState.score - getPrevLevelScore(level-1)
                    LevelStatus.SameLevel -> playerUiState.score - getPrevLevelScore(level)
                }

                if (level != 15) {
                    AnimatedCircularProgressIndicator(
                        currentValue = currentValue,
                        maxValue = maxValue,
                        level = level,
                        progressIndicatorColor = colors.primary,
                        progressBackgroundColor = Color.DarkGray,
                        xp = playerUiState.score,
                        won = won,
                        levelStatus = levelStatus
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(LocalConfiguration.current.screenWidthDp.dp / 3),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "star",
                            tint = Color.Yellow,
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            text = "Max",
                            color = Color.Black,
                            fontSize = LocalConfiguration.current.screenHeightDp.sp * 0.03,
                            fontWeight = FontWeight.ExtraBold,
                            softWrap = false
                        )
                    }
                }
                var animatedCoins by remember {
                    mutableIntStateOf(0)
                }

                if (won) {
                    Row(
                        horizontalArrangement = Arrangement.Absolute.Left,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "+",
                            fontSize = LocalConfiguration.current.screenHeightDp.sp * 0.04,
                            color = colors.onBackground
                        )
                        AddedCoinsAnimation(
                            coins = animatedCoins,
                            color = colors.onBackground
                        )
                        Spacer(modifier = Modifier.fillMaxWidth(0.02f))
                        Image(
                            painter = painterResource(id = R.drawable.coin),
                            contentDescription = "Coin",
                            modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 15)

                        )
                    }



                    LaunchedEffect(Unit) {
                        repeat(coins) {
                            animatedCoins++
                            delay(50)

                        }
                    }
                }
                else {
                    Spacer(modifier = Modifier.height(LocalConfiguration.current.screenWidthDp.dp / 15))
                }

                Button(
                    onClick = {
                        if(!delete) {
                            delete = true
                            deleteGame(context, databaseReference)

                            gameState.game = OnlineGameUiState()
                            navController.navigate(GameScreen.Home.title)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(colors.primary),
                    modifier = Modifier.fillMaxWidth(0.4f)
                ) {
                    Text(
                        text = "Home",
                        fontSize = LocalConfiguration.current.screenHeightDp.sp * 0.03,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AddedCoinsAnimation(
    coins: Int,
    color: Color
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    var oldCount by remember {
        mutableIntStateOf(coins)
    }
    SideEffect {
        oldCount = coins
    }
    Row(horizontalArrangement = Arrangement.Absolute.Left) {
        val countString = coins.toString()
        val oldCountString = oldCount.toString()
        for (i in countString.indices) {
            val oldChar = oldCountString.getOrNull(i)
            val newChar = countString[i]
            val char = if (oldChar == newChar) {
                oldCountString[i]
            } else {
                countString[i]
            }
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { it } with slideOutVertically { -it }
                }
            ) { char ->
                Text(
                    text = char.toString(),
                    color = color,
                    fontSize = screenHeight.value.sp * 0.04,
                    fontWeight = FontWeight.ExtraBold,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
fun ShowPlayersDialog(
    gameState: OnlineGameRememberedValues
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            gameState.player1.Draw(
                modifier = Modifier
                    .size(((screenWidth - 40) / 4).dp),
                screenWidth = screenWidth,
                shape = RoundedCornerShape(20),
                border = BorderStroke(2.dp, if (gameState.game.playerTurn == "X") colors.tertiary else { colors.background})
            )
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = colors.primary),
            modifier = Modifier
                .height(((screenWidth - 40) / 3 / 3).dp)
                .weight(1f)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "${gameState.game.player1Score} : ${gameState.game.player2Score}",
                    fontSize = screenWidth.sp * 0.04,
                    color = colors.onPrimary
                )
            }
        }
        Column(
            Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            gameState.player2.Draw(
                modifier = Modifier
                    .size(((screenWidth - 40) / 4).dp),
                screenWidth = screenWidth,
                shape = RoundedCornerShape(20),
                border = BorderStroke(2.dp, if (gameState.game.playerTurn == "O") colors.tertiary else { colors.background})
            )
        }
    }
}

@Composable
fun AnimatedCircularProgressIndicator(
    currentValue: Int,
    maxValue: Int,
    level: Int,
    progressBackgroundColor: Color,
    progressIndicatorColor: Color,
    modifier: Modifier = Modifier,
    xp: Int,
    won: Boolean,
    levelStatus: LevelStatus
) {
    val colors = MaterialTheme.colorScheme
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val stroke = with(LocalDensity.current) {
        Stroke(width = (screenHeight/90).toPx(), cap = StrokeCap.Butt, join = StrokeJoin.Round)
    }
    var animatedLevel  by remember {
        mutableIntStateOf(level)
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        ProgressStatus(
            level = animatedLevel,
            color = colors.onBackground,
            xp = xp
        )

        val animateFloat = remember {
            Animatable(
                if(levelStatus != LevelStatus.LevelDown) {
                    if (won) {
                        (currentValue - 1) / maxValue.toFloat()
                    } else {
                        (currentValue + 1) / maxValue.toFloat()
                    }
                } else {
                    0f
                }
            )
        }
        LaunchedEffect(animateFloat) {
            when(levelStatus) {
                LevelStatus.LevelUp -> {
                    animateFloat.animateTo(
                        targetValue = currentValue / maxValue.toFloat(),
                        animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
                    )
                    animateFloat.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 0, easing = LinearOutSlowInEasing)
                    )
                    ++animatedLevel
                }
                LevelStatus.LevelDown -> {
                    animateFloat.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 0, easing = LinearOutSlowInEasing)
                    )
                    animateFloat.animateTo(
                        targetValue = currentValue / maxValue.toFloat(),
                        animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
                    )
                    --animatedLevel
                }
                LevelStatus.SameLevel -> {
                    animateFloat.animateTo(
                        targetValue = currentValue / maxValue.toFloat(),
                        animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
                    )
                }
            }
        }


        Canvas(
            Modifier
                .progressSemantics(currentValue / maxValue.toFloat())
                .size(LocalConfiguration.current.screenWidthDp.dp / 3)
        ) {
            // Start at 12 O'clock
            val startAngle = 270f
            val sweep: Float = animateFloat.value * 360f
            val diameterOffset = stroke.width / 2

            drawCircle(
                color = progressBackgroundColor,
                style = stroke,
                radius = size.minDimension / 2.0f - diameterOffset
            )
            drawCircularProgressIndicator(startAngle, sweep, progressIndicatorColor, stroke)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ProgressStatus(
    level: Int,
    color: Color,
    xp: Int
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    var oldCount by remember {
        mutableIntStateOf(level)
    }
    SideEffect {
        oldCount = level
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.Absolute.Left) {
            val countString = level.toString()
            val oldCountString = oldCount.toString()
            for (i in countString.indices) {
                val oldChar = oldCountString.getOrNull(i)
                val newChar = countString[i]
                val char = if (oldChar == newChar) {
                    oldCountString[i]
                } else {
                    countString[i]
                }
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        slideInVertically { it } with slideOutVertically { -it }
                    }
                ) { char ->
                    Text(
                        text = char.toString(),
                        color = color,
                        fontSize = screenHeight.value.sp * 0.04,
                        fontWeight = FontWeight.ExtraBold,
                        softWrap = false
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.Absolute.Left) {
            Text(
                text = xp.toString(),
                color = color,
                fontSize = screenHeight.value.sp * 0.02,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = Modifier.width(screenHeight/200))
            Text(
                text = "XP",
                color = color,
                fontSize = screenHeight.value.sp * 0.02,
                fontWeight = FontWeight.ExtraBold,

            )
        }
    }
}

private fun DrawScope.drawCircularProgressIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}