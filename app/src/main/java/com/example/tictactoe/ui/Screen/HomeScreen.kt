package com.example.tictactoe.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.tictactoe.ui.theme.BackGround
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tictactoe.R
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes

@Composable
fun HomeScreen(onTwoPlayersClick: () -> Unit = {}, onSinglePlayerClick: () -> Unit = {}) {
    Row() {
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
                onClick = onSinglePlayerClick,
                colors = ButtonDefaults.buttonColors(Secondery),
                shape = Shapes.large,
            ) {
                Text(
                    text = "Single Player",
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.weight(.5f))

            Button(
                onClick = onTwoPlayersClick,
                colors = ButtonDefaults.buttonColors(Secondery),
                shape = Shapes.large,
            ) {
                Text(
                    text = "Two Players",
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(4f))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    HomeScreen()
}