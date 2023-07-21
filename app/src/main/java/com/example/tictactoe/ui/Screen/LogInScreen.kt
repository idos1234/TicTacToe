package com.example.tictactoe.ui.Screen

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
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.data.isValid
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes
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
        mutableStateOf<MainPlayerUiState?>(null)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(BackGround))
    {
        Spacer(modifier = Modifier.height(80.dp))
        Text(text = "Welcome To (app name)", fontSize = 30.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(100.dp))

        LogINInputForm(
            playerUiState = uiState,
            onValueChange = viewModel::updateUiState,
            onEmailValueChange = viewModel::updateEmail,
            emailsharedPreferences = emailsharedPreferences,
        )

        if (isPasswordOrNameEmpty) {
            Toast.makeText(context, "You have to fill all of the labels", Toast.LENGTH_SHORT).show()
            isPasswordOrNameEmpty = false
        }

        Spacer(modifier = Modifier.height(60.dp))

        if (!isEnabled) {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (uiState.isValid()) {
                    isEnabled = false

                    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

                    db.collection("Players").get()
                        .addOnSuccessListener { queryDocumentSnapshots ->
                            if (!queryDocumentSnapshots.isEmpty) {
                                val list = queryDocumentSnapshots.documents
                                for (d in list) {
                                    val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
                                    if (p?.name == uiState.name){
                                        player = p
                                    }
                                }

                                if (player != null ) {
                                    if (player?.password != uiState.password) {
                                        Toast.makeText(
                                            context,
                                            "Password was not correct",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        isEnabled = true
                                    } else {
                                        Toast.makeText(
                                            context, "Welcome to (App Name)", Toast.LENGTH_SHORT
                                        ).show()

                                        onClick()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Player was not found",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    isEnabled = true
                                }
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Fail to get the data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
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
    onValueChange: (MainPlayerUiState) -> Unit = {},
    playerUiState: MainPlayerUiState,
    emailsharedPreferences: sharedPreferences,
    onEmailValueChange: (sharedPreferences) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    var showPassword by remember {
        mutableStateOf(false)
    }

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