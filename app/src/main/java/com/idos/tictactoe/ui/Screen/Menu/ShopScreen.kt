package com.idos.tictactoe.ui.screens.Shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idos.tictactoe.R
import com.idos.tictactoe.data.images

@Preview(showSystemUi = true, device = "spec:width=1080px,height=2340px,dpi=640")
@Composable
fun ShopScreen() {
    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

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
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .height((screenWidth*0.8).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    ShowDeal(
                        image = images[2].image,
                        name = images[2].name,
                        price = 2000,
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight
                    )
                    ShowDeal(
                        image = images[14].x,
                        name = images[14].name,
                        price = 5000,
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight
                    )
                    ShowUnderLevel(
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        level = 4
                    )
                }
                Row(
                    Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    ShowUnderLevel(
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        level = 5
                    )
                    ShowUnderLevel(
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        level = 7
                    )
                    ShowUnderLevel(
                        colors = colors,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        level = 10
                    )
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
                .height((screenWidth*0.8).dp),
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
            .fillMaxHeight(0.8f)
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
                        contentDescription = null,
                        Modifier

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
            .fillMaxSize()
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
fun ShowDeal(image: Int, name: String, price: Int, colors: ColorScheme, screenWidth: Int, screenHeight: Int) {
    Card(
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(colors.primary),
        modifier = Modifier
            .padding(5.dp)
            .width((screenWidth / 3 - 15).dp)
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(20))
                    .size(((screenWidth / 3 - 30) / 5 * 3).dp)
            )
            Text(
                text = name,
                color = colors.onPrimary,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold,
                fontSize = screenHeight.sp*0.01,
            )
            Text(
                text = price.toString(),
                color = colors.onPrimary,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold,
                fontSize = screenHeight.sp*0.025,
            )
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = null
            )
        }
    }
}