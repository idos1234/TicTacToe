package com.idos.tictactoe.ui.Screen.Menu

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.idos.tictactoe.data.GetO
import com.idos.tictactoe.data.GetX
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.MainPlayerUiState

@Composable
fun ProfileScreen(player: String, context: Context) {
    var profile by remember {
        mutableStateOf(MainPlayerUiState())
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

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    //get Players collection from database
    profile = getPlayer(player, context)

    val currentX = GetX(profile.currentX)
    val currentO = GetO(profile.currentO)
    val currentImage = GetXO(profile.currentImage)

    val scoreFromCurrentLevel = getPrevLevelScore(profile.level)
    val scoreToNextLevel = getNextLevelScore(profile.level)
    val progress:Float = if(profile.level != 15) {
        (profile.score-scoreFromCurrentLevel)/(scoreToNextLevel-scoreFromCurrentLevel).toFloat()
    } else {
        1f
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .background(brush)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(16.dp),
            colors = CardDefaults.cardColors(colors.primary),
            elevation = CardDefaults.cardElevation(20.dp),
            shape = RoundedCornerShape(40.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Left,
                modifier = Modifier.padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .size(150.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //image
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.7f)
                            .weight(2f),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Image(
                            painter = painterResource(id = currentImage),
                            contentDescription = "Player's image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20))
                        )
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.Black,
                            modifier = Modifier.clickable(onClick = { showPhotos = true })
                        )
                    }
                    //x and o
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.Left,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(0.7f)
                            .padding(5.dp)
                    ) {
                        //x
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.5.dp)
                                .clickable(
                                    onClick = { showPlayerX = true }
                                ),
                            shape = RoundedCornerShape(20),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.onPrimary)
                        ) {
                            Image(
                                painter = painterResource(id = currentX),
                                contentDescription = "Player's X",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        //o
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.5.dp)
                                .clickable(
                                    onClick = { showPlayerO = true }
                                ),
                            shape = RoundedCornerShape(20),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.onPrimary)
                        ) {
                            Image(
                                painter = painterResource(id = currentO),
                                contentDescription = "Player's O",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    //name
                    Text(
                        text = profile.name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onBackground
                    )

                    Box(modifier = Modifier.padding(top = 16.dp)) {
                        //wins
                        Column {
                            Text(
                                text = "Wins: ${profile.wins}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.onBackground
                            )
                            //loses
                            Text(
                                text = "Loses: ${profile.loses}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.onBackground
                            )
                        }
                    }
                }
            }
        }

        //progress to the next rank
        Box(modifier = Modifier.fillMaxWidth(0.9f)) {
            Column(Modifier.fillMaxWidth()) {
                //progress line
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CustomLinearProgressIndicator(
                        progress = progress,
                        progressColor = colors.secondary,
                        backgroundColor = Color.DarkGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(15.dp),
                        clipShape = RoundedCornerShape(20.dp)
                    )
                    var text by remember {
                        mutableStateOf("")
                    }
                    var size by remember {
                        mutableStateOf(0.sp)
                    }
                    var color by remember {
                        mutableStateOf(colors.primary)
                    }
                    if (profile.level == 15) {
                        text = "Max"
                        size = 15.sp
                    } else {
                        text = "Level: ${profile.level}"
                        size = 10.sp
                    }

                    if (progress > 0.5f) {
                        color = colors.onSecondary
                    } else {
                        color = Color.White
                    }
                    Text(
                        text = text,
                        color = color,
                        fontSize = size,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left,
                ) {
                    //stars from this level
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .weight(1f),
                        contentAlignment = AbsoluteAlignment.CenterLeft
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Absolute.Left
                        ) {
                            Text(
                                text = scoreFromCurrentLevel.toString(),
                                fontSize = 20.sp,
                                color = colors.secondary,
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = Color.Yellow
                            )
                        }
                    }
                    //stars to next level
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .weight(1f),
                        contentAlignment = AbsoluteAlignment.CenterRight
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Absolute.Left
                        ) {
                            Text(
                                text = scoreToNextLevel.toString(),
                                fontSize = 20.sp,
                                color = colors.secondary,
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = Color.Yellow
                            )
                        }
                    }
                }
            }
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
fun ShowPlayersImages(player: MainPlayerUiState, onCloseClicked: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    var changeImage by remember {
        mutableStateOf(false)
    }
    var Image by remember {
        mutableStateOf(player.currentImage)
    }
    Dialog(onDismissRequest = onCloseClicked) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(colors.background)
            .wrapContentHeight()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.unlockedImages) { photo ->
                        AsyncImage(
                            model = GetXO( photo ),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .border(
                                    BorderStroke(if (photo == Image) 2.dp else 0.dp, colors.onBackground),
                                    shape = RoundedCornerShape(0)
                                )
                                .clickable(onClick = {
                                    changeImage = true
                                    Image = photo
                                })

                        )
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentHeight()
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.lockedImages) { photo ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            AsyncImage(
                                model = GetXO( photo ),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.25f) })
                            )
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "locked image")
                        }
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentHeight()

            )
        }
    }
    if (changeImage) {
        ChangeImage(image = Image, player = player)
    }
}
@Composable
fun ShowPlayerX(player: MainPlayerUiState, onCloseClicked: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    var changeImage by remember {
        mutableStateOf(false)
    }
    var Image by remember {
        mutableStateOf(player.currentX)
    }
    Dialog(onDismissRequest = onCloseClicked) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(colors.background)
            .wrapContentHeight()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.unlockedX) { X ->
                        AsyncImage(
                            model = GetX( X ),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .border(
                                    BorderStroke(if (X == Image) 2.dp else 0.dp, colors.onBackground),
                                    shape = RoundedCornerShape(0)
                                )
                                .clickable(onClick = {
                                    changeImage = true
                                    Image = X
                                })

                        )
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .background(colors.primary)
                    .wrapContentHeight()
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.lockedX) { X ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            AsyncImage(
                                model = GetX( X ),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.25f) })
                            )
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "locked X")
                        }
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .background(colors.primary)
                    .wrapContentHeight()
            )
        }
    }
    if (changeImage) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val updatedPlayer = MainPlayerUiState(
            name = player.name,
            email = player.email,
            score = player.score,
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
fun ShowPlayerO(player: MainPlayerUiState, onCloseClicked: () -> Unit) {
    val  colors = MaterialTheme.colorScheme

    var changeImage by remember {
        mutableStateOf(false)
    }
    var Image by remember {
        mutableStateOf(player.currentO)
    }
    Dialog(onDismissRequest = onCloseClicked) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(colors.background)
            .wrapContentHeight()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.unlockedO) { O ->
                        AsyncImage(
                            model = GetO( O ),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .border(
                                    BorderStroke(if (O == Image) 2.dp else 0.dp, colors.onBackground),
                                    shape = RoundedCornerShape(0)
                                )
                                .clickable(onClick = {
                                    changeImage = true
                                    Image = O
                                })

                        )
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .background(colors.primary)
                    .wrapContentHeight()
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                content = {
                    items(player.lockedO) { O ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            AsyncImage(
                                model = GetO( O ),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.25f) })
                            )
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "locked O")
                        }
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .background(colors.primary)
                    .wrapContentHeight()
            )
        }
    }
    if (changeImage) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val updatedPlayer = MainPlayerUiState(
            name = player.name,
            email = player.email,
            score = player.score,
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

@Composable
fun PlayerGraph(
    profile: MainPlayerUiState,
    winsColor: Color,
    losesColor: Color,
    modifier: Modifier,
    ) {
    Row(
        modifier = modifier.background(Color.White)
    ) {
        if (profile.wins > 0) {
            Card(
                modifier = Modifier
                    .weight(weight = profile.wins.toFloat()),
                colors = CardDefaults.cardColors(winsColor),
                shape = RoundedCornerShape(0)
            ) {
                Text(text = "")
            }
        }
        if (profile.loses > 0) {
            Card(
                modifier = Modifier
                    .weight(weight = profile.loses.toFloat()),
                colors = CardDefaults.cardColors(losesColor),
                shape = RoundedCornerShape(0)
            ) {
                Text(text = "")
            }
        }
    }
}

@Composable
fun ChangeImage(image: String, player: MainPlayerUiState) {
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

@Composable
fun CustomLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color,
    backgroundColor: Color,
    clipShape: RoundedCornerShape = RoundedCornerShape(16.dp)
) {
    Box(
        modifier = modifier
            .clip(clipShape)
            .background(backgroundColor)
            .height(8.dp),
        contentAlignment = AbsoluteAlignment.CenterLeft
    ) {
        Box(
            modifier = Modifier
                .background(progressColor)
                .fillMaxHeight()
                .fillMaxWidth(progress)
        )
    }
}

fun getNextLevelScore(currentLevel: Int): Int {
    return when(currentLevel) {
        1 -> 25
        2 -> 50
        3 -> 100
        4 -> 150
        5 -> 200
        6 -> 300
        7 -> 400
        8 -> 500
        9 -> 600
        10 -> 700
        11 -> 800
        12 -> 900
        13 -> 1000
        14 -> 1200
        else -> 0
    }
}

fun getPrevLevelScore(currentLevel: Int): Int {
    return when(currentLevel) {
        1 -> 0
        2 -> 25
        3 -> 50
        4 -> 100
        5 -> 150
        6 -> 200
        7 -> 300
        8 -> 400
        9 -> 500
        10 -> 600
        11 -> 700
        12 -> 800
        13 -> 900
        14 -> 1000
        else -> 0
    }
}