package com.idos.tictactoe.ui.Screen.GoogleSignIn

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoogleUserModel(
    @Json
    val name: String?,
    @Json
    val email: String?
)
