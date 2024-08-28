package com.idos.tictactoe.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.idos.tictactoe.ui.Screen.Game.Online.DotsFlashing

@Composable
fun OpponentLeftGame(onClick: () -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val colors = MaterialTheme.colorScheme

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.15f)
                .background(colors.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "Your opponent has left the game",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = screenHeight.sp * 0.02,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground
                )

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(0.4f),
                    colors = ButtonDefaults.buttonColors(colors.primary)
                ) {
                    Text(
                        text = "Ok",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = screenHeight.sp * 0.02,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimary
                    )
                }
            }
        }
    }
}


@Composable
fun NoInternetDialog(navController: NavController, onPlayOffline: () -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val colors = MaterialTheme.colorScheme

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .background(colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height((screenHeight / 50).dp))

            Text(
                text = "No internet",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = screenHeight.sp * 0.05,
                color = colors.onBackground
            )
            Text(
                text = "connection",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = screenHeight.sp * 0.05,
                color = colors.onBackground
            )

            Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = "No Wifi",
                modifier = Modifier
                    .fillMaxWidth()
                    .size((screenWidth * 0.8).dp),
                tint = colors.onBackground
            )

            Spacer(modifier = Modifier.height((screenHeight / 20).dp))

            Button(
                onClick = {
                    onPlayOffline()
                    navController.navigate(GameScreen.Home.title)
                },
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight(),
                colors = ButtonDefaults.buttonColors(colors.onBackground),
                shape = CircleShape,
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Text(
                    text = "Play offline",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = screenHeight.sp * 0.035,
                    color = colors.primary
                )
            }

            Spacer(modifier = Modifier.height((screenHeight / 50).dp))
        }
    }
}


@Composable
fun WaitingForServerResponse() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp

    val colors = MaterialTheme.colorScheme

    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .background(colors.background, CircleShape)
                .fillMaxWidth(0.9f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Waiting for server response",
                    fontSize = screenHeight.value.sp * 0.0175,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center
                )
                DotsFlashing(size = screenWidth * 10 / 450)
            }
        }
    }
}

@Composable
fun TimeUp(navController: NavController) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    Box(modifier = Modifier
        .background(brush)
        .fillMaxSize()) {
        Dialog(
            onDismissRequest = { navController.navigate(GameScreen.Home.title) },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .background(colors.primary)
                    .fillMaxWidth(0.85f)
                    .padding((LocalConfiguration.current.screenWidthDp * 12 / 412).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "There are no players online",
                    fontSize = screenHeight.value.sp * 0.02,
                    color = colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = "Please try again later",
                    fontSize = screenHeight.value.sp * 0.02,
                    color = colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.height(screenHeight*16/915))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    TextButton(
                        onClick = { navController.navigate(GameScreen.Home.title) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Back",
                            color = colors.onBackground,
                            fontSize = screenHeight.value.sp * 0.02
                        )
                    }
                    TextButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Try again",
                            color = colors.onBackground,
                            fontSize = screenHeight.value.sp * 0.02
                        )
                    }
                }
            }
        }
    }
}