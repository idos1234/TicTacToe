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
    var currentImage: Int = R.drawable.ic_launcher_foreground,
    //images that the player got
    var unlockedImages: List<String> = listOf(),
    //images that the player didn't got
    var lockedImages: List<String> = listOf()
)

//check if name and password is valid in sign in and log in
fun MainPlayerUiState.isValid() : Boolean {
    return name.isNotEmpty() && password.isNotEmpty()
}