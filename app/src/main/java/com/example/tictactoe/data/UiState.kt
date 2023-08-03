package com.example.tictactoe.data


/**
  * [UiState] has the data of the game
 **/

data class UiState (
    //box1 in the game board
    var Box1: String = "",
    //box1 in the game board
    var Box2: String = "",
    //box1 in the game board
    var Box3: String = "",
    //box1 in the game board
    var Box4: String = "",
    //box1 in the game board
    var Box5: String = "",
    //box1 in the game board
    var Box6: String = "",
    //box1 in the game board
    var Box7: String = "",
    //box1 in the game board
    var Box8: String = "",
    //box1 in the game board
    var Box9: String = "",
    //box1 in the game board

    //show whose turn X/O
    var player_Turn: String = "X",
    //player1
    var player1: Player = Player(),
    //player2
    var player2: Player = Player(),
    //who is the winner
    var winner: String = "",
    //show if need to check winner
    var ToCheck: Boolean = false,
    //times played in one round
    var times: Int = 0,
    //check if box is enabled(if it already clicked)
    var isenabled: Boolean = true
)