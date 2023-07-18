package com.example.tictactoe.data

data class MainPlayerUiState(
    var name: String = "",
    var score: Int = 0,
    var password: String = "",
    val image: String = ""
)


fun MainPlayerUiState.toPlayer(): Player = Player(
    id = 1,
    name = name,
    score = score,
    password = password
)

fun MainPlayerUiState.isValid() : Boolean {
    return name.isNotEmpty() && password.isNotEmpty()
}