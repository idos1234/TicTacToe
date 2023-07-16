package com.example.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.AppViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore

/**
 * [SettingScreen] Show the settings screen
 */

@JvmOverloads
@Composable
fun SettingScreen(
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onClearClick: () -> Unit
) {

    val isDialogOpenUiState by viewModel.isDialogOpen.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(BackGround)) {

        Text(text = "Settings", fontWeight = FontWeight.Bold, fontSize = 70.sp)
        
        Spacer(modifier = Modifier.height(50.dp))
        
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxSize()
                .background(BackGround)
        ) {
            item {
                ShowTopPlayersButton(
                    settingsViewModel = viewModel,
                    uiState = isDialogOpenUiState,
                    context = LocalContext.current
                )
            }

            item {
                Spacer(modifier = Modifier.height(50.dp))
            }

            item {
                ClearDataButton(
                    settingsViewModel = viewModel,
                    uiState = isDialogOpenUiState,
                    onClearClick = onClearClick
                )
            }
        }
    }
}

/**
 * Show the players from the data
 */

@SuppressLint("UnrememberedMutableState")
@Composable
fun ShowTopPlayersButton(
    settingsViewModel: SettingsViewModel,
    uiState: isDialogOpen,
    context: Context
) {

    var playerlist = mutableStateListOf<MainPlayerUiState?>()
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    db.collection("Players").get()
        .addOnSuccessListener { queryDocumentSnapshots ->
            if (!queryDocumentSnapshots.isEmpty) {
                val list = queryDocumentSnapshots.documents
                for (d in list) {
                    val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
                    playerlist.add(p)

                }
                playerlist.sortBy {
                    it?.score
                }
            } else {
                // if the snapshot is empty we are displaying
                // a toast message.
                Toast.makeText(
                    context,
                    "No data found in Database",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener {
            Toast.makeText(
                context,
                "Fail to get the data.",
                Toast.LENGTH_SHORT
            ).show()
        }


    Button(
         onClick = {settingsViewModel.ChangeShowingPlayersAlertDialog()},
         colors = ButtonDefaults.buttonColors(Secondery), shape = Shapes.large, modifier = Modifier.width(200.dp)) {
         Text(text = "Top Scores", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black, modifier = Modifier.padding(4.dp))
     }
    if (uiState.isShowingPlayersDialogOpen) {
        ShowTopPlayers(
            Players = playerlist,
            settingsViewModel = settingsViewModel,
        )
    }
}

@Composable
fun ShowTopPlayers(
    Players: SnapshotStateList<MainPlayerUiState?>,
    settingsViewModel: SettingsViewModel
) {

    AlertDialog(
        backgroundColor = BackGround,
        title = {
            IconButton(onClick = {settingsViewModel.ChangeShowingPlayersAlertDialog()}) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.height(300.dp).fillMaxWidth()) {
                 Card(modifier = Modifier.width(250.dp), backgroundColor = Title, border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(0)) {
                     Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {

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
                 }
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .height(300.dp)
                ) {
                    itemsIndexed(Players) { index, item ->
                        Card(
                            modifier = Modifier
                                .width(250.dp),
                            border = BorderStroke(1.dp, Color.Black),
                            backgroundColor = if(index == 0) First else Secondery,
                            shape = RoundedCornerShape(0)
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
               },
        onDismissRequest = {},
        confirmButton = {}
    )
}


/**
 * This function clear the players data from the game
 */

@Composable
fun ClearDataButton(
    settingsViewModel: SettingsViewModel,
    uiState: isDialogOpen,
    onClearClick: () -> Unit
) {

    Button(
        onClick = {
            settingsViewModel.ChangeCheckClearDataAlertDialog()
        },
        colors = ButtonDefaults.buttonColors(Secondery),
    ) {
        Text(text = "Clear Data", textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
    }

    if (uiState.isCheckClearDataDialogOpen) {
        CheckClearData(
            onCancelClick = {settingsViewModel.ChangeCheckClearDataAlertDialog()},
            onClearClick = onClearClick
        )
    }
}

@Composable
fun CheckClearData(
    onCancelClick: () -> Unit,
    onClearClick: () -> Unit
) {

    AlertDialog(
        title = { Text(text = "Are you sure you want to clear all data?") },
        backgroundColor = BackGround,
        onDismissRequest = {},
        dismissButton = {
            TextButton(
                onClick = { onCancelClick() }
            ) {
                Text("Cancel")
            }
                        },
            confirmButton = {
                TextButton(
                    onClick = onClearClick
                ) {
                   Text("Clear")
                }
            }
        )
}