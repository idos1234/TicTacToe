package com.idos.tictactoe.ui.Screen.GoogleSignIn

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoogleSignInViewModel(aplication: Application): AndroidViewModel(aplication) {
    private var _userState = MutableLiveData<GoogleUserModel>()
    val googleUser: LiveData<GoogleUserModel> = _userState

    private var _loadingState = MutableLiveData(false)

    private val _emailState = MutableStateFlow(GoogleEmail())
    val emailState: StateFlow<GoogleEmail> = _emailState.asStateFlow()

    fun fetchSignInUser(email: String?, name: String?) {
        _loadingState.value = true

        viewModelScope.launch {
            _userState.value = GoogleUserModel(
                email = email,
                name = name,
            )
        }

        _loadingState.value = false
    }

    fun hideLoading() {
        _loadingState.value = false
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

class SignInGoogleViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(GoogleSignInViewModel::class.java)) {
            return GoogleSignInViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}