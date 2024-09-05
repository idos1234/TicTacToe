package com.idos.tictactoe.ui

import com.idos.tictactoe.data.UiState

/**
 * Help the bot to choose which box it should choose
 */

fun willWin(uiState: UiState, player: String): Int {

    var Box = 0

    // box1

    if (uiState.boxes.box2 == player) {
        if (uiState.boxes.box3 == player) {
            if (uiState.boxes.box1 == "") {
                Box = 1
            }
        }
    }
    if (uiState.boxes.box2 == player) {
        if (uiState.boxes.box3 == player) {
            if (uiState.boxes.box1 == "") {
                Box = 1
            }
        }
    }
    if (uiState.boxes.box5 == player) {
        if (uiState.boxes.box9 == player) {
            if (uiState.boxes.box1 == "") {
                Box = 1
            }
        }
    }

    // box2

    if (uiState.boxes.box1 == player) {
        if (uiState.boxes.box3 == player) {
            if (uiState.boxes.box2 == "") {
                Box = 2
            }
        }
    }
    if (uiState.boxes.box5 == player) {
        if (uiState.boxes.box8 == "X") {
            if (uiState.boxes.box2 == "") {
                Box = 2
            }
        }
    }

    // box3

    if (uiState.boxes.box2 == player) {
        if (uiState.boxes.box1 == player) {
            if (uiState.boxes.box3 == "") {
                Box = 3
            }
        }
    }
    if (uiState.boxes.box6 == player) {
        if (uiState.boxes.box9 == player) {
            if (uiState.boxes.box3 == "") {
                Box = 3
            }
        }
    }
    if (uiState.boxes.box5 == player) {
        if (uiState.boxes.box7 == player) {
            if (uiState.boxes.box3 == "") {
                Box = 3
            }
        }
    }

    // box4

    if (uiState.boxes.box5 == player) {
        if (uiState.boxes.box6 == player) {
            if (uiState.boxes.box4 == "") {
                Box = 4
            }
        }
    }
    if (uiState.boxes.box1 == player) {
        if (uiState.boxes.box7 == player) {
            if (uiState.boxes.box4 == "") {
                Box = 4
            }
        }
    }

    // box5

    if (uiState.boxes.box1 == player) {
        if (uiState.boxes.box9 == player) {
            if (uiState.boxes.box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.boxes.box3 == player) {
        if (uiState.boxes.box7 == player) {
            if (uiState.boxes.box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.boxes.box2 == player) {
        if (uiState.boxes.box8 == player) {
            if (uiState.boxes.box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.boxes.box4 == player) {
        if (uiState.boxes.box6 == player) {
            if (uiState.boxes.box5 == "") {
                Box = 5
            }
        }
    }

    // box6

    if (uiState.boxes.box3 == player) {
        if (uiState.boxes.box9 == player) {
            if (uiState.boxes.box6 == "") {
                Box = 6
            }
        }
    }
    if (uiState.boxes.box4 == player) {
        if (uiState.boxes.box5 == player) {
            if (uiState.boxes.box6 == "") {
                Box = 6
            }
        }
    }

    // box7

    if (uiState.boxes.box3 == player) {
        if (uiState.boxes.box5 == player) {
            if (uiState.boxes.box7 == "") {
                Box = 7
            }
        }
    }
    if (uiState.boxes.box1 == player) {
        if (uiState.boxes.box4 == player) {
            if (uiState.boxes.box7 == "") {
                Box = 7
            }
        }
    }
    if (uiState.boxes.box9 == player) {
        if (uiState.boxes.box8 == player) {
            if (uiState.boxes.box7 == "") {
                Box = 7
            }
        }
    }

    // box8

    if (uiState.boxes.box2 == player) {
        if (uiState.boxes.box5 == player) {
            if (uiState.boxes.box8 == "") {
                Box = 8
            }
        }
    }
    if (uiState.boxes.box7 == player) {
        if (uiState.boxes.box9 == player) {
            if (uiState.boxes.box8 == "") {
                Box = 8
            }
        }
    }

    // box9

    if (uiState.boxes.box3 == player) {
        if (uiState.boxes.box6 == player) {
            if (uiState.boxes.box9 == "") {
                Box = 9
            }
        }
    }
    if (uiState.boxes.box7 == player) {
        if (uiState.boxes.box8 == player) {
            if (uiState.boxes.box9 == "") {
                Box = 9
            }
        }
    }
    if (uiState.boxes.box1 == player) {
        if (uiState.boxes.box5 == player) {
            if (uiState.boxes.box9 == "") {
                Box = 9
            }
        }
    }

    return Box

}