package com.idos.tictactoe.ui.Screen.GoogleSignIn

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.android.gms.common.api.ApiException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.idos.tictactoe.ui.Screen.GameScreen

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
    var checkedPlayer by remember {
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
        onClick = { authResultLauncher.launch(signInRequestCode) },
        isError = isError,
        context = context
    )

    user?.let {
        var times by remember {
            mutableIntStateOf(0)
        }
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

            //if user is existed
        if (isPlayerExisted) {
            if (times == 0) {
                //log in
                changeEmail(user?.email!!)
                onClick()
            }
        } else if (!isPlayerExisted && checkedPlayer) {
            if (times == 0) {
                //sign up
                viewModel.updateEmail(viewModel.emailState.copy(email2 = user!!.email))
                navController.navigate(GameScreen.NewName.name)
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ChooseName(
    viewModel: GoogleSignInViewModel,
    context: Context,
    emailState: GoogleEmail,
    changeEmail: (String) -> Unit,
    onClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        AlertDialog(
            onDismissRequest = {},
            text = {
                   TextField(value = emailState.name!!, onValueChange = {viewModel.updateEmail(emailState.copy(name = it))})
            },
            buttons = {
                Button(onClick = {
                    //if label is empty
                    if (viewModel.emailState.name!! == "") {
                        Toast.makeText(
                            context,
                            "You have to fill the label",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else
                        //if name is bigger than 8 chars
                        if (emailState.name!!.length > 8) {
                        Toast.makeText(
                            context,
                            "Maximum chars: 8",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // sign up
                        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                        val dbPlayers: CollectionReference = db.collection("Players")

                        val player = com.idos.tictactoe.data.MainPlayerUiState(
                            name = emailState.name!!,
                            email = emailState.email2!!,
                            score = 0,
                            password = ""
                        )

                        dbPlayers.add(player)
                            //on success
                            .addOnSuccessListener {
                                changeEmail(emailState.email2!!)
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
                }) {
                    Text(text = "Next")
                }
            }
        )
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