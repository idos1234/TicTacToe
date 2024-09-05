package com.idos.tictactoe.data

data class OnlineGameUiState(
    //game id
    var id: String = "",
    //player1
    var player1: String = "",
    //player2
    var player2: String = "",
    //winner
    var winner: String = "",
    //nine boxes
    var boxes: Boxes = Boxes(),
    //player turn
    var playerTurn: String = "X",
    //number of boxes filled
    var times: Int = 0,
    //player1 score
    var player1Score: Int = 0,
    //player2 score
    var player2Score: Int = 0,
    //if has winner for round
    var foundWinner: Boolean = false,
    //number of rounds
    var rounds: Int = 1,
    //if round cleared
    var editedRounds: Boolean = false,
    //if player 1 quit
    var player1Quit: Boolean = false,
    //if player 2 quit
    var player2Quit: Boolean = false,
    //for friendly games
    var wasGameStarted: Boolean = false,
    //player 1 progress
    var player1TimeLeft: Int = 10,
    //player 2 progress
    var player2TimeLeft: Int = 10
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