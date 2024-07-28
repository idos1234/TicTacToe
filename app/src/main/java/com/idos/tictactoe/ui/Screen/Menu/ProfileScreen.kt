package com.idos.tictactoe.ui.Screen.Menu

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.data.GetO
import com.idos.tictactoe.data.GetX
import com.idos.tictactoe.data.GetXO
import com.idos.tictactoe.data.MainPlayerUiState

@Composable
fun PlayerCard(
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
fun ShowSkinsList(
    modifier: Modifier,
    profile: MainPlayerUiState
) {
    var playerImage by remember {
        mutableStateOf(profile.currentImage)
    }
    var playerX by remember {
        mutableStateOf(profile.currentX)
    }
    var playerO by remember {
        mutableStateOf(profile.currentO)
    }
    var option by remember {
        mutableIntStateOf(0)
    }
    var text by remember {
        mutableStateOf("images")
    }

    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4),
        colors = CardDefaults.cardColors(colors.background)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //change skins button
            Box(
                modifier = Modifier.fillMaxWidth(0.9f),
                contentAlignment = AbsoluteAlignment.CenterLeft
            ) {
                Card(
                    onClick = {
                        ++option
                        option %= 3
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.6f),
                    colors = CardDefaults.cardColors(colors.secondary)
                ) {
                    Text(
                        text = "Show all $text",
                        fontSize = (LocalConfiguration.current.screenHeightDp*0.015).sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))

            when(option) {
                0 -> {
                    ShowPlayerImages(player = profile, image = playerImage) {
                        playerImage = it
                    }
                    text = "images"
                }
                1 -> {
                    ShowPlayerX(player = profile, image = playerX) {
                        playerX = it
                    }
                    text = "x"
                }
                2 -> {
                    ShowPlayerO(player = profile, image = playerO) {
                        playerO = it
                    }
                    text = "o"
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

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .background(brush)
    ) {
        //player's card(name, score, wins, loses, skins)
        item {
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
        }


        //progress to the next rank
        item {
            LevelBar(
                modifier = Modifier.fillMaxWidth(0.9f),
                profile = profile,
                scoreToNextLevel = scoreToNextLevel,
                scoreFromCurrentLevel = scoreFromCurrentLevel
            )
        }

        item {
            ShowSkinsList(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = (LocalConfiguration.current.screenHeightDp * 20 / 915).dp)
                    .wrapContentHeight(),
                profile = profile
            )
        }
    }
}

@Composable
fun ShowPlayerImages(
    player: MainPlayerUiState,
    image: String,
    onChangingImage: (String) -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    var changeImage by remember {
        mutableStateOf(false)
    }
    val size = (LocalConfiguration.current.screenWidthDp*0.9-5*2)/4

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxWidth()
        .background(Color.Transparent)
        .wrapContentHeight()) {
        Card(
            modifier = Modifier
                .padding(5.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(4),
            colors = CardDefaults.cardColors(colors.primary)
        ) {
            FlowRow(
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = FlowMainAxisAlignment.Start,
                content = {
                    for (photo in player.unlockedImages) {
                        AsyncImage(
                            model = GetXO(photo),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .size(size.dp)
                                .border(
                                    BorderStroke(
                                        if (photo == image) {
                                            2.dp
                                        } else {
                                            0.dp
                                        },
                                        if (photo == image) {
                                            colors.onPrimary
                                        } else {
                                            colors.primary
                                        }
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
                    .wrapContentHeight()
            )
        }

        Card(
            modifier = Modifier
                .padding(5.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(4),
            colors = CardDefaults.cardColors(colors.primary)
        ) {
            FlowRow(
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = FlowMainAxisAlignment.Start,
                content = {
                    for (photo in player.lockedImages) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            AsyncImage(
                                model = GetXO(photo),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(size.dp),
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                    setToSaturation(
                                        0.25f
                                    )
                                })
                            )
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "locked image"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .wrapContentHeight()

            )
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

    val size = (LocalConfiguration.current.screenWidthDp*0.9-5*2)/4

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxWidth()
        .background(Color.Transparent)
        .wrapContentHeight()) {
        Card(
            modifier = Modifier
                .padding(5.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(4),
            colors = CardDefaults.cardColors(colors.primary)
        ) {
            FlowRow(
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = FlowMainAxisAlignment.Start,
                content = {
                    for (X in player.unlockedX) {
                        AsyncImage(
                            model = GetX(X),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .size(size.dp)
                                .border(
                                    BorderStroke(
                                        if (X == image) {
                                            2.dp
                                        } else {
                                            0.dp
                                        },
                                        color = if (X == image) {
                                            colors.onPrimary
                                        } else {
                                            colors.primary
                                        }
                                    ),
                                    shape = RoundedCornerShape(4)
                                )
                                .clickable(onClick = {
                                    onChangingImage(X)
                                })

                        )
                    }
                },
                modifier = Modifier
                    .wrapContentHeight()
            )
        }
        Card(
            modifier = Modifier
                .padding(5.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(4),
            colors = CardDefaults.cardColors(colors.primary)
        ) {
            FlowRow(
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = FlowMainAxisAlignment.Start,
                content = {
                    for (X in player.lockedX) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            AsyncImage(
                                model = GetX(X),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(size.dp),
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                    setToSaturation(
                                        0.25f
                                    )
                                })
                            )
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "locked X",
                                tint = Color(0xFF2B2B2B)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .wrapContentHeight()
            )
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

    val size = (LocalConfiguration.current.screenWidthDp*0.9-5*2)/4

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxWidth()
        .background(Color.Transparent)
        .wrapContentHeight()) {
        Card(
            modifier = Modifier
                .padding(5.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(4),
            colors = CardDefaults.cardColors(colors.primary)
        ) {
            FlowRow(
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = FlowMainAxisAlignment.Start,
                content = {
                    for (O in player.unlockedO) {
                        AsyncImage(
                            model = GetO(O),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .size(size.dp)
                                .border(
                                    BorderStroke(
                                        if (O == image) {
                                            2.dp
                                        } else {
                                            0.dp
                                        },
                                        if (O == image) {
                                            colors.onPrimary
                                        } else {
                                            colors.primary
                                        }
                                    ),
                                    shape = RoundedCornerShape(0)
                                )
                                .clickable(onClick = {
                                    onChangingImage(O)
                                    changeImage = true
                                })

                        )
                    }
                },
                modifier = Modifier
                    .wrapContentHeight()
            )
        }

        Card(
            modifier = Modifier
                .padding(5.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(4),
            colors = CardDefaults.cardColors(colors.primary)
        ) {
            FlowRow(
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = FlowMainAxisAlignment.Start,
                content = {
                    for (O in player.lockedO) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            AsyncImage(
                                model = GetO(O),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(size.dp),
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                    setToSaturation(
                                        0.25f
                                    )
                                })
                            )
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "locked O")
                        }
                    }
                },
                modifier = Modifier
                    .wrapContentHeight()
            )
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