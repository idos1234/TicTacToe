package com.example.tictactoe.data

data class MainPlayerUiState(
    var name: String = "",
    var email: String = "",
    var score: Int = 0,
    var password: String = "",
    var currentImage: String = "",
    var unlockedImages: List<String> = listOf(),
    var lockedImages: List<String> = listOf()
)

fun MainPlayerUiState.isValid() : Boolean {
    return name.isNotEmpty() && password.isNotEmpty()
}