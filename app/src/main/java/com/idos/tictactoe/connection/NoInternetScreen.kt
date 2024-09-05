package com.idos.tictactoe.connection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.idos.tictactoe.ui.screen.GameScreen
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun NoInternetConnectionScreen(navController: NavController, onPlayOffline: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp

    var tryAgain by remember {
        mutableStateOf(false)
    }
    var navigate by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(brush), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                colors = ButtonDefaults.buttonColors(colors.onPrimary),
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

            Spacer(modifier = Modifier.height((screenHeight / 30).dp))

            Button(
                onClick = {tryAgain = true},
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight(),
                colors = ButtonDefaults.buttonColors(colors.onPrimary),
                shape = CircleShape,
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Text(
                    text = "Try again",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = screenHeight.sp * 0.035,
                    color = colors.primary
                )
            }
        }
    }

    if(tryAgain) {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(0.5f),
                strokeWidth = (screenWidth*0.01).dp,
                color = colors.primary,
                strokeCap = StrokeCap.Round
            )
        }

        val connection by connectivityState()
        Timer().schedule((500..1000).random().toLong()) {
            tryAgain = false
            if (connection == ConnectionState.Available) {
                navigate = true
            }
        }
    }

    if(navigate) {
        navigate = false
        navController.navigate(GameScreen.Home.title)
    }
}