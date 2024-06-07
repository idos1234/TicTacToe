package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.firestore.FirebaseFirestore
import com.idos.tictactoe.data.GetO
import com.idos.tictactoe.data.GetX
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Secondery

@SuppressLint("UnrememberedMutableState")
@Composable
fun LeaderBoardScreen(
    context: Context,
    yourPlayer: String
) {
    var i by remember {
        mutableIntStateOf(0)
    }
    //players list in database
    var playerlist = mutableStateListOf<MainPlayerUiState?>()
    //database
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
                    //first 10 players
                    playerlist.add(p)
                    i++
                    if(i == 10) {
                        break
                    }

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
    

    ShowTopPlayers(Players = playerlist, yourPlayer = yourPlayer)
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

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    Column(modifier = Modifier
        .fillMaxSize()
        .background(brush = brush),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        androidx.compose.material3.Text(
            text = "LeaderBoard",
            color = colors.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = screenHeight.value.sp * 0.05,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(screenHeight.value.times(0.05).dp))

        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxSize(0.8f),
            colors = CardDefaults.cardColors(containerColor = colors.primary)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = screenHeight.value.times(0.05).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Players.forEachIndexed { index, item ->
                    item {
                        desplayPlayer(
                            player = item!!,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color.Transparent)
                                .height(screenHeight.value.times(0.05).dp)
                                .padding(5.dp)
                                .clickable(
                                    onClick = {
                                        showPlayer = true
                                        player = item
                                    }
                                )
                                .align(Alignment.CenterHorizontally),
                            index + 1,
                            colors,
                            screenHeight,
                            screenWidth,
                            item.email == yourPlayer
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight.value.times(0.05).dp))
    }

    //show player's information on player click
    if (showPlayer) {
        showPlayerData(player = player, onCloseClicked = { showPlayer = false })
    }
}

//show player's information
@Composable
fun showPlayerData(player: MainPlayerUiState, onCloseClicked: () -> Unit) {
    val currentX = GetX(player.currentX)
    val currentO = GetO(player.currentO)
    val currentImage = GetXO(player.currentImage)
    Dialog(
        onDismissRequest = onCloseClicked
    ) {
        Card(
            colors = CardDefaults.cardColors(Secondery),
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
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
                            painter = painterResource(currentImage),
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
                        Image(painter = painterResource(id = currentX) , contentDescription = "Player's X", modifier = Modifier
                            .weight(1f)
                            .background(BackGround))
                        Spacer(modifier = Modifier.height(5.dp))
                        Image(painter = painterResource(id = currentO) , contentDescription = "Player's O", modifier = Modifier
                            .weight(1f)
                            .background(BackGround))
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)) {
                    Row(horizontalArrangement = Arrangement.Start) {
                        Card(Modifier.size(15.dp), colors = CardDefaults.cardColors(Color.Red)){}
                        Text(text = "Wins = ${player.wins}", Modifier.padding(start = 4.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.Start) {
                        Card(Modifier.size(15.dp), colors = CardDefaults.cardColors(Color.Yellow)){}
                        Text(text = "Loses = ${player.loses}", Modifier.padding(start = 4.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.Start) {
                        Card(Modifier.size(15.dp), colors = CardDefaults.cardColors(Color.Green)){}
                        Text(text = "Score: ${player.score}", Modifier.padding(start = 4.dp))
                    }
                }
                PlayerGraph(
                    profile = player,
                    winsColor = Color.Red,
                    losesColor = Color.Yellow,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(50)),
                )
            }
        }
    }
}

@Composable
fun desplayPlayer(
    player: MainPlayerUiState,
    modifier: Modifier, index: Int,
    colorScheme: ColorScheme, screenHeight: Dp,
    screenWidth: Dp, isYourPlayer: Boolean
) {
    val color = if(index == 1) {
        Color(0xFFFFC400)
    } else if(isYourPlayer) {
        Color(0xFF3455FF)
    } else {
        colorScheme.onPrimary
    }

    androidx.compose.material3.Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    androidx.compose.material3.Text(
                        "$index",
                        fontSize = screenHeight.value.sp * 0.02,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                    androidx.compose.material3.Text(
                        ".",
                        fontSize = screenHeight.value.sp * 0.02,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                    Image(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Image",
                        modifier = Modifier.size(((screenWidth.value * 0.8 - 60) / 6 / 2).dp)
                    )
                }
            }
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    player.name,
                    fontSize = screenHeight.value.sp * 0.02,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    player.score.toString(),
                    fontSize = screenHeight.value.sp * 0.02,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun getPlayer(email: String, context: Context): MainPlayerUiState {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }

    //database
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    //get Players collection from database
    db.collection("Players").get()
        //on success
        .addOnSuccessListener { queryDocumentSnapshots ->
            //check if collection is empty
            if (!queryDocumentSnapshots.isEmpty) {
                val list = queryDocumentSnapshots.documents
                try {
                    player = list.find {
                        it.toObject(MainPlayerUiState::class.java)!!.email == email
                    }?.toObject(MainPlayerUiState::class.java)!!
                } catch (e: Exception) {
                    player = MainPlayerUiState()
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

    return player
}