package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.firestore.FirebaseFirestore
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.ui.theme.*

@SuppressLint("UnrememberedMutableState")
@Composable
fun LeaderBoardScreen(
    context: Context,
    yourPlayer: String
) {
    //players list in database
    var playerlist = mutableStateListOf<MainPlayerUiState?>()
    //database
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    //get Players collection from database
    db.collection("Players").get()
        //on success
        .addOnSuccessListener { queryDocumentSnapshots ->
            //check if collection is empty
            if (!queryDocumentSnapshots.isEmpty) {
                val list = queryDocumentSnapshots.documents
                for (d in list) {
                    //add every player to player list
                    val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
                    playerlist.add(p)

                }
                //sort players list by players' score
                playerlist.sortByDescending  {
                    it?.score
                }
            }
        }
        //on failure
        .addOnFailureListener {
            Toast.makeText(
                context,
                "Fail to get the data.",
                Toast.LENGTH_SHORT
            ).show()
        }
    
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(BackGround)) {
        Text(text = "World LeaderBoard", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(75.dp))
        ShowTopPlayers(Players = playerlist, yourPlayer = yourPlayer)
    }
}

@Composable
fun ShowTopPlayers(
    Players: SnapshotStateList<MainPlayerUiState?>,
    yourPlayer: String
) {

    var showPlayer by remember {
        mutableStateOf(false)
    }
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }
    //table header
    Card(modifier = Modifier.fillMaxWidth(.9f), border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(10)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Primery),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Place",
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Player",
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Score",
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                //for every player show player's score, name, rank
                itemsIndexed(Players) { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    showPlayer = true
                                    player = item!!
                                }
                            ),
                        border = BorderStroke(1.dp, Color.Black),
                        backgroundColor = if (item!!.name == yourPlayer) You else Secondery,
                        shape = RoundedCornerShape(0),
                    ) {
                        Row {
                            Text(
                                text = index.plus(1).toString(),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            Players[index]?.name?.let {
                                Text(
                                    text = it,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Players[index]?.score?.let {
                                Text(
                                    text = it.toString(),
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    //show player's information on player click
    if (showPlayer) {
        showPlayer(player = player, onCloseClicked = { showPlayer = false })
    }
}

//show player's information
@Composable
fun showPlayer(player: MainPlayerUiState, onCloseClicked: () -> Unit) {
    Dialog(
        onDismissRequest = onCloseClicked
    ) {
        Card(backgroundColor = Secondery, elevation = 10.dp, modifier = Modifier.fillMaxWidth(0.9f)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.align(Alignment.Start)) {
                    IconButton(onClick = onCloseClicked) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(player.currentImage),
                            contentDescription = null,
                            Modifier
                                .size(110.dp)
                                .weight(1f)
                        )
                        Text(text = player.name, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .weight(1f)
                        .height(130.dp)) {
                        Image(painter = painterResource(id = player.currentX) , contentDescription = "Player's X", modifier = Modifier
                            .weight(1f)
                            .background(BackGround))
                        Spacer(modifier = Modifier.height(5.dp))
                        Image(painter = painterResource(id = player.currentO) , contentDescription = "Player's O", modifier = Modifier
                            .weight(1f)
                            .background(BackGround))
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)) {
                    Row(horizontalArrangement = Arrangement.Start) {
                        Card(Modifier.size(15.dp), backgroundColor = Color.Red){}
                        Text(text = "Wins = ${player.wins}", Modifier.padding(start = 4.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.Start) {
                        Card(Modifier.size(15.dp), backgroundColor = Color.Yellow){}
                        Text(text = "Loses = ${player.loses}", Modifier.padding(start = 4.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.Start) {
                        Card(Modifier.size(15.dp), backgroundColor = Color.Green){}
                        Text(text = "Score: ${player.score}", Modifier.padding(start = 4.dp))
                    }
                }
                PlayerGraph(
                    profile = player,
                    winsColor = Color.Red,
                    losesColor = Color.Yellow,
                    modifier = Modifier.fillMaxWidth(),
                    backGroundColor = Secondery
                )
            }
        }
    }
}

