package com.example.tictactoe.data

data class OnlineGameUiState (
    var id: Int = 0,
    var player1: String = "",
    var player2: String = "",
    var winner: String = "",
    var boxes: Boxes = Boxes(),
    var playerTurn: String = "X",
    var times: Int = 0,
    var player1Score: Int = 0,
    var player2Score: Int = 0,
    var foundWinner: Boolean = false,
    var rounds: Int = 1
)

data class Boxes (
    var Box1: String = "",
    var Box2: String = "",
    var Box3: String = "",
    var Box4: String = "",
    var Box5: String = "",
    var Box6: String = "",
    var Box7: String = "",
    var Box8: String = "",
    var Box9: String = ""
)