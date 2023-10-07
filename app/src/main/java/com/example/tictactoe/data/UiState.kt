package com.example.tictactoe.data


/**
  * [UiState] has the data of the game
 **/

data class UiState (
    //box1 in the game board
    var Box1: String = "",
    //box2 in the game board
    var Box2: String = "",
    //box3 in the game board
    var Box3: String = "",
    //box4 in the game board
    var Box4: String = "",
    //box5 in the game board
    var Box5: String = "",
    //box6 in the game board
    var Box6: String = "",
    //box7 in the game board
    var Box7: String = "",
    //box8 in the game board
    var Box8: String = "",
    //box9 in the game board
    var Box9: String = "",
    //show whose turn X/O
    var player_Turn: String = "X",
    //who is the winner
    var winner: String = "",
    //show if need to check winner
    var ToCheck: Boolean = false,
    //times played in one round
    var times: Int = 0,
    //check if box is enabled(if it already clicked)
    var isenabled: Boolean = true
)