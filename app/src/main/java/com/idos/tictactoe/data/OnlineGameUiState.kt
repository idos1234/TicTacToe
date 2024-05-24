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