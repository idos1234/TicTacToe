package com.idos.tictactoe.ui.Screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idos.tictactoe.data.isValid
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Secondery
import com.idos.tictactoe.ui.theme.Shapes
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LogInScreen(
    viewModel: SignUpViewModel,
    context: Context,
    emailsharedPreferences: sharedPreferences,
    onSignInClick: () -> Unit,
    onClick: () -> Unit
) {
    val uiState = viewModel.playerUiState
    var isEnabled by remember {
        mutableStateOf(true)
    }
    var isPasswordOrNameEmpty by remember {
        mutableStateOf(false)
    }
    var player by remember {
        mutableStateOf<com.idos.tictactoe.data.MainPlayerUiState?>(null)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(BackGround))
    {
        Spacer(modifier = Modifier.height(80.dp))
        Text(text = "Welcome again", fontSize = 30.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(100.dp))

        //login input form(name + password)
        LogINInputForm(
            playerUiState = uiState,
            onValueChange = viewModel::updateUiState,
            onEmailValueChange = viewModel::updateEmail,
            emailsharedPreferences = emailsharedPreferences,
        )

        //check if password or name labels empty
        if (isPasswordOrNameEmpty) {
            Toast.makeText(context, "You have to fill all of the labels", Toast.LENGTH_SHORT).show()
            isPasswordOrNameEmpty = false
        }

        Spacer(modifier = Modifier.height(60.dp))

        //progressbar
        if (!isEnabled) {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(20.dp))


        Button(
            onClick = {
                //check if name and password is valid
                if (uiState.isValid()) {
                    isEnabled = false

                    //get database
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
                                        com.idos.tictactoe.data.MainPlayerUiState::class.java)
                                    //find player using database
                                    if (p?.name == uiState.name){
                                        player = p
                                    }
                                }

                                //check if player found
                                if (player != null ) {
                                    //check if password was not correct
                                    if (player?.password != uiState.password) {
                                        Toast.makeText(
                                            context,
                                            "Password was not correct",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        isEnabled = true
                                    }
                                    //log in to app
                                    else {
                                        Toast.makeText(
                                            context, "Welcome to (App Name)", Toast.LENGTH_SHORT
                                        ).show()

                                        onClick()
                                    }
                                }
                                //player was not found
                                else {
                                    Toast.makeText(
                                        context,
                                        "Player was not found",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    isEnabled = true
                                }
                            }
                        }
                        //on failure
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Fail to get the data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                //if name and password not valid
                else {
                    isPasswordOrNameEmpty = true
                }
            },
            shape = Shapes.large,
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(Secondery),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)

        ) {
            Text(text = "Next", fontWeight = FontWeight.SemiBold, fontSize = 30.sp)
        }

        Spacer(modifier = Modifier.height(15.dp))

        //navigation to sign in
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Don't have an account?")
            TextButton(onClick = onSignInClick) {
                Text(text = "Sign in", color = Color.Yellow)
            }
        }

    }
}

@Composable
fun LogINInputForm(
    onValueChange: (com.idos.tictactoe.data.MainPlayerUiState) -> Unit = {},
    playerUiState: com.idos.tictactoe.data.MainPlayerUiState,
    emailsharedPreferences: sharedPreferences,
    onEmailValueChange: (sharedPreferences) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    var showPassword by remember {
        mutableStateOf(false)
    }

    //name text field
    OutlinedTextField(
        value = playerUiState.name,
        onValueChange = { onValueChange(playerUiState.copy(name = it))
            onEmailValueChange(emailsharedPreferences.copy(name2 = it)) },
        singleLine = true,
        placeholder = { Text(text = "Name") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Secondery,
        ),
        shape = Shapes.large,
    )

    Spacer(modifier = Modifier.height(15.dp))

    //password text field
    OutlinedTextField(
        value = playerUiState.password,
        onValueChange = { onValueChange(playerUiState.copy(password = it)) },
        singleLine = true,
        placeholder = { Text(text = "Password") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Secondery),
        shape = Shapes.large,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (showPassword)
                Icons.Default.Visibility
            else Icons.Filled.VisibilityOff

            // Localized description for accessibility services
            val description = if (showPassword) "Hide password" else "Show password"

            // Toggle button to hide or display password
            IconButton(onClick = {showPassword = !showPassword}){
                Icon(imageVector  = image, description)
            }
        }
    )
}