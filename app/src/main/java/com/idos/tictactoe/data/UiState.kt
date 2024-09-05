package com.idos.tictactoe.data


/**
  * [UiState] has the data of the game
 **/

data class UiState (
    //nine boxes
    var boxes: Boxes = Boxes(),
    //show whose turn X/O
    var playerTurn: String = "X",
    //who is the winner
    var winner: String = "",
    //show if need to check winner
    var toCheck: Boolean = false,
    //times played in one round
    var times: Int = 0,
    //check if box is enabled(if it already clicked)
    var isEnabled: Boolean = true,
    //player1 score
    var player1Score: Int = 0,
    //player2 score
    var player2Score: Int = 0,
    //update score
    var isScoreUpdated: Boolean = false,
    //target boxes for bot
    var targets: MutableList<Int> = mutableListOf(1,2,3,4,5,6,7,8,9)
)