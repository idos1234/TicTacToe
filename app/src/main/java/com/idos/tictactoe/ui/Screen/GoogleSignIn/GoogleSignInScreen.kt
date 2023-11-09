package com.idos.tictactoe.ui.Screen.GoogleSignIn

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.common.api.ApiException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun GoogleSignInScreen(viewModel: GoogleSignInViewModel, onClick: () -> Unit) {
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
    var checkedPlayer by remember {
        mutableStateOf(false)
    }

    val authResultLauncher = rememberLauncherForActivityResult(contract = GoogleApiContract()) {
        try {
            val account = it?.getResult(ApiException::class.java)
            if (account == null) {
                text = "Google sign in failed"
                isError = true
            } else {
                viewModel.fetchSignInUser(email = account.email!!, name = account.displayName!!)
            }
        } catch (e: ApiException) {
            text = e.localizedMessage
        }
    }
    
    ScreenView(
        onClick = { authResultLauncher.launch(signInRequestCode) },
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
                        if (p?.email == user!!.email) {
                            isPlayerExisted = true
                        }
                    }
                }
                checkedPlayer = true

            }

        if (isPlayerExisted) {
            viewModel.updateEmail(user!!.email)
            onClick()
        } else if (!isPlayerExisted && checkedPlayer){
            val dbPlayers: CollectionReference = db.collection("Players")

            val player = com.idos.tictactoe.data.MainPlayerUiState(
                name = "",
                email = user!!.email!!,
                score = 0,
                password = ""
            )

            dbPlayers.add(player)
                //on success
                .addOnSuccessListener {
                    viewModel.updateEmail(user!!.email)
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