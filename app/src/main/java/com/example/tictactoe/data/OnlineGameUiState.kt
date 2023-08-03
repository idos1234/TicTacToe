package com.example.tictactoe.data

data class OnlineGameUiState (
    var player1: String = "",
    var player2: String = "",
    var winner: String = "",
    var boxes: Boxes = Boxes()
)

data class Boxes (
    var box1: String = "",
    var box2: String = "",
    var box3: String = "",
    var box4: String = "",
    var box5: String = "",
    var box6: String = "",
    var box7: String = "",
    var box8: String = "",
    var box9: String = ""
)