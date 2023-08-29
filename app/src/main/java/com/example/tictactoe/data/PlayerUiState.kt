package com.example.tictactoe.data

import com.example.tictactoe.R

//data class for every player in database
data class MainPlayerUiState(
    //player's name
    var name: String = "",
    //player's email
    var email: String = "",
    //player's score
    var score: Int = 0,
    //player's password
    var password: String = "",
    //player's image
    var currentImage: Int = R.drawable.xo_1,
    //images that the player got
    var unlockedImages: List<Int> = listOf(R.drawable.xo_1),
    //images that the player didn't got
    var lockedImages: List<Int> = listOf(
        R.drawable.xo_2,
        R.drawable.xo_3,
        R.drawable.xo_4,
        R.drawable.xo_5_1,
        R.drawable.xo_5_2,
        R.drawable.xo_6,
        R.drawable.xo_7,
        R.drawable.xo_8,
        R.drawable.xo_9,
        R.drawable.xo_10_1,
        R.drawable.xo_10_2,
        R.drawable.xo_11,
        R.drawable.xo_12,
        R.drawable.xo_13,
        R.drawable.xo_14,
        R.drawable.xo_15_1,
        R.drawable.xo_15_2
    ),
    var wins: Int = 0,
    var loses: Int = 0,
    var draws: Int = 0,
    var level: Int = 1
)

//check if name and password is valid in sign in and log in
fun MainPlayerUiState.isValid() : Boolean {
    return name.isNotEmpty() && password.isNotEmpty()
}