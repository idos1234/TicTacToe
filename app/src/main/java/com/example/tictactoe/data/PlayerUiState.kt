package com.example.tictactoe.data

data class MainPlayerUiState(
    var name: String = "",
    var score: Int = 0,
    var password: String = "",
    var secondPlayers: List<SecondPlayerUiState> = listOf()
)

data class SecondPlayerUiState(
    var name: String = "",
    var score: Int = 0
)


fun MainPlayerUiState.toPlayer(): Player = Player(
    id = 1,
    name = name,
    score = score,
    password = password
)

fun MainPlayerUiState.isValid() : Boolean {
    return name.isNotBlank()
}