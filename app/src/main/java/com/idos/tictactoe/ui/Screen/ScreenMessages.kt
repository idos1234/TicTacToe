package com.idos.tictactoe.ui.Screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

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