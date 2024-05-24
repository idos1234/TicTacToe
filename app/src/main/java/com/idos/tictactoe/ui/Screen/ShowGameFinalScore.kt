package com.idos.tictactoe.ui.Screen

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.ui.Online.CodeGameViewModel
import com.idos.tictactoe.ui.Online.MyTurn
import com.idos.tictactoe.ui.Online.OnlineGameRememberedValues
import com.idos.tictactoe.ui.Online.OnlineGameService
import com.idos.tictactoe.ui.Online.findGame
import com.idos.tictactoe.ui.Online.onlineGameId
import com.idos.tictactoe.ui.Online.otherPlayerQuit
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Primery
import com.idos.tictactoe.ui.theme.Secondery

@Composable
fun ShowGameFinalScore(
    uiState: OnlineGameRememberedValues,
    navController: NavController,
    codeGameViewModel: CodeGameViewModel,
    context: Context = LocalContext.current
) {
    val player1CurrentImage = GetXO(uiState.player1.currentImage)
    val player2CurrentImage = GetXO(uiState.player1.currentImage)

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Games")
    uiState.game = findGame(gameId = onlineGameId, databaseReference = databaseReference)
    if(uiState.game.player2Quit || uiState.game.player1Quit) {
        otherPlayerQuit = true
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(BackGround)
        .fillMaxSize()) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = uiState.FinalScoreText, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                        painter = painterResource(id = player1CurrentImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Text(uiState.player1.name, fontSize = 10.sp, color = Color.White)
                }
            Text("${uiState.game.player1Score} : ${uiState.game.player2Score}", fontWeight = FontWeight.Bold, color = Color.White)

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
                        painter = painterResource(id = player2CurrentImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Text(uiState.player2.name, fontSize = 10.sp, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(200.dp))
        Button(
            onClick = {
                //other player quit
                if(otherPlayerQuit) {
                    //deletes game
                    CodeGameViewModel().removeGame(onlineGameId, databaseReference, 0) {
                        context.stopService(
                            Intent(
                                context,
                                OnlineGameService::class.java
                            )
                        )
                    }
                } else {
                    //player quit
                    CodeGameViewModel().playerQuit(MyTurn, databaseReference, onlineGameId)
                    context.stopService(
                        Intent(
                            context,
                            OnlineGameService::class.java
                        )
                    )
                }
                otherPlayerQuit = false

                //clears code after quit game
                codeGameViewModel.clearCode()
                navController.navigate(GameScreen.Start.title)
                      },
            colors = ButtonDefaults.buttonColors(backgroundColor = Primery)
        ) {
            Text(text = "Home", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}