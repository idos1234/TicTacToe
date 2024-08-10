package com.idos.tictactoe.ui.Screen.Menu

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.network.Connection.ConnectionState
import com.example.network.Connection.connectivityState
import com.idos.tictactoe.ui.Screen.Game.Online.OnlineGameService
import com.idos.tictactoe.ui.theme.Shapes

/**
 * [HomeScreen] Show the home screen
 */

@Composable
fun HomeScreen(
    onTwoPlayersClick: () -> Unit = {},
    onSinglePlayerClick: () -> Unit = {},
    onFriendlyBattleClick: () -> Unit,
    onOnlineClick: () -> Unit = {},
    context: Context
) {
    val connection by connectivityState()

    var showTrainingGames by remember {
        mutableStateOf(false)
    }

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    val colors = MaterialTheme.colorScheme
    val type = MaterialTheme.typography
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(brush = brush)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        //"XO" text- Big
        Text(
            text = "xo",
            style = type.headlineLarge.copy(
                drawStyle = Stroke(
                    width = 6f,
                    join = StrokeJoin.Round
                )
            ),
            fontSize = screenHeight.value.sp * 0.25,
            color = colors.onBackground,
            textAlign = TextAlign.Center
        )
        //"Tic Tac Toe" text- Small
        Text(
            "Tic Tac Toe",
            color = colors.onBackground,
            fontSize = screenHeight.value.sp * 0.02,
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.2f))

        //the buttons
        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxSize()
            ) {
                //training games button
                Button(
                    onClick = { showTrainingGames = true },
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth(0.5f),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                ) {
                    Text(
                        text = "Training",
                        fontSize = screenHeight.value.sp*0.03,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onPrimary
                    )
                }

                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                //online game button
                Button(
                    onClick = {
                        if (connection == ConnectionState.Available) {
                            context.startService(Intent(context, OnlineGameService::class.java))
                            onOnlineClick()
                        }
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth(0.5f),
                    colors = ButtonDefaults.buttonColors(if(connection == ConnectionState.Available) {colors.primary} else {Color.Gray})
                ) {
                    Text(
                        text = "Play",
                        style = type.bodyMedium,
                        fontSize = screenHeight.value.sp*0.03,
                        fontWeight = FontWeight.SemiBold,
                        color = if(connection == ConnectionState.Available) {colors.onPrimary} else {Color.Black}
                    )
                }

                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                //online game button
                Button(
                    onClick = {
                        if (connection == ConnectionState.Available) {
                            onFriendlyBattleClick()
                        }
                              },
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth(0.7f),
                    colors = ButtonDefaults.buttonColors(if(connection == ConnectionState.Available) {colors.primary} else {Color.Gray})
                ) {
                    Text(
                        text = "Friendly Battle",
                        style = type.bodyMedium,
                        fontSize = screenHeight.value.sp*0.03,
                        fontWeight = FontWeight.SemiBold,
                        color = if(connection == ConnectionState.Available) {colors.onPrimary} else {Color.Black}
                    )
                }
            }
        }

        if (showTrainingGames) {
            TrainingGames(onTwoPlayersClick, onSinglePlayerClick, onCloseClicked = {showTrainingGames = false})
        }

        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
    }
}

//training games dialog(offline)
@Composable
fun TrainingGames(onTwoPlayersClick: () -> Unit = {}, onSinglePlayerClick: () -> Unit = {}, onCloseClicked: () -> Unit = {}) {
    val colors = MaterialTheme.colorScheme
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    Dialog(
        onDismissRequest = onCloseClicked,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = colors.background),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.wrapContentWidth().wrapContentHeight()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {

                Row(modifier = Modifier.align(Alignment.Start)) {
                    IconButton(onClick = onCloseClicked) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = colors.onBackground
                        )
                    }
                }

                Text(
                    text = "Training",
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onBackground,
                    fontSize = screenHeight.value.sp*0.03,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                //single player
                Button(
                    onClick = onSinglePlayerClick,
                    colors = ButtonDefaults.buttonColors(colors.primary),
                    shape = Shapes.large,

                    ) {
                    Text(
                        text = "Single Player",
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        fontSize = screenHeight.value.sp*0.02,
                        color = colors.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                //two players
                Button(
                    onClick = onTwoPlayersClick,
                    colors = ButtonDefaults.buttonColors(colors.primary),
                    shape = Shapes.large,
                ) {
                    Text(
                        text = "Two Players",
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        fontSize = screenHeight.value.sp*0.02,
                        color = colors.onPrimary
                    )
                }

            }
        }
    }
}