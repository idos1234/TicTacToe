package com.example.tictactoe.ui.Screen

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
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnrememberedMutableState")
@JvmOverloads
@Composable
fun LeaderBoardScreen(
    context: Context
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
                playerlist.sortBy {
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
        ShowTopPlayers(Players = playerlist)
    }
}

@Composable
fun ShowTopPlayers(
    Players: SnapshotStateList<MainPlayerUiState?>,
) {

    var showPlayer by remember {
        mutableStateOf(false)
    }
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }
    //table header
    Card(modifier = Modifier.width(250.dp), border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(10)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
            .fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().background(Primery),
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
                            .width(250.dp)
                            .clickable(
                                onClick = {
                                    showPlayer = true
                                    player = item!!
                                }
                            ),
                        border = BorderStroke(1.dp, Color.Black),
                        backgroundColor = Secondery,
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
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(backgroundColor = Secondery, elevation = 10.dp) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        shape = RoundedCornerShape(125.dp),
                        elevation = 10.dp,
                        modifier = Modifier
                            .size(90.dp)
                    ) {
                        Image(imageVector = Icons.Default.TagFaces, contentDescription = null, contentScale = ContentScale.FillBounds)
                    }

                    Spacer(modifier = Modifier.width(15.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = player.name, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Score: ${player.score}", fontSize = 20.sp, fontWeight = FontWeight.W500)

                    }
                }
            }
        }
    }
}