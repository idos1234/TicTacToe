package com.idos.tictactoe.ui.GoogleSignIn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GoogleSignInViewModel(): ViewModel() {
    private var _userState: MutableStateFlow<GoogleUserModel?> = MutableStateFlow(null)
    var googleUser: StateFlow<GoogleUserModel?> = _userState

    var emailState by mutableStateOf(GoogleEmail())

    fun fetchSignInUser(email: String?, name: String?) {
        _userState.value =  GoogleUserModel(name, email)
    }

    fun updateEmail(newEmail: GoogleEmail) {
        emailState = newEmail.copy()
    }

}

data class GoogleEmail(
    var email2: String? = "",
    var name: String? = "",
)