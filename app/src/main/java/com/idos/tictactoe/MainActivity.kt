package com.idos.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.idos.tictactoe.ui.Screen.TicTacToeApp
import com.idos.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                TicTacToeApp()
                Firebase.messaging.subscribeToTopic("Ido")
            }
        }
    }
}

@Composable
fun SplashScreen(onFinished: () -> Unit, isCheckedNextScreen: Boolean) {
    LaunchedEffect(key1 = true) {
        delay(1500L)
        while (!isCheckedNextScreen) {null}
        onFinished()
    }
}