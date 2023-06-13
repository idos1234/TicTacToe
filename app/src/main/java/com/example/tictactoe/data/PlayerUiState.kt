package com.example.tictactoe.data

data class PlayerUiState(
    val id: Int = 0,
    var name: String = "",
    var password: String? = ""
)

fun PlayerUiState.toPlayer(): Player = Player(
    id = id,
    name = name,
)

fun PlayerUiState.isValid() : Boolean {
    return name.isNotBlank()
}