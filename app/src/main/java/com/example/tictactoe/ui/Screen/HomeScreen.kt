package com.example.tictactoe.ui.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.tictactoe.ui.theme.BackGround
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.R
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(onTwoPlayersClick: () -> Unit = {}, onSinglePlayerClick: () -> Unit = {}) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState= scaffoldState,
        topBar = {
        topHomeScreenBar(
            onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        )
    },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            HomeScreenMenu()
        }
    ){
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
}

@Composable()
fun topHomeScreenBar(onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(45.dp)
        .background(BackGround), verticalAlignment = Alignment.CenterVertically) {
        Image(
            imageVector = Icons.Default.Menu,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .size(30.dp)
                .clickable(
                    onClick = onClick
                ))
        Spacer(modifier = Modifier.weight(3f))
    }
}

@Composable()
fun HomeScreenMenu() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight()) {
        Button(onClick = {}) {
            Text(text = "Home")
        }
        Button(onClick = {}) {
            Text(text = "Settings")
        }
        Button(onClick = {}) {
            Text(text = "About Us")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    HomeScreen()
}