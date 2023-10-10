package com.idos.tictactoe.ui.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Primery
import com.idos.tictactoe.ui.theme.Secondery

@Composable
fun ShowGameFinalScore(Text: String, game: com.idos.tictactoe.data.OnlineGameUiState, player1: com.idos.tictactoe.data.MainPlayerUiState, player2: com.idos.tictactoe.data.MainPlayerUiState, navController: NavController) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = Text, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(40.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .size(150.dp)
                        .padding(20.dp),
                    elevation = 5.dp,
                    backgroundColor = Secondery,
                    shape = RoundedCornerShape(125.dp)
                    ) {
                    Image(
                        painter = painterResource(id = player1.currentImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Text(player1.name, fontSize = 10.sp, color = Color.White)
                }
            Text("${game.player1Score} : ${game.player2Score}", fontWeight = FontWeight.Bold, color = Color.White)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .size(150.dp)
                        .padding(20.dp),
                    elevation = 5.dp,
                    backgroundColor = Secondery,
                    shape = RoundedCornerShape(125.dp)
                ) {
                    Image(
                        painter = painterResource(id = player2.currentImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Text(player2.name, fontSize = 10.sp, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(200.dp))
        Button(onClick = {navController.navigate(GameScreen.Start.name)}, colors = ButtonDefaults.buttonColors(backgroundColor = Primery)) {
            Text(text = "Home", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}