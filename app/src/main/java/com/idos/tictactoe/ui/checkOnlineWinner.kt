package com.idos.tictactoe.ui

import com.idos.tictactoe.data.Boxes

fun checkOnlineWinner(boxes: Boxes): String {
    var winner = ""

    // first row

    if(boxes.box1 == "X") {
        if(boxes.box2 == "X") {
            if(boxes.box3 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.box1 == "O") {
        if(boxes.box2 == "O") {
            if(boxes.box3 == "O"){
                winner = "O"
            }
        }
    }

    // second row

    if(boxes.box4 == "X") {
        if(boxes.box5 == "X") {
            if(boxes.box6 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.box4 == "O") {
        if(boxes.box5 == "O") {
            if(boxes.box6 == "O"){
                winner = "O"
            }
        }
    }

    // third row

    if(boxes.box7 == "X") {
        if(boxes.box8 == "X") {
            if(boxes.box9 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.box7 == "O") {
        if(boxes.box8 == "O") {
            if(boxes.box9 == "O"){
                winner = "O"
            }
        }
    }

    // first column

    if(boxes.box1 == "X") {
        if(boxes.box4 == "X") {
            if(boxes.box7 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.box1 == "O") {
        if(boxes.box4 == "O") {
            if(boxes.box7 == "O"){
                winner = "O"
            }
        }
    }

    // second column

    if(boxes.box2 == "X") {
        if(boxes.box5 == "X") {
            if(boxes.box8 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.box2 == "O") {
        if(boxes.box5 == "O") {
            if(boxes.box8 == "O"){
                winner = "O"
            }
        }
    }

    // third column

    if(boxes.box3 == "X") {
        if(boxes.box6 == "X") {
            if(boxes.box9 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.box3 == "O") {
        if(boxes.box6 == "O") {
            if(boxes.box9 == "O"){
                winner = "O"
            }
        }
    }

    // first diagonal line

    if(boxes.box1 == "X") {
        if(boxes.box5 == "X") {
            if(boxes.box9 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.box1 == "O") {
        if(boxes.box5 == "O") {
            if(boxes.box9 == "O"){
                winner = "O"
            }
        }
    }

    // second diagonal line

    if(boxes.box3 == "X") {
        if(boxes.box5 == "X") {
            if(boxes.box7 == "X"){
                winner = "X"
            }
        }
    }

    if(boxes.box3 == "O") {
        if(boxes.box5 == "O") {
            if(boxes.box7 == "O"){
                winner = "O"
            }
        }
    }

    return winner

}