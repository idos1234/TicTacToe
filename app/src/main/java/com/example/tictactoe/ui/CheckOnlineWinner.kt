package com.example.tictactoe.ui.Screen

import com.example.tictactoe.data.Boxes

fun CheckOnlineWinner(boxes: Boxes): String {
    var winner = ""

    // first row

    if(boxes.Box1 == "X") {
        if(boxes.Box2 == "X") {
            if(boxes.Box3 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.Box1 == "O") {
        if(boxes.Box2 == "O") {
            if(boxes.Box3 == "O"){
                winner = "O"
            }
        }
    }

    // second row

    if(boxes.Box4 == "X") {
        if(boxes.Box5 == "X") {
            if(boxes.Box6 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.Box4 == "O") {
        if(boxes.Box5 == "O") {
            if(boxes.Box6 == "O"){
                winner = "O"
            }
        }
    }

    // third row

    if(boxes.Box7 == "X") {
        if(boxes.Box8 == "X") {
            if(boxes.Box9 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.Box7 == "O") {
        if(boxes.Box8 == "O") {
            if(boxes.Box9 == "O"){
                winner = "O"
            }
        }
    }

    // first column

    if(boxes.Box1 == "X") {
        if(boxes.Box4 == "X") {
            if(boxes.Box7 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.Box1 == "O") {
        if(boxes.Box4 == "O") {
            if(boxes.Box7 == "O"){
                winner = "O"
            }
        }
    }

    // second column

    if(boxes.Box2 == "X") {
        if(boxes.Box5 == "X") {
            if(boxes.Box8 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.Box2 == "O") {
        if(boxes.Box5 == "O") {
            if(boxes.Box8 == "O"){
                winner = "O"
            }
        }
    }

    // third column

    if(boxes.Box3 == "X") {
        if(boxes.Box6 == "X") {
            if(boxes.Box9 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.Box3 == "O") {
        if(boxes.Box6 == "O") {
            if(boxes.Box9 == "O"){
                winner = "O"
            }
        }
    }

    // first diagonal line

    if(boxes.Box1 == "X") {
        if(boxes.Box5 == "X") {
            if(boxes.Box9 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.Box1 == "O") {
        if(boxes.Box5 == "O") {
            if(boxes.Box9 == "O"){
                winner = "O"
            }
        }
    }

    // second diagonal line

    if(boxes.Box3 == "X") {
        if(boxes.Box5 == "X") {
            if(boxes.Box7 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.Box3 == "O") {
        if(boxes.Box5 == "O") {
            if(boxes.Box7 == "O"){
                winner = "O"
            }
        }
    }

    return winner

}