package com.idos.tictactoe.ui.GoogleSignIn

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.WorkManager.SetNewDelay
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.ui.Screen.GameScreen
import com.idos.tictactoe.ui.Screen.toSHA256

@Composable
fun GoogleSignInScreen(viewModel: GoogleSignInViewModel, navController: NavController, changeEmail: (String) -> Unit, onClick: @Composable () -> Unit) {
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
    var setDelay by remember {
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

        //get database
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Players")
        //get Players collection from database
        databaseReference.addValueEventListener(object : ValueEventListener {
            //on success
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                 try {
                     list.find {
                         it.getValue(MainPlayerUiState::class.java)!!.email == user?.email!!.toSHA256()
                     }?.getValue(MainPlayerUiState::class.java)!!
                     isPlayerExisted = true
                     searchedPlayer = true
                } catch (_: Exception) {
                     isPlayerExisted = false
                     searchedPlayer = true
                }

            }
            override fun onCancelled(error: DatabaseError) {}
        })

        //if user is existed
        if (isPlayerExisted) {
            //log in
            changeEmail(user?.email!!.toSHA256())
            if(!setDelay) {
                SetNewDelay(
                    hour = 11,
                    min = 0,
                    context = context
                )
                setDelay = true
            }
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