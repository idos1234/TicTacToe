package com.example.tictactoe.data

/**
  * [UiState] has the data of the game
 **/

data class UiState (
    var Box1: String = "",
    var Box2: String = "",
    var Box3: String = "",
    var Box4: String = "",
    var Box5: String = "",
    var Box6: String = "",
    var Box7: String = "",
    var Box8: String = "",
    var Box9: String = "",

    var player_Turn: String = "X",
    var player1: String = "",
    var player2: String = "",
    var winner: String = "",
    var ToCheck: Boolean = false,
    var times: Int = 0,
    var isenabled: Boolean = true
)