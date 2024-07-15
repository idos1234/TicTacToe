package com.idos.tictactoe.ui.screens.Shop

import android.content.Context
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.idos.tictactoe.R
import com.idos.tictactoe.data.Images
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.Os
import com.idos.tictactoe.data.Skin
import com.idos.tictactoe.data.Xs
import com.idos.tictactoe.ui.Screen.Menu.Shop.DealOptions
import com.idos.tictactoe.ui.Screen.Menu.Shop.ShopPreferences
import com.idos.tictactoe.ui.Screen.Menu.getPlayer
import java.util.Calendar
import java.util.Timer
import kotlin.concurrent.schedule

var firstDeal = mutableStateOf(Skin(0, "", 0, "") {})
var secondDeal = mutableStateOf(Skin(0, "", 0, "") {})
var thirdDeal = mutableStateOf(Skin(0, "", 0, "") {})
var fourthDeal = mutableStateOf(Skin(0, "", 0, "") {})
var fifthDeal = mutableStateOf(Skin(0, "", 0, "") {})
var sixthDeal = mutableStateOf(Skin(0, "", 0, "") {})
var text = mutableStateOf("")

var collectedDeals = mutableListOf(0, 0, 0, 0, 0, 0)

private var playerEmail: String = ""


fun getDeal(key: String): Skin {
    return try {
        Images.find {
            it.tag == key
        }!!
    } catch (e: Exception) {
        try {
            Xs.find {
                it.tag == key
            }!!
        } catch (e: Exception) {
            try {
                Os.find {
                    it.tag == key
                }!!
            } catch (e: Exception) {
                Skin(0, "", 0, "") {}
            }
        }
    }
}

fun GetDealsFromSharedPreferences(context: Context) {
    //shared preferences
    val preferencesManager = ShopPreferences(context)

    firstDeal.value = getDeal(key = preferencesManager.getData("1", ""))
    secondDeal.value = getDeal(key = preferencesManager.getData("2", ""))
    thirdDeal.value = getDeal(key = preferencesManager.getData("3", ""))
    fourthDeal.value = getDeal(key = preferencesManager.getData("4", ""))
    fifthDeal.value = getDeal(key = preferencesManager.getData("5", ""))
    sixthDeal.value = getDeal(key = preferencesManager.getData("6", ""))

    collectedDeals[0] = preferencesManager.getData("list-1", "0").toInt()
    collectedDeals[1] = preferencesManager.getData("list-2", "0").toInt()
    collectedDeals[2] = preferencesManager.getData("list-3", "0").toInt()
    collectedDeals[3] = preferencesManager.getData("list-4", "0").toInt()
    collectedDeals[4] = preferencesManager.getData("list-5", "0").toInt()
    collectedDeals[5] = preferencesManager.getData("list-6", "0").toInt()


}

fun SetNewDeals(context: Context, email: String = playerEmail) {
    val preferencesManager = ShopPreferences(context)
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    //get Players collection from database
    db.collection("Players").get()
        //on success
        .addOnSuccessListener { queryDocumentSnapshots ->
            //check if collection is empty
            if (!queryDocumentSnapshots.isEmpty) {
                val list = queryDocumentSnapshots.documents
                try {
                    val player = list.find {
                        it.toObject(MainPlayerUiState::class.java)!!.email == email
                    }?.toObject(MainPlayerUiState::class.java)!!

                    preferencesManager.saveData("1", DealOptions(0..1, player = player).random())
                    preferencesManager.saveData("2", DealOptions(2..3, player = player).random())
                    preferencesManager.saveData("3", DealOptions(4..5, player = player).random())
                    preferencesManager.saveData("4", DealOptions(6..7, player = player).random())
                    preferencesManager.saveData("5", DealOptions(9..11, player = player).random())
                    preferencesManager.saveData("6", DealOptions(12..13, player = player).random())
                } catch (_: Exception) {
                }
            }
        }
    SetCollectedDeals(context)
}

fun SetCollectedDeals(context: Context) {
    val preferencesManager = ShopPreferences(context)

    preferencesManager.saveData("list-1", collectedDeals[0].toString())
    preferencesManager.saveData("list-2", collectedDeals[1].toString())
    preferencesManager.saveData("list-3", collectedDeals[2].toString())
    preferencesManager.saveData("list-4", collectedDeals[3].toString())
    preferencesManager.saveData("list-5", collectedDeals[4].toString())
    preferencesManager.saveData("list-6", collectedDeals[5].toString())
}


@Composable
fun ShopScreen(playerName: String, onBuy: @Composable () -> Unit, context: Context = LocalContext.current) {
    playerEmail = playerName

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    val player = getPlayer(email = playerName, context = context)

    //today in 11 AM
    val timeToMatch = Calendar.getInstance()
    timeToMatch[Calendar.HOUR_OF_DAY] = 11
    timeToMatch[Calendar.MINUTE] = 0
    //current Time
    val currentTime = Calendar.getInstance()

    GetDealsFromSharedPreferences(context)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height((screenHeight/10).dp))
        }
        item {
            Text(
                text = "Daily Deals",
                fontSize = (screenHeight*0.05).sp,
                fontWeight = FontWeight.Light,
                color = colors.onSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .background(colors.secondary, CircleShape)
                    .fillMaxWidth()
            )
        }
        item {
            if(currentTime >= timeToMatch) {
                text.value = "tomorrow"
            } else {
                text.value = "today"
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = AbsoluteAlignment.CenterLeft
            ) {
                Text(
                    text = "Next deals: ${text.value} in 11",
                    fontSize = (screenHeight*0.015).sp,
                    fontWeight = FontWeight.Light,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }
        }

        item {
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .height((screenWidth * 0.8).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    if(firstDeal.value == Skin(0, "", 0, "", null)) {
                        ShowCoinsDeal(
                            coins = 5,
                            player = player,
                            colors = colors,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            dealNumber = 1,
                            onBuy = onBuy
                        )
                    } else {
                        ShowDeal(
                            skin = firstDeal.value,
                            player = player,
                            colors = colors,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            dealNumber = 1,
                            onBuy = onBuy
                        )
                    }
                    if(player.level >= 2) {
                        if(secondDeal.value == Skin(0, "", 0, "", null)) {
                            ShowCoinsDeal(
                                coins = 5,
                                player = player,
                                colors = colors,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                dealNumber = 2,
                                onBuy = onBuy
                            )
                        } else {
                            ShowDeal(
                                skin = secondDeal.value,
                                player = player,
                                colors = colors,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                dealNumber = 2,
                                onBuy = onBuy
                            )
                        }
                    } else {
                        ShowUnderLevel(
                            colors = colors,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            level = 2
                        )
                    }
                    if(player.level >= 3) {
                        if(thirdDeal.value == Skin(0, "", 0, "", null)) {
                            ShowCoinsDeal(
                                coins = 5,
                                player = player,
                                colors = colors,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                dealNumber = 3,
                                onBuy = onBuy
                            )
                        } else {
                            ShowDeal(
                                skin = thirdDeal.value,
                                player = player,
                                colors = colors,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                dealNumber = 3,
                                onBuy = onBuy
                            )
                        }
                    } else {
                        ShowUnderLevel(
                            colors = colors,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            level = 3
                        )
                    }
                }
                Row(
                    Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    if(player.level >= 4) {
                        if(fourthDeal.value == Skin(0, "", 0, "", null)) {
                            ShowCoinsDeal(
                                coins = 5,
                                player = player,
                                colors = colors,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                dealNumber = 4,
                                onBuy = onBuy
                            )
                        } else {
                            ShowDeal(
                                skin = fourthDeal.value,
                                player = player,
                                colors = colors,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                dealNumber = 4,
                                onBuy = onBuy
                            )
                        }
                    } else {
                        ShowUnderLevel(
                            colors = colors,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            level = 4
                        )
                    }
                    if(player.level >= 5) {
                        if(fifthDeal.value == Skin(0, "", 0, "", null)) {
                            ShowCoinsDeal(
                                coins = 5,
                                player = player,
                                colors = colors,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                dealNumber = 5,
                                onBuy = onBuy
                            )
                        } else {
                            ShowDeal(
                                skin = fifthDeal.value,
                                player = player,
                                colors = colors,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                dealNumber = 5,
                                onBuy = onBuy
                            )
                        }
                    } else {
                        ShowUnderLevel(
                            colors = colors,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            level = 5
                        )
                    }
                    if(player.level >= 6) {
                        if(sixthDeal.value == Skin(0, "", 0, "", null)) {
                            ShowCoinsDeal(
                                coins = 5,
                                player = player,
                                colors = colors,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                dealNumber = 6,
                                onBuy = onBuy
                            )
                        } else {
                            ShowDeal(
                                skin = sixthDeal.value,
                                player = player,
                                colors = colors,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                dealNumber = 6,
                                onBuy = onBuy
                            )
                        }
                    } else {
                        ShowUnderLevel(
                            colors = colors,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            level = 6
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Coins",
                fontSize = (screenHeight*0.05).sp,
                fontWeight = FontWeight.Light,
                color = colors.onSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .background(colors.secondary, CircleShape)
                    .fillMaxWidth()
            )
        }

        item {
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .height((screenWidth * 0.8).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    ShowCoins(
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        coins = 100,
                        price = 0.99
                    )
                    ShowCoins(
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        coins = 500,
                        price = 4.99
                    )
                    ShowCoins(
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        coins = 1000,
                        price = 9.99
                    )
                }
                Row(
                    Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    ShowCoins(
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        coins = 2000,
                        price = 19.99
                    )
                    ShowCoins(
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        coins = 5000,
                        price = 49.99
                    )
                    ShowCoins(
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        coins = 10000,
                        price = 99.99
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        }
    }
}

@Composable
fun ShowCoins(
    colors: ColorScheme,
    screenWidth: Int,
    screenHeight: Int,
    coins: Int,
    price: Double
) {
    Card(
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(colors.primary),
        modifier = Modifier
            .padding(5.dp)
            .width((screenWidth / 3 - 15).dp)
            .fillMaxHeight()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .size((screenWidth / 4).dp)
                    .padding(horizontal = 5.dp),
                colors = CardDefaults.cardColors(colors.onPrimary),
                shape = RoundedCornerShape(30)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = coins.toString(),
                        color = colors.primary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = screenHeight.sp*0.02,
                    )

                    Image(
                        painter = painterResource(id = R.drawable.coin),
                        modifier = Modifier.size((screenHeight*15/915).dp),
                        contentDescription = null
                    )
                }
            }

            Text(
                text = "$$price",
                color = colors.onPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = screenHeight.sp * 0.02,
            )

        }
    }
}

@Composable
fun ShowUnderLevel(colors: ColorScheme, screenWidth: Int, screenHeight: Int, level: Int) {
    Card(
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(colors.primary),
        modifier = Modifier
            .padding(5.dp)
            .width((screenWidth / 3 - 15).dp)
            .fillMaxHeight()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = "lock",
                tint = colors.onPrimary,
                modifier = Modifier
                    .size(((screenWidth / 3 - 30) / 5 * 3).dp)
            )
            Text(
                text = "Unlocks at level $level",
                color = colors.onPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = screenHeight.sp * 0.015,
            )
        }
    }
}


@Composable
fun ShowDeal(
    skin: Skin,
    player: MainPlayerUiState,
    colors: ColorScheme,
    screenWidth: Int,
    screenHeight: Int,
    dealNumber: Int,
    onBuy: @Composable () -> Unit

) {
    var rotation by remember {
        mutableFloatStateOf(0f)
    }
    var error by remember {
        mutableStateOf(false)
    }
    var showError by remember {
        mutableStateOf(false)
    }
    var buyDeal by remember {
        mutableStateOf(false)
    }

    val color = if(collectedDeals[dealNumber-1] == 0) {
        colors.primary
    } else {
        Color.Green.copy(alpha = 0.6f)
    }
    Card(
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(color),
        modifier = Modifier
            .padding(5.dp)
            .width((screenWidth / 3 - 15).dp)
            .fillMaxHeight()
            .rotate(rotation)
            .clickable(
                enabled = (collectedDeals[dealNumber - 1] == 0),
                onClick = {
                    if (player.coins < skin.price) {
                        showError = true
                    } else {
                        buyDeal = true
                    }
                }
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = skin.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(20))
                    .size(((screenWidth / 3 - 30) / 5 * 3).dp)
            )
            Text(
                text = skin.name,
                color = colors.onPrimary,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold,
                fontSize = screenHeight.sp*0.01,
            )
            val color = if(player.coins >= skin.price) {
                colors.onPrimary
            } else {
                colors.error
            }
            Text(
                text = skin.price.toString(),
                color = color,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold,
                fontSize = screenHeight.sp*0.025,
            )
            Image(
                painter = painterResource(id = R.drawable.coin),
                modifier = Modifier.size((screenHeight*15/915).dp),
                contentDescription = null
            )
        }
    }

    if(showError) {
        LaunchedEffect(error, rotation) {
            error = true
            Timer().schedule(200) {
                error = false
                rotation = 0f
            }
        }
        showError = false
    }

    if(error) {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        rotation = infiniteTransition.animateFloat(
            initialValue = 5f,
            targetValue = -5f,
            animationSpec = infiniteRepeatable(
                animation = tween(100, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        ).value
    }

    if(buyDeal) {
        DealDialog(
            onClose = { buyDeal = false },
            skin = skin,
            colors = colors,
            onBuy = {
                collectedDeals[dealNumber-1] = 1
                SetCollectedDeals(LocalContext.current)
                skin.onClick!!(player.email)
                onBuy()
            }
        )
    }
}

@Composable
fun ShowCoinsDeal(
    coins: Int,
    player: MainPlayerUiState,
    colors: ColorScheme,
    screenWidth: Int,
    screenHeight: Int,
    dealNumber: Int,
    onBuy: @Composable () -> Unit
) {
    var getCoins by remember {
        mutableStateOf(false)
    }

    val color = if(collectedDeals[dealNumber-1] == 0) {
        colors.primary
    } else {
        Color.Green.copy(alpha = 0.6f)
    }
    Card(
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(color),
        modifier = Modifier
            .padding(5.dp)
            .width((screenWidth / 3 - 15).dp)
            .fillMaxHeight()
            .clickable(
                enabled = (collectedDeals[dealNumber - 1] == 0),
                onClick = {
                    getCoins = true
                }
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = coins.toString(),
                color = colors.onPrimary,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold,
                fontSize = screenHeight.sp*0.03,
            )
            Image(
                painter = painterResource(id = R.drawable.coin),
                modifier = Modifier.size((screenHeight*40/915).dp),
                contentDescription = null
            )
        }
    }

    if(getCoins) {
        collectedDeals[dealNumber-1] = 1
        SetCollectedDeals(LocalContext.current)
        getCoins = false

        val db = FirebaseFirestore.getInstance().collection("Players")

        val updatedPlayer = player.copy(
            coins = player.coins + coins
        )

        db.whereEqualTo("email", player.email)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    db.document(document.id).set(
                        updatedPlayer,
                        SetOptions.merge()
                    )
                }
            }

        onBuy()
    }
}

@Composable
fun DealDialog(
    onClose: () -> Unit,
    skin: Skin,
    colors: ColorScheme,
    onBuy: @Composable () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    var buy by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(10),
            colors = CardDefaults.cardColors(colors.background),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(0.5f)
                .wrapContentHeight()
        ) {
            Column(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = skin.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20))
                        .size(((screenWidth / 3 - 30) / 5 * 3).dp)
                )
                Text(
                    text = skin.name,
                    color = colors.onBackground,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                    fontSize = screenHeight.sp*0.01,
                )

                Text(
                    text = skin.price.toString(),
                    color = colors.onBackground,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                    fontSize = screenHeight.sp*0.025,
                )
                Image(
                    painter = painterResource(id = R.drawable.coin),
                    modifier = Modifier.size((screenHeight*15/915).dp),
                    contentDescription = null
                )

                Button(
                    onClick = { buy = true },
                    colors = ButtonDefaults.buttonColors(colors.primary),
                    modifier = Modifier
                        .padding(top = (screenHeight * 15 / 915).dp)
                        .fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "Buy",
                        color = colors.onPrimary,
                        textAlign = TextAlign.Right,
                        fontWeight = FontWeight.Bold,
                        fontSize = screenHeight.sp*0.025,
                    )
                }
            }
        }
    }

    if(buy) {
        buy = false
        onBuy()
        onClose()
    }
}