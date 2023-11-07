package com.idos.tictactoe.ui.Screen.GoogleSignIn

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.common.api.ApiException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun GoogleSignInScreen(viewModel: GoogleSignInViewModel, onClick: () -> Unit) {
    val signInRequestCode = 1
    val context = LocalContext.current

    val mSignInViewModel: GoogleSignInViewModel = viewModel(
        factory = SignInGoogleViewModelFactory(context.applicationContext as Application)
    )

    val state = mSignInViewModel.googleUser.observeAsState()
    val user = state.value

    val isError = rememberSaveable { mutableStateOf(false) }

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)

                if (gsa != null) {
                    mSignInViewModel.fetchSignInUser(gsa.email, gsa.displayName)
                } else {
                    isError.value = true
                }
            } catch (e: ApiException) {
                Log.d("Error in AuthScreen%s", e.toString())
            }
        }
    
    ScreenView(
        onClick = { authResultLauncher.launch(signInRequestCode) },
        isError = isError.value,
        mSignInViewModel = mSignInViewModel,
        context = context
    )

    user?.let {
        mSignInViewModel.hideLoading()

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
                        if (p?.email == user.email) {
                            isPlayerExisted = true
                        }
                    }
                }
            }

        if (isPlayerExisted) {
            viewModel.updateEmail(user.email)
            onClick()
        } else {
            val dbPlayers: CollectionReference = db.collection("Players")

            val player = com.idos.tictactoe.data.MainPlayerUiState(
                name = "",
                email = user.email!!,
                score = 0,
                password = ""
            )

            dbPlayers.add(player)
                //on success
                .addOnSuccessListener {
                    viewModel.updateEmail(user.email)
                    onClick()
                }
                //on failure
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Fail! Try again \n$e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}

@Composable
fun ScreenView(
    onClick: () -> Unit,
    isError: Boolean = false,
    mSignInViewModel: GoogleSignInViewModel,
    context: Context
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onClick() }) {
            Text(text = "SignIn with Google")
        }

        when {
            isError -> {
                isError.let {
                    mSignInViewModel.hideLoading()
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