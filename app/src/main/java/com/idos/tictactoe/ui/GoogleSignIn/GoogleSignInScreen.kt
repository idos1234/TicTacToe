package com.idos.tictactoe.ui.GoogleSignIn

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.android.gms.common.api.ApiException
import com.google.firebase.firestore.FirebaseFirestore
import com.idos.tictactoe.ui.Screen.GameScreen
import com.idos.tictactoe.ui.Screen.toSHA256

@Composable
fun GoogleSignInScreen(viewModel: GoogleSignInViewModel, navController: NavController, changeEmail: (String) -> Unit, onClick: () -> Unit) {
    var text by remember {
        mutableStateOf<String?>(null)
    }
    val user by remember(viewModel) {
        viewModel.googleUser
    }.collectAsState()
    val signInRequestCode = 1
    val context = LocalContext.current
    var isError by remember {
        mutableStateOf(false)
    }
    var searchedPlayer by remember {
        mutableStateOf(false)
    }

    val authResultLauncher = rememberLauncherForActivityResult(contract = GoogleApiContract()) {
        try {
            val account = it?.getResult(ApiException::class.java)
            //if sign in failed
            if (account == null) {
                text = "Google sign in failed"
                isError = true
            } else {
                //getting user data
                viewModel.fetchSignInUser(email = account.email!!, name = account.displayName!!)
            }
        } catch (e: ApiException) {
            text = e.localizedMessage
        }
    }

    //sign in button
    ScreenView(
        onClick = {
            authResultLauncher.launch(signInRequestCode)
                  },
        isError = isError,
        context = context
    )

    user?.let {
        var isPlayerExisted by remember {
            mutableStateOf(false)
        }
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        //get Players collection from database
        db.collection("Players").get()
            //on success
            .addOnSuccessListener { queryDocumentSnapshots ->
                //check if collection is empty
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    for (d in list) {
                        val p: com.idos.tictactoe.data.MainPlayerUiState? = d.toObject(
                            com.idos.tictactoe.data.MainPlayerUiState::class.java
                        )
                        //check if email already used
                        if (p?.email == user!!.email?.toSHA256()) {
                            isPlayerExisted = true
                        }
                    }
                }
                searchedPlayer = true

            }

            //if user is existed
        if (isPlayerExisted) {
            //log in
            changeEmail(user?.email!!.toSHA256())
            onClick()
        } else if (!isPlayerExisted && searchedPlayer) {
            //sign up
            viewModel.updateEmail(viewModel.emailState.copy(email2 = user!!.email))
            navController.navigate(GameScreen.NewName.name)
        }
    }
}

@Composable
fun ScreenView(
    onClick: () -> Unit,
    isError: Boolean = false,
    context: Context
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onClick() }) {
            Text(text = "Sign in with Google")
        }

        when {
            isError -> {
                isError.let {
                    Toast.makeText(
                        context,
                        "Something went wrong...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}