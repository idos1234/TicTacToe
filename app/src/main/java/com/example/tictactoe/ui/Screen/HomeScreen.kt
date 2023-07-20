package com.example.tictactoe.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tictactoe.R
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes

/**
 * [HomeScreen] Show the home screen
 */

@Composable
fun HomeScreen(onTwoPlayersClick: () -> Unit = {}, onSinglePlayerClick: () -> Unit = {}) {
    var showTrainingGames by remember {
        mutableStateOf(false)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .background(BackGround)
            .fillMaxSize()
    )
    {
        Spacer(modifier = Modifier.weight(2f))
        Text(
            text = stringResource(id = R.string.app_name),
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Italic,
            fontSize = 50.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { showTrainingGames = true },
            colors = ButtonDefaults.buttonColors(Primery),
            shape = Shapes.large,
        ) {
            Text(
                text = "Training games",
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                color = Color.Black
            )
        }

        if (showTrainingGames) {
            TrainingGames(onTwoPlayersClick, onSinglePlayerClick, onCloseClicked = {showTrainingGames = false})
        }

        Spacer(modifier = Modifier.weight(4f))
    }
}

@Composable
fun TrainingGames(onTwoPlayersClick: () -> Unit = {}, onSinglePlayerClick: () -> Unit = {}, onCloseClicked: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(backgroundColor = Secondery, elevation = 4.dp, modifier = Modifier.fillMaxWidth(0.60f)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Row(modifier = Modifier.align(Alignment.Start)) {
                    IconButton(onClick = onCloseClicked) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }

                Text(
                    text = "Training",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onSinglePlayerClick,
                    colors = ButtonDefaults.buttonColors(Primery),
                    shape = Shapes.large,

                    ) {
                    Text(
                        text = "Single Player",
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onTwoPlayersClick,
                    colors = ButtonDefaults.buttonColors(Primery),
                    shape = Shapes.large,
                ) {
                    Text(
                        text = "Two Players",
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    HomeScreen()
}