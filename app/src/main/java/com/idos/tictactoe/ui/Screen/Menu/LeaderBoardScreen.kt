package com.idos.tictactoe.ui.Screen.Menu

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
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
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.data.MainPlayerUiState

@SuppressLint("UnrememberedMutableState")
@Composable
fun LeaderBoardScreen(
    yourPlayer: String
) {
    var i by remember {
        mutableIntStateOf(1)
    }
    //players list in database
    val playerList = mutableStateListOf<MainPlayerUiState?>()
    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Players")

    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            val list = snapshot.children
            //sort players list by players' score
            val sortedList = list.sortedByDescending {
                it.getValue(MainPlayerUiState::class.java)?.score
            }

            //take first 10 players
            for (d in sortedList) {
                //add every player to player list
                val p: MainPlayerUiState? = d.getValue(MainPlayerUiState::class.java)
                //first 10 players
                playerList.add(p)
                i++
                if(i == 10) {
                    break
                }

            }

        }

        override fun onCancelled(error: DatabaseError) {}
    })

    ShowTopPlayers(Players = playerList, yourPlayer = yourPlayer)
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
        Text(
            text = "LeaderBoard",
            color = colors.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = screenHeight.value.sp * 0.05,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(screenHeight.value.times(0.05).dp))

        Card(
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
                        DisplayPlayer(
                            player = item!!,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color.Transparent)
                                .wrapContentHeight()
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
        ShowPlayerData(player = player, onCloseClicked = { showPlayer = false })
    }
}

//show player's information
@Composable
fun ShowPlayerData(player: MainPlayerUiState, onCloseClicked: () -> Unit) {
    Dialog(
        onDismissRequest = onCloseClicked,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        PlayerCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding((LocalConfiguration.current.screenWidthDp * 16 / 412).dp),
            profile = player,
            currentImage = player.currentImage,
            currentX = player.currentX,
            currentO = player.currentO
        )
    }
}

@Composable
fun DisplayPlayer(
    player: MainPlayerUiState,
    modifier: Modifier,
    index: Int,
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

    Card(
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
                contentAlignment = AbsoluteAlignment.CenterLeft
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    Text(
                        "$index",
                        fontSize = screenHeight.value.sp * 0.02,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                    Text(
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
                contentAlignment = AbsoluteAlignment.CenterRight
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
fun getPlayer(email: String): MainPlayerUiState {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Players")
    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            val list = snapshot.children
            player = try {
                list.find {
                    it.getValue(MainPlayerUiState::class.java)!!.email == email
                }?.getValue(MainPlayerUiState::class.java)!!
            } catch (_: Exception) { MainPlayerUiState() }

        }

        override fun onCancelled(error: DatabaseError) {

        }
    })

    return player
}