package com.idos.tictactoe.ui.Screen.Menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.network.Connection.ConnectionState
import com.example.network.Connection.connectivityState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.data.MainPlayerUiState

@Composable
fun Settings(
    playerEmail: String,
    botDifficulty: Int,
    changeBotDifficulty: (Int) -> Unit,
    onDone: () -> Unit
) {
    val connection by connectivityState()

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    //show dialog
    var showBotDifficultyDialog by remember {
        mutableStateOf(false)
    }
    var showOnlineTimeLimitDialog by remember {
        mutableStateOf(false)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(brush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game settings",
                fontSize = screenHeight.value.sp * 0.05,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.3f))

            //button 1 - online time limit
            Button(
                onClick = {
                    if (connection == ConnectionState.Available) {
                        showOnlineTimeLimitDialog = true
                    }
                          },
                modifier = Modifier.fillMaxWidth(0.95f),
                colors = ButtonDefaults.buttonColors(if(connection == ConnectionState.Available) {colors.primary} else {Color.Gray})
            ) {
                Text(
                    text = "Change online time limit",
                    fontSize = screenHeight.value.sp * 0.02,
                    fontWeight = FontWeight.Bold,
                    color =
                    if (connection == ConnectionState.Available) {
                        colors.onPrimary
                    } else {
                        Color.Black
                    }
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.05f))

            //button 2 - bot difficulty
            Button(
                onClick = { showBotDifficultyDialog = true },
                modifier = Modifier.fillMaxWidth(0.95f),
                colors = ButtonDefaults.buttonColors(colors.primary)
            ) {
                Text(
                    text = "Change bot difficulty",
                    fontSize = screenHeight.value.sp * 0.02,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimary
                )
            }
        }
    }

    if (showBotDifficultyDialog) {
        ChangeBotDifficulty(
            playerEmail = playerEmail,
            botDifficulty = botDifficulty,
            changeBotDifficulty = changeBotDifficulty,
            onDone = {
                onDone()
                showBotDifficultyDialog = false
            }
        )
    } else if (showOnlineTimeLimitDialog) {
        ChangeOnlineTimeLimit(
            playerEmail = playerEmail,
            onDone = {showOnlineTimeLimitDialog = false}
        )
    }
}

@Composable
fun ChangeBotDifficulty(
    playerEmail: String,
    botDifficulty: Int,
    changeBotDifficulty: (Int) -> Unit,
    onDone: () -> Unit
) {

    val colors = MaterialTheme.colorScheme

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp


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
                    it.getValue(MainPlayerUiState::class.java)!!.email == playerEmail
                }?.getValue(MainPlayerUiState::class.java)!!
            } catch (_: Exception) { MainPlayerUiState() }

        }

        override fun onCancelled(error: DatabaseError) {}
    })

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //header
            Text(
                text = "Change difficulty",
                fontSize = screenHeight.value.sp * 0.04,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.05f))

            // easy level
            Card(
                onClick = {
                    changeBotDifficulty(1)
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(50),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    if (botDifficulty != 1) {
                        colors.primary.copy(0.6f)
                    } else {
                        colors.primary
                    }
                )
            ) {
                Text(
                    text = "Easy",
                    fontSize = screenHeight.value.sp * 0.04,
                    fontWeight = FontWeight.Bold,
                    color = if (botDifficulty != 1) {
                        colors.onPrimary.copy(0.6f)
                    } else {
                        colors.onPrimary
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.01f))

            // medium level
            Card(
                onClick = {
                    changeBotDifficulty(2)
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(50),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    if (botDifficulty != 2) {
                        colors.primary.copy(0.6f)
                    } else {
                        colors.primary
                    }
                )
            ) {
                Text(
                    text = "Medium",
                    fontSize = screenHeight.value.sp * 0.04,
                    fontWeight = FontWeight.Bold,
                    color = if (botDifficulty != 2) {
                        colors.onPrimary.copy(0.6f)
                    } else {
                        colors.onPrimary
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.01f))

            //hard level
            Card(
                onClick = {
                    changeBotDifficulty(3)
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(50),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    if (botDifficulty != 3) {
                        colors.primary.copy(0.6f)
                    } else {
                        colors.primary
                    }
                )
            ) {
                Text(
                    text = "Hard",
                    fontSize = screenHeight.value.sp * 0.04,
                    fontWeight = FontWeight.Bold,
                    color = if (botDifficulty != 3) {
                        colors.onPrimary.copy(0.6f)
                    } else {
                        colors.onPrimary
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.04f))

            Card(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth(0.5f),
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(colors.primary)
            ) {
                Text(
                    text = "Done",
                    fontSize = screenHeight.value.sp * 0.04,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
        }
    }
}

@Composable
fun ChangeOnlineTimeLimit(
    playerEmail: String,
    onDone: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp


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
                    it.getValue(MainPlayerUiState::class.java)!!.email == playerEmail
                }?.getValue(MainPlayerUiState::class.java)!!
            } catch (_: Exception) { MainPlayerUiState() }

        }

        override fun onCancelled(error: DatabaseError) {}
    })

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //header
            Text(
                text = "Change time limit",
                fontSize = screenHeight.value.sp * 0.04,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.05f))

            // easy level
            Card(
                onClick = {
                    databaseReference.child(player.key).child("onlineTimeLimit").setValue(40)
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(50),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    if (player.onlineTimeLimit != 40) {
                        colors.primary.copy(0.6f)
                    } else {
                        colors.primary
                    }
                )
            ) {
                Text(
                    text = "Easy - 40 sec",
                    fontSize = screenHeight.value.sp * 0.04,
                    fontWeight = FontWeight.Bold,
                    color = if (player.onlineTimeLimit != 40) {
                        colors.onPrimary.copy(0.6f)
                    } else {
                        colors.onPrimary
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.01f))

            // medium level
            Card(
                onClick = {
                    databaseReference.child(player.key).child("onlineTimeLimit").setValue(30)
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(50),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    if (player.onlineTimeLimit != 30) {
                        colors.primary.copy(0.6f)
                    } else {
                        colors.primary
                    }
                )
            ) {
                Text(
                    text = "Medium - 30 sec",
                    fontSize = screenHeight.value.sp * 0.04,
                    fontWeight = FontWeight.Bold,
                    color = if (player.onlineTimeLimit != 30) {
                        colors.onPrimary.copy(0.6f)
                    } else {
                        colors.onPrimary
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.01f))

            //hard level
            Card(
                onClick = {
                    databaseReference.child(player.key).child("onlineTimeLimit").setValue(15)
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(50),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    if (player.onlineTimeLimit != 15) {
                        colors.primary.copy(0.6f)
                    } else {
                        colors.primary
                    }
                )
            ) {
                Text(
                    text = "Hard - 15 sec",
                    fontSize = screenHeight.value.sp * 0.04,
                    fontWeight = FontWeight.Bold,
                    color = if (player.onlineTimeLimit != 15) {
                        colors.onPrimary.copy(0.6f)
                    } else {
                        colors.onPrimary
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.04f))

            Card(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth(0.5f),
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(colors.primary)
            ) {
                Text(
                    text = "Done",
                    fontSize = screenHeight.value.sp * 0.04,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
        }
    }
}