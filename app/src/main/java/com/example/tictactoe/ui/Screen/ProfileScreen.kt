package com.example.tictactoe.ui.Screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Primery
import com.example.tictactoe.ui.theme.Secondery
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(player: String, context: Context) {
    var profile by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var showPhotos by remember {
        mutableStateOf(false)
    }
    //get database
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    //get Players collection from database
    db.collection("Players").get()
        //on success
        .addOnSuccessListener { queryDocumentSnapshots ->
            //check if collection is empty
            if (!queryDocumentSnapshots.isEmpty) {
                val list = queryDocumentSnapshots.documents
                for (d in list) {
                    val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
                    //find player using database
                    if (p?.name == player){
                        profile = p
                    }

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
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(BackGround)) {
        item {
            Card(backgroundColor = Primery, modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Welcome ${profile.name}",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Score: ${profile.score}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W500
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Card(
                            shape = RoundedCornerShape(125.dp),
                            elevation = 10.dp,
                            modifier = Modifier
                                .size(90.dp)
                        ) {
                            Image(painter = painterResource(id = profile.currentImage), contentDescription = null, contentScale = ContentScale.FillBounds)
                            }
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "edit icon", modifier = Modifier.clickable(onClick = { showPhotos = true }))
                    }
                }
            }
        }
    }
    if (showPhotos) {
        ShowPlayersImages(player = profile, onCloseClicked = { showPhotos = false })
    }
}

@Composable
fun ShowPlayersImages(player: MainPlayerUiState, onCloseClicked: () -> Unit) {
    Dialog(onDismissRequest = {}) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Secondery)
            .fillMaxHeight(0.87f)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.unlockedImages) { photo ->
                        AsyncImage(
                            model = photo,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                    }
                },
                modifier = Modifier.padding(10.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.lockedImages) { photo ->
                        Box(contentAlignment = Alignment.Center) {
                            AsyncImage(
                                model = photo,
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                colorFilter = tint(color = Color.Gray, blendMode = BlendMode.Darken)
                            )
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "locked image")
                        }
                    }
                },
                modifier = Modifier.padding(10.dp)
            )

            OutlinedButton(onClick = onCloseClicked) {
                Text(text = "Close")
            }
        }
    }
}