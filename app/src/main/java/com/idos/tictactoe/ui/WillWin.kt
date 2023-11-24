package com.idos.tictactoe.ui

import com.idos.tictactoe.data.UiState

/**
 * Help the bot to choose which box it should choose
 */

fun WillWin (uiState: UiState, player: String): Int {

    var Box = 0

    // box1

    if (uiState.boxes.Box2 == player) {
        if (uiState.boxes.Box3 == player) {
            if (uiState.boxes.Box1 == "") {
                Box = 1
            }
        }
    }
    if (uiState.boxes.Box2 == player) {
        if (uiState.boxes.Box3 == player) {
            if (uiState.boxes.Box1 == "") {
                Box = 1
            }
        }
    }
    if (uiState.boxes.Box5 == player) {
        if (uiState.boxes.Box9 == player) {
            if (uiState.boxes.Box1 == "") {
                Box = 1
            }
        }
    }

    // box2

    if (uiState.boxes.Box1 == player) {
        if (uiState.boxes.Box3 == player) {
            if (uiState.boxes.Box2 == "") {
                Box = 2
            }
        }
    }
    if (uiState.boxes.Box5 == player) {
        if (uiState.boxes.Box8 == "X") {
            if (uiState.boxes.Box2 == "") {
                Box = 2
            }
        }
    }

    // box3

    if (uiState.boxes.Box2 == player) {
        if (uiState.boxes.Box1 == player) {
            if (uiState.boxes.Box3 == "") {
                Box = 3
            }
        }
    }
    if (uiState.boxes.Box6 == player) {
        if (uiState.boxes.Box9 == player) {
            if (uiState.boxes.Box3 == "") {
                Box = 3
            }
        }
    }
    if (uiState.boxes.Box5 == player) {
        if (uiState.boxes.Box7 == player) {
            if (uiState.boxes.Box3 == "") {
                Box = 3
            }
        }
    }

    // box4

    if (uiState.boxes.Box5 == player) {
        if (uiState.boxes.Box6 == player) {
            if (uiState.boxes.Box4 == "") {
                Box = 4
            }
        }
    }
    if (uiState.boxes.Box1 == player) {
        if (uiState.boxes.Box7 == player) {
            if (uiState.boxes.Box4 == "") {
                Box = 4
            }
        }
    }

    // box5

    if (uiState.boxes.Box1 == player) {
        if (uiState.boxes.Box9 == player) {
            if (uiState.boxes.Box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.boxes.Box3 == player) {
        if (uiState.boxes.Box7 == player) {
            if (uiState.boxes.Box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.boxes.Box2 == player) {
        if (uiState.boxes.Box8 == player) {
            if (uiState.boxes.Box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.boxes.Box4 == player) {
        if (uiState.boxes.Box6 == player) {
            if (uiState.boxes.Box5 == "") {
                Box = 5
            }
        }
    }

    // box6

    if (uiState.boxes.Box3 == player) {
        if (uiState.boxes.Box9 == player) {
            if (uiState.boxes.Box6 == "") {
                Box = 6
            }
        }
    }
    if (uiState.boxes.Box4 == player) {
        if (uiState.boxes.Box5 == player) {
            if (uiState.boxes.Box6 == "") {
                Box = 6
            }
        }
    }

    // box7

    if (uiState.boxes.Box3 == player) {
        if (uiState.boxes.Box5 == player) {
            if (uiState.boxes.Box7 == "") {
                Box = 7
            }
        }
    }
    if (uiState.boxes.Box1 == player) {
        if (uiState.boxes.Box4 == player) {
            if (uiState.boxes.Box7 == "") {
                Box = 7
            }
        }
    }
    if (uiState.boxes.Box9 == player) {
        if (uiState.boxes.Box8 == player) {
            if (uiState.boxes.Box7 == "") {
                Box = 7
            }
        }
    }

    // box8

    if (uiState.boxes.Box2 == player) {
        if (uiState.boxes.Box5 == player) {
            if (uiState.boxes.Box8 == "") {
                Box = 8
            }
        }
    }
    if (uiState.boxes.Box7 == player) {
        if (uiState.boxes.Box9 == player) {
            if (uiState.boxes.Box8 == "") {
                Box = 8
            }
        }
    }

    // box9

    if (uiState.boxes.Box3 == player) {
        if (uiState.boxes.Box6 == player) {
            if (uiState.boxes.Box9 == "") {
                Box = 9
            }
        }
    }
    if (uiState.boxes.Box7 == player) {
        if (uiState.boxes.Box8 == player) {
            if (uiState.boxes.Box9 == "") {
                Box = 9
            }
        }
    }
    if (uiState.boxes.Box1 == player) {
        if (uiState.boxes.Box5 == player) {
            if (uiState.boxes.Box9 == "") {
                Box = 9
            }
        }
    }

    return Box

}