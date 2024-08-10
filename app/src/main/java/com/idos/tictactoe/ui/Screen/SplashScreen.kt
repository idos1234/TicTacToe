package com.idos.tictactoe.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.network.Connection.ConnectionState
import com.example.network.Connection.connectivityState
import com.idos.tictactoe.R
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun SplashScreen(navController: NavHostController, startDestination: String) {
    var isPlaying by remember {
        mutableStateOf(true)
    }
    var navigate by remember {
        mutableStateOf(false)
    }

    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.splash_screen
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        isPlaying = isPlaying
    )

    LaunchedEffect(key1 = preloaderProgress) {
        isPlaying = preloaderProgress != 1f
    }

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
    )
    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = Modifier.fillMaxSize()
    )

    if(!isPlaying && !navigate) {
        Timer().schedule(500) {
            navigate = true
        }
    }

    if(navigate) {
        val connection by connectivityState()
        if(connection == ConnectionState.UnAvailable && startDestination == GameScreen.Home.title) {
            navController.navigate(GameScreen.NoInternet.title)
        } else {
            navController.navigate(startDestination)
        }
    }
}