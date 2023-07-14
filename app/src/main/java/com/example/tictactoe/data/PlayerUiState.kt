package com.example.tictactoe.data

data class MainPlayerUiState(
    var name: String = "",
    var score: Int = 0,
    var email: String = "" ,
    var password: String = "",
    var secondPlayers: List<SecondPlayerUiState> = listOf()
)

data class SecondPlayerUiState(
    var name: String = "",
)


fun MainPlayerUiState.toPlayer(): Player = Player(
    id = 1,
    name = name,
    score = score,
    password = password
)

fun MainPlayerUiState.isValid() : Boolean {
    return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
}