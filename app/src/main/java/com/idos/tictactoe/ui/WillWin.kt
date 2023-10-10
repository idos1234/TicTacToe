package com.idos.tictactoe.ui

import com.idos.tictactoe.data.UiState

/**
 * Help the bot to choose which box it should choose
 */

fun WillWin (uiState: UiState, player: String): Int {

    var Box = 0

    // box1

    if (uiState.Box2 == player) {
        if (uiState.Box3 == player) {
            if (uiState.Box1 == "") {
                Box = 1
            }
        }
    }
    if (uiState.Box2 == player) {
        if (uiState.Box3 == player) {
            if (uiState.Box1 == "") {
                Box = 1
            }
        }
    }
    if (uiState.Box5 == player) {
        if (uiState.Box9 == player) {
            if (uiState.Box1 == "") {
                Box = 1
            }
        }
    }

    // box2

    if (uiState.Box1 == player) {
        if (uiState.Box3 == player) {
            if (uiState.Box2 == "") {
                Box = 2
            }
        }
    }
    if (uiState.Box5 == player) {
        if (uiState.Box8 == "X") {
            if (uiState.Box2 == "") {
                Box = 2
            }
        }
    }

    // box3

    if (uiState.Box2 == player) {
        if (uiState.Box1 == player) {
            if (uiState.Box3 == "") {
                Box = 3
            }
        }
    }
    if (uiState.Box6 == player) {
        if (uiState.Box9 == player) {
            if (uiState.Box3 == "") {
                Box = 3
            }
        }
    }
    if (uiState.Box5 == player) {
        if (uiState.Box7 == player) {
            if (uiState.Box3 == "") {
                Box = 3
            }
        }
    }

    // box4

    if (uiState.Box5 == player) {
        if (uiState.Box6 == player) {
            if (uiState.Box4 == "") {
                Box = 4
            }
        }
    }
    if (uiState.Box1 == player) {
        if (uiState.Box7 == player) {
            if (uiState.Box4 == "") {
                Box = 4
            }
        }
    }

    // box5

    if (uiState.Box1 == player) {
        if (uiState.Box9 == player) {
            if (uiState.Box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.Box3 == player) {
        if (uiState.Box7 == player) {
            if (uiState.Box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.Box2 == player) {
        if (uiState.Box8 == player) {
            if (uiState.Box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.Box4 == player) {
        if (uiState.Box6 == player) {
            if (uiState.Box5 == "") {
                Box = 5
            }
        }
    }

    // box6

    if (uiState.Box3 == player) {
        if (uiState.Box9 == player) {
            if (uiState.Box6 == "") {
                Box = 6
            }
        }
    }
    if (uiState.Box4 == player) {
        if (uiState.Box5 == player) {
            if (uiState.Box6 == "") {
                Box = 6
            }
        }
    }

    // box7

    if (uiState.Box3 == player) {
        if (uiState.Box5 == player) {
            if (uiState.Box7 == "") {
                Box = 7
            }
        }
    }
    if (uiState.Box1 == player) {
        if (uiState.Box4 == player) {
            if (uiState.Box7 == "") {
                Box = 7
            }
        }
    }
    if (uiState.Box9 == player) {
        if (uiState.Box8 == player) {
            if (uiState.Box7 == "") {
                Box = 7
            }
        }
    }

    // box8

    if (uiState.Box2 == player) {
        if (uiState.Box5 == player) {
            if (uiState.Box8 == "") {
                Box = 8
            }
        }
    }
    if (uiState.Box7 == player) {
        if (uiState.Box9 == player) {
            if (uiState.Box8 == "") {
                Box = 8
            }
        }
    }

    // box9

    if (uiState.Box3 == player) {
        if (uiState.Box6 == player) {
            if (uiState.Box9 == "") {
                Box = 9
            }
        }
    }
    if (uiState.Box7 == player) {
        if (uiState.Box8 == player) {
            if (uiState.Box9 == "") {
                Box = 9
            }
        }
    }
    if (uiState.Box1 == player) {
        if (uiState.Box5 == player) {
            if (uiState.Box9 == "") {
                Box = 9
            }
        }
    }

    return Box

}