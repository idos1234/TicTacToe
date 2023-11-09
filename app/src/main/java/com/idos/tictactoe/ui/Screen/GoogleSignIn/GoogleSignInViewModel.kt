package com.idos.tictactoe.ui.Screen.GoogleSignIn

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GoogleSignInViewModel(): ViewModel() {
    private var _userState: MutableStateFlow<GoogleUserModel?> = MutableStateFlow(null)
    var googleUser: StateFlow<GoogleUserModel?> = _userState

    private val _emailState = MutableStateFlow(GoogleEmail())
    val emailState: StateFlow<GoogleEmail> = _emailState.asStateFlow()

    fun fetchSignInUser(email: String?, name: String?) {
        _userState.value =  GoogleUserModel(name, email)
    }

    fun updateEmail(newEmail: String?) {
        _emailState.value = GoogleEmail(newEmail)
    }
}

data class GoogleEmail(
    var email: String? = ""
)