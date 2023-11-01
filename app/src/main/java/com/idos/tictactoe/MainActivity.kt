package com.idos.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.idos.tictactoe.ui.Screen.TicTacToeApp
import com.idos.tictactoe.ui.theme.TicTacToeTheme

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