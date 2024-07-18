package com.idos.tictactoe.ui.Screen.Menu

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.data.GetO
import com.idos.tictactoe.data.GetX
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.MainPlayerUiState

@Composable
private fun PlayerCard(
    modifier: Modifier,
    profile: MainPlayerUiState,
    currentImage: String,
    currentX: String,
    currentO: String
) {
    val colors = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(colors.primary),
        elevation = CardDefaults.cardElevation(20.dp),
        shape = RoundedCornerShape(40.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left,
            modifier = Modifier.padding((LocalConfiguration.current.screenWidthDp*12/412).dp)
        ) {
            Column(
                modifier = Modifier
                    .size((LocalConfiguration.current.screenWidthDp*150/412).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //image
                Image(
                    painter = painterResource(id = GetXO(currentImage)),
                    contentDescription = "Player's image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(0.7f)
                        .weight(2f)
                        .clip(RoundedCornerShape(20))
                )
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
                            .padding(horizontal = 2.5.dp),
                        shape = RoundedCornerShape(20),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.onPrimary)
                    ) {
                        Image(
                            painter = painterResource(id = GetX(currentX)),
                            contentDescription = "Player's X",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    //o
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.5.dp),
                        shape = RoundedCornerShape(20),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.onPrimary)
                    ) {
                        Image(
                            painter = painterResource(id = GetO(currentO)),
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
}

@Composable
private fun LevelBar(
    modifier: Modifier,
    profile: MainPlayerUiState,
    scoreToNextLevel: Int,
    scoreFromCurrentLevel: Int
) {
    val progress:Float = if(profile.level != 15) {
        (profile.score-scoreFromCurrentLevel)/(scoreToNextLevel-scoreFromCurrentLevel).toFloat()
    } else {
        1f
    }

    val colors = MaterialTheme.colorScheme

    Box(modifier = modifier) {
        Column(Modifier.fillMaxWidth()) {
            //progress line
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                CustomLinearProgressIndicator(
                    progress = progress,
                    progressColor = colors.secondary,
                    backgroundColor = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((LocalConfiguration.current.screenHeightDp * 20 / 915).dp),
                    clipShape = RoundedCornerShape(50)
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
                        .weight(1f)
                        .clip(RoundedCornerShape(20)),
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

@Composable
fun ProfileScreen(player: String) {
    var profile by remember {
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
            profile = try {
                list.find {
                    it.getValue(MainPlayerUiState::class.java)!!.email == player
                }?.getValue(MainPlayerUiState::class.java)!!
            } catch (e: Exception) {
                MainPlayerUiState()
            }

        }

        override fun onCancelled(error: DatabaseError) {}
    })

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    val currentX = profile.currentX
    val currentO = profile.currentO
    val currentImage = profile.currentImage

    val scoreFromCurrentLevel = getPrevLevelScore(profile.level)
    val scoreToNextLevel = getNextLevelScore(profile.level)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .background(brush)
    ) {
        //player's card(name, score, wins, loses, skins)
        PlayerCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding((LocalConfiguration.current.screenWidthDp * 16 / 412).dp),
            profile = profile,
            currentImage = currentImage,
            currentX = currentX,
            currentO = currentO
        )


        //progress to the next rank
        LevelBar(
            modifier = Modifier.fillMaxWidth(0.9f),
            profile = profile,
            scoreToNextLevel = scoreToNextLevel,
            scoreFromCurrentLevel = scoreFromCurrentLevel
        )

        var expand by remember {
            mutableStateOf(false)
        }
        val rotationState by animateFloatAsState(
            targetValue = if (expand) 180f else 0f
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                )
                .fillMaxWidth(0.7f)
                .height((LocalConfiguration.current.screenHeightDp * 680 / 915).dp)
                .padding(vertical = (LocalConfiguration.current.screenHeightDp * 12 / 915).dp)
        ) {
            if (expand) {
                var image by remember {
                    mutableStateOf(currentImage)
                }
                var x by remember {
                    mutableStateOf(currentX)
                }
                var o by remember {
                    mutableStateOf(currentO)
                }
                var option by remember {
                    mutableStateOf(1)
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.background), contentAlignment = Alignment.Center) {
                    Row(
                        Modifier
                            .fillMaxWidth(0.9f)
                            .background(colors.background),
                        horizontalArrangement = Arrangement.Absolute.Left,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .size((LocalConfiguration.current.screenWidthDp * 0.7 * 0.9 / 4).dp)
                                .clickable(onClick = { option = 1 }),
                            shape = RoundedCornerShape(20),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = GetXO(image)),
                                contentDescription = "Player's Image",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width((LocalConfiguration.current.screenWidthDp * 0.7 * 0.9 / 8).dp))
                        Card(
                            modifier = Modifier
                                .size((LocalConfiguration.current.screenWidthDp * 0.7 * 0.9 / 4).dp)
                                .clickable(onClick = { option = 2 }),
                            shape = RoundedCornerShape(20),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.onPrimary)
                        ) {
                            Image(
                                painter = painterResource(id = GetX(x)),
                                contentDescription = "Player's X",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width((LocalConfiguration.current.screenWidthDp * 0.7 * 0.9 / 8).dp))
                        Card(
                            modifier = Modifier
                                .size((LocalConfiguration.current.screenWidthDp * 0.7 * 0.9 / 4).dp)
                                .clickable(onClick = { option = 3 }),
                            shape = RoundedCornerShape(20),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.onPrimary)
                        ) {
                            Image(
                                painter = painterResource(id = GetO(o)),
                                contentDescription = "Player's O",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(colors.onPrimary)
                ) {
                    when(option) {
                        1 -> ShowPlayersImages(
                            player = profile,
                            image = image,
                            onChangingImage = {image = it},
                        )
                        2 -> ShowPlayerX(
                            player = profile,
                            image = x,
                            onChangingImage = {x = it},
                        )
                        3 -> ShowPlayerO(
                            player = profile,
                            image = o,
                            onChangingImage = {o = it},
                        )
                    }
                }
            }
            //arrow button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((LocalConfiguration.current.screenHeightDp * 30 / 915).dp),
                shape = RoundedCornerShape(20),
                colors = CardDefaults.cardColors(colors.secondary)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    IconButton(
                        onClick = { expand = !expand },
                        modifier = Modifier.rotate(rotationState),
                        ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = Icons.Default.ArrowDropDown.name,
                            tint = colors.onSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowPlayersImages(
    player: MainPlayerUiState,
    image: String,
    onChangingImage: (String) -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    var changeImage by remember {
        mutableStateOf(false)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxWidth()
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
                                BorderStroke(
                                    if (photo == image) { 2.dp } else { 0.dp },
                                    if (photo == image) { colors.onPrimary } else { colors.primary }
                                ),
                                shape = RoundedCornerShape(0)
                            )
                            .clickable(onClick = {
                                onChangingImage(photo)
                            })

                    )
                }
            },
            modifier = Modifier
                .padding(5.dp)
                .background(colors.primary)
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
                .padding(5.dp)
                .background(colors.primary)
                .wrapContentHeight()

        )

        if (image != player.currentImage) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20),
                colors = CardDefaults.cardColors(Color.Green)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { changeImage = true }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = Icons.Default.Check.name,
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }

    if (changeImage) {
        ChangeImage(image = image, player = player)
        changeImage = false
    }
}
@Composable
fun ShowPlayerX(
    player: MainPlayerUiState,
    image: String,
    onChangingImage: (String) -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    var changeImage by remember {
        mutableStateOf(false)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxWidth()
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
                                BorderStroke(
                                    if (X == image) { 2.dp } else { 0.dp },
                                    if (X == image) { colors.onPrimary } else { colors.primary }
                                ),
                                shape = RoundedCornerShape(0)
                            )
                            .clickable(onClick = {
                                onChangingImage(X)
                            })

                    )
                }
            },
            modifier = Modifier
                .padding(5.dp)
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
                .padding(5.dp)
                .background(colors.primary)
                .wrapContentHeight()
        )

        if (image != player.currentX) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20),
                colors = CardDefaults.cardColors(Color.Green)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { changeImage = true }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = Icons.Default.Check.name,
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }

    if (changeImage) {
        //get database
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Players")

        val updatedPlayer = player.copy(
            currentX = image
        )


        databaseReference.child(player.key).setValue(updatedPlayer)

        changeImage = false
    }
}

@Composable
fun ShowPlayerO(
    player: MainPlayerUiState,
    image: String,
    onChangingImage: (String) -> Unit,
) {
    val  colors = MaterialTheme.colorScheme

    var changeImage by remember {
        mutableStateOf(false)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxWidth()
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
                                BorderStroke(
                                    if (O == image) { 2.dp } else { 0.dp },
                                    if (O == image) { colors.onPrimary } else { colors.primary }
                                ),
                                shape = RoundedCornerShape(0)
                            )
                            .clickable(onClick = {
                                onChangingImage(O)
                            })

                    )
                }
            },
            modifier = Modifier
                .padding(5.dp)
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
                .padding(5.dp)
                .background(colors.primary)
                .wrapContentHeight()
        )

        if (image != player.currentO) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20),
                colors = CardDefaults.cardColors(Color.Green)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { changeImage = true }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = Icons.Default.Check.name,
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }

    if (changeImage) {
        //get database
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Players")

        val updatedPlayer = player.copy(
            currentO = image
        )


        databaseReference.child(player.key).setValue(updatedPlayer)

        changeImage = false
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
    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Players")

    val updatedPlayer = player.copy(
        currentImage = image
    )

    databaseReference.child(player.key).setValue(updatedPlayer)
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