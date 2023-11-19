package com.idos.tictactoe.ui.Screen.GoogleSignIn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GoogleSignInViewModel(): ViewModel() {
    private var _userState: MutableStateFlow<GoogleUserModel?> = MutableStateFlow(null)
    var googleUser: StateFlow<GoogleUserModel?> = _userState

    var emailState by mutableStateOf(GoogleEmail())

    private val _emailState = MutableStateFlow(GoogleEmail())
    val email: StateFlow<GoogleEmail> = _emailState.asStateFlow()

    fun fetchSignInUser(email: String?, name: String?) {
        _userState.value =  GoogleUserModel(name, email)
    }

    fun updateEmail(newEmail: GoogleEmail) {
        emailState = newEmail.copy()
    }

    fun resetEmail() {
        _emailState.update {
            it.copy(email = "")
        }
    }

}

data class GoogleEmail(
    var email: String? = "",
    var email2: String? = "",
    var name: String? = "",
)