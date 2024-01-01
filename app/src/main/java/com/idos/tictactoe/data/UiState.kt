package com.idos.tictactoe.data


/**
  * [UiState] has the data of the game
 **/

data class UiState (
    //nine boxes
    var boxes: Boxes = Boxes(),
    //show whose turn X/O
    var player_Turn: String = "X",
    //who is the winner
    var winner: String = "",
    //show if need to check winner
    var ToCheck: Boolean = false,
    //times played in one round
    var times: Int = 0,
    //check if box is enabled(if it already clicked)
    var isenabled: Boolean = true,
    //target boxes for bot
    var targets: MutableList<Int> = mutableListOf(1,2,3,4,5,6,7,8,9)
)