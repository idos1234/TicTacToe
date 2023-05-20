package com.example.tictactoe.ui

import com.example.tictactoe.data.UiState

fun WillWin (uiState: UiState): Int {

    var Box = 0

    // box1

    if (uiState.Box2 == "X") {
        if (uiState.Box3 == "X") {
            if (uiState.Box1 == "") {
                Box = 1
            }
        }
    }
    if (uiState.Box2 == "X") {
        if (uiState.Box3 == "X") {
            if (uiState.Box1 == "") {
                Box = 1
            }
        }
    }
    if (uiState.Box5 == "X") {
        if (uiState.Box9 == "X") {
            if (uiState.Box1 == "") {
                Box = 1
            }
        }
    }

    // box2

    if (uiState.Box1 == "X") {
        if (uiState.Box3 == "X") {
            if (uiState.Box2 == "") {
                Box = 2
            }
        }
    }
    if (uiState.Box5 == "X") {
        if (uiState.Box8 == "X") {
            if (uiState.Box2 == "") {
                Box = 2
            }
        }
    }

    // box3

    if (uiState.Box2 == "X") {
        if (uiState.Box1 == "X") {
            if (uiState.Box3 == "") {
                Box = 3
            }
        }
    }
    if (uiState.Box6 == "X") {
        if (uiState.Box9 == "X") {
            if (uiState.Box3 == "") {
                Box = 3
            }
        }
    }
    if (uiState.Box5 == "X") {
        if (uiState.Box7 == "X") {
            if (uiState.Box3 == "") {
                Box = 3
            }
        }
    }

    // box4

    if (uiState.Box5 == "X") {
        if (uiState.Box6 == "X") {
            if (uiState.Box4 == "") {
                Box = 4
            }
        }
    }
    if (uiState.Box1 == "X") {
        if (uiState.Box7 == "X") {
            if (uiState.Box4 == "") {
                Box = 4
            }
        }
    }

    // box5

    if (uiState.Box1 == "X") {
        if (uiState.Box9 == "X") {
            if (uiState.Box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.Box3 == "X") {
        if (uiState.Box7 == "X") {
            if (uiState.Box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.Box2 == "X") {
        if (uiState.Box8 == "X") {
            if (uiState.Box5 == "") {
                Box = 5
            }
        }
    }
    if (uiState.Box4 == "X") {
        if (uiState.Box6 == "X") {
            if (uiState.Box5 == "") {
                Box = 5
            }
        }
    }

    // box6

    if (uiState.Box3 == "X") {
        if (uiState.Box9 == "X") {
            if (uiState.Box6 == "") {
                Box = 6
            }
        }
    }
    if (uiState.Box4 == "X") {
        if (uiState.Box5 == "X") {
            if (uiState.Box6 == "") {
                Box = 6
            }
        }
    }

    // box7

    if (uiState.Box3 == "X") {
        if (uiState.Box5 == "X") {
            if (uiState.Box7 == "") {
                Box = 7
            }
        }
    }
    if (uiState.Box1 == "X") {
        if (uiState.Box4 == "X") {
            if (uiState.Box7 == "") {
                Box = 7
            }
        }
    }
    if (uiState.Box9 == "X") {
        if (uiState.Box8 == "X") {
            if (uiState.Box7 == "") {
                Box = 7
            }
        }
    }

    // box8

    if (uiState.Box2 == "X") {
        if (uiState.Box5 == "X") {
            if (uiState.Box8 == "") {
                Box = 8
            }
        }
    }
    if (uiState.Box7 == "X") {
        if (uiState.Box9 == "X") {
            if (uiState.Box8 == "") {
                Box = 8
            }
        }
    }

    // box9

    if (uiState.Box3 == "X") {
        if (uiState.Box6 == "X") {
            if (uiState.Box9 == "") {
                Box = 9
            }
        }
    }
    if (uiState.Box7 == "X") {
        if (uiState.Box8 == "X") {
            if (uiState.Box9 == "") {
                Box = 9
            }
        }
    }
    if (uiState.Box1 == "X") {
        if (uiState.Box5 == "X") {
            if (uiState.Box9 == "") {
                Box = 9
            }
        }
    }

    return Box

}