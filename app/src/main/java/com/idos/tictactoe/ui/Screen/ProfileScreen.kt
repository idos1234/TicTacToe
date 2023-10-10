package com.idos.tictactoe.ui.Screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import coil.compose.AsyncImage
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Primery
import com.idos.tictactoe.ui.theme.Secondery
import com.idos.tictactoe.ui.theme.Shapes
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@Composable
fun ProfileScreen(player: String, context: Context) {
    var profile by remember {
        mutableStateOf(com.idos.tictactoe.data.MainPlayerUiState())
    }
    var showPhotos by remember {
        mutableStateOf(false)
    }
    var showPlayerX by remember {
        mutableStateOf(false)
    }
    var showPlayerO by remember {
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
                    val p: com.idos.tictactoe.data.MainPlayerUiState? = d.toObject(com.idos.tictactoe.data.MainPlayerUiState::class.java)
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
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Card(
                            shape = RoundedCornerShape(125.dp),
                            elevation = 10.dp,
                            modifier = Modifier
                                .size(90.dp)
                        ) {
                            Image(painter = painterResource(id = profile.currentImage), contentDescription = null, contentScale = ContentScale.Crop)
                            }
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "edit icon", modifier = Modifier.clickable(onClick = { showPhotos = true }))
                    }
                    Text(
                        text = profile.name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Card(modifier = Modifier
                    .weight(1f)
                    .size(120.dp)
                    .padding(20.dp), shape = Shapes.small, backgroundColor = Secondery) {
                    Image(
                        painter = painterResource(id = profile.currentX),
                        contentDescription = "player's current X",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier.clickable(
                            onClick = { showPlayerX = true }
                        ))
                }
                Card(modifier = Modifier
                    .weight(1f)
                    .size(120.dp)
                    .padding(20.dp), shape = Shapes.small, backgroundColor = Secondery) {
                    Image(
                        painter = painterResource(id = profile.currentO),
                        contentDescription = "player's current O",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier.clickable(
                            onClick = { showPlayerO = true }
                        ))
                }
            }
        }

        item {
            Column(horizontalAlignment = Alignment.Start) {
                Row(horizontalArrangement = Arrangement.Start) {
                   Card(Modifier.size(15.dp), backgroundColor = Color.Red){}
                    Text(text = "Wins = ${profile.wins}", Modifier.padding(start = 4.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.Start) {
                    Card(Modifier.size(15.dp), backgroundColor = Color.Yellow){}
                    Text(text = "Loses = ${profile.loses}", Modifier.padding(start = 4.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            PlayerGraph(
                profile = profile,
                winsColor = Color.Red,
                losesColor = Color.Yellow,
            )

        }
    }
    if (showPhotos) {
        ShowPlayersImages(player = profile, onCloseClicked = { showPhotos = false })
    }
    if (showPlayerX) {
        ShowPlayerX(player = profile, onCloseClicked = { showPlayerX = false })
    }
    if (showPlayerO) {
        ShowPlayerO(player = profile, onCloseClicked = { showPlayerO = false })
    }
}

@Composable
fun ShowPlayersImages(player: com.idos.tictactoe.data.MainPlayerUiState, onCloseClicked: () -> Unit) {
    var changeImage by remember {
        mutableStateOf(false)
    }
    var Image by remember {
        mutableIntStateOf(player.currentImage)
    }
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
                                .border(
                                    BorderStroke(if (photo == Image) 2.dp else 0.dp, Primery),
                                    shape = RoundedCornerShape(0)
                                )
                                .clickable(onClick = {
                                    changeImage = true
                                    Image = photo
                                })

                        )
                    }
                },
                modifier = Modifier.padding(10.dp).height(200.dp)
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
                modifier = Modifier.padding(10.dp).height(200.dp)
            )

            OutlinedButton(onClick = onCloseClicked) {
                Text(text = "Close")
            }
        }
    }
    if (changeImage) {
        ChangeImage(image = Image, player = player)
    }
}
@Composable
fun ShowPlayerX(player: com.idos.tictactoe.data.MainPlayerUiState, onCloseClicked: () -> Unit) {
    var changeImage by remember {
        mutableStateOf(false)
    }
    var Image by remember {
        mutableStateOf(player.currentX)
    }
    Dialog(onDismissRequest = {}) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Secondery)
            .fillMaxHeight(0.87f)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.unlockedX) { X ->
                        AsyncImage(
                            model = X,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .border(
                                    BorderStroke(if (X == Image) 2.dp else 0.dp, Primery),
                                    shape = RoundedCornerShape(0)
                                )
                                .clickable(onClick = {
                                    changeImage = true
                                    Image = X
                                })

                        )
                    }
                },
                modifier = Modifier.padding(10.dp).height(200.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.lockedX) { X ->
                        Box(contentAlignment = Alignment.Center) {
                            AsyncImage(
                                model = X,
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                colorFilter = tint(color = Color.LightGray, blendMode = BlendMode.Darken)
                            )
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "locked X")
                        }
                    }
                },
                modifier = Modifier.padding(10.dp).height(200.dp)
            )

            OutlinedButton(onClick = onCloseClicked) {
                Text(text = "Close")
            }
        }
    }
    if (changeImage) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val updatedPlayer = com.idos.tictactoe.data.MainPlayerUiState(
            name = player.name,
            email = player.email,
            score = player.score,
            password = player.password,
            currentImage = player.currentImage,
            lockedImages = player.lockedImages,
            unlockedImages = player.unlockedImages,
            wins = player.wins,
            loses = player.loses,
            level = player.level,
            lockedX = player.lockedX,
            unlockedX = player.unlockedX,
            lockedO = player.lockedO,
            unlockedO = player.unlockedO,
            currentO = player.currentO,
            currentX = Image
        )


        db.collection("Players")
            .whereEqualTo("name", player.name)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    db.collection("Players").document(document.id).set(
                        updatedPlayer,
                        SetOptions.merge()
                    )
                }
            }
    }
}

@Composable
fun ShowPlayerO(player: com.idos.tictactoe.data.MainPlayerUiState, onCloseClicked: () -> Unit) {
    var changeImage by remember {
        mutableStateOf(false)
    }
    var Image by remember {
        mutableStateOf(player.currentO)
    }
    Dialog(onDismissRequest = {}) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Secondery)
            .fillMaxHeight(0.87f)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.unlockedO) { O ->
                        AsyncImage(
                            model = O,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .border(
                                    BorderStroke(if (O == Image) 2.dp else 0.dp, Primery),
                                    shape = RoundedCornerShape(0)
                                )
                                .clickable(onClick = {
                                    changeImage = true
                                    Image = O
                                })

                        )
                    }
                },
                modifier = Modifier.padding(10.dp).height(200.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.lockedO) { O ->
                        Box(contentAlignment = Alignment.Center) {
                            AsyncImage(
                                model = O,
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                colorFilter = tint(color = Color.LightGray, blendMode = BlendMode.Darken)
                            )
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "locked O")
                        }
                    }
                },
                modifier = Modifier.padding(10.dp).height(200.dp)
            )

            OutlinedButton(onClick = onCloseClicked) {
                Text(text = "Close")
            }
        }
    }
    if (changeImage) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val updatedPlayer = com.idos.tictactoe.data.MainPlayerUiState(
            name = player.name,
            email = player.email,
            score = player.score,
            password = player.password,
            currentImage = player.currentImage,
            lockedImages = player.lockedImages,
            unlockedImages = player.unlockedImages,
            wins = player.wins,
            loses = player.loses,
            level = player.level,
            lockedO = player.lockedO,
            unlockedO = player.unlockedO,
            lockedX = player.lockedX,
            unlockedX = player.unlockedX,
            currentX = player.currentX,
            currentO = Image
        )


        db.collection("Players")
            .whereEqualTo("name", player.name)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    db.collection("Players").document(document.id).set(
                        updatedPlayer,
                        SetOptions.merge()
                    )
                }
            }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerGraph(
    profile: com.idos.tictactoe.data.MainPlayerUiState,
    winsColor: Color,
    losesColor: Color,
    ) {
    val donutChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("Wins", profile.wins.toFloat(), winsColor),
            PieChartData.Slice("Loses", profile.loses.toFloat(), losesColor),
        ),
        plotType = PlotType.Pie
    )
    val donutChartConfig = PieChartConfig(
        strokeWidth = 100f,
        backgroundColor = BackGround,
        sliceLabelTextColor = BackGround
    )
    DonutPieChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp),
        donutChartData,
        donutChartConfig
    )

}

@Composable
fun ChangeImage(image: Int, player: com.idos.tictactoe.data.MainPlayerUiState) {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val updatedPlayer = player.copy(currentImage = image)

    db.collection("Players")
        .whereEqualTo("name", player.name)
        .get()
        .addOnSuccessListener {
            for (document in it) {
                db.collection("Players").document(document.id).set(
                    updatedPlayer,
                    SetOptions.merge()
                )
            }
        }
}