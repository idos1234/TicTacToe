package com.idos.tictactoe.ui.Screen.GoogleSignIn

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GoogleSignInViewModel(aplication: Application): AndroidViewModel(aplication) {
    private var _userState: MutableStateFlow<GoogleUserModel?> = MutableStateFlow(null)
    val googleUser: StateFlow<GoogleUserModel?> = _userState

    private val _emailState = MutableStateFlow(GoogleEmail())
    val emailState: StateFlow<GoogleEmail> = _emailState.asStateFlow()

    suspend fun fetchSignInUser(email: String?, name: String?) {
        delay(2000)
        _userState.value = GoogleUserModel(name, email)
    }

    fun updateEmail(newEmail: String?) {
        _emailState.update {
            it.copy(email = newEmail)
        }
    }
}

data class GoogleEmail(
    var email: String? = ""
)