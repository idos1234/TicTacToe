package com.idos.tictactoe.ui.Screen

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
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
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idos.tictactoe.data.isValid
import com.idos.tictactoe.ui.theme.BackGround
import com.idos.tictactoe.ui.theme.Secondery
import com.idos.tictactoe.ui.theme.Shapes
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    context: Context,
    emailsharedPreferences: SignUpName,
    onClick: () -> Unit,
    signUpViewModel: SignUpViewModel,
    onLogInClick: () -> Unit,
    ) {

    //player state
    val uiState = viewModel.playerUiState
    var isPasswordOrNameEmpty by remember {
        mutableStateOf(false)
    }
    var isEnabled by remember {
        mutableStateOf(true)
    }
    var alreadyUsed by remember {
        mutableStateOf(false)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(BackGround))
    {
        Spacer(modifier = Modifier.height(80.dp))
        Text(text = "Welcome To (app name)", fontSize = 30.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(100.dp))

        //sign in input form
        SignUpInputForm(
            playerUiState = uiState,
            onValueChange = viewModel::updateUiState,
            onEmailValueChange = viewModel::updateEmail,
            emailsharedPreferences = emailsharedPreferences,
            signUpViewModel = signUpViewModel
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
                //check if player is valid
                if (uiState.isValid() && signUpViewModel.verifiedPassword.value.isNotEmpty() && uiState.email.isNotEmpty()) {
                 //if password eas not verified
                 if (uiState.password != signUpViewModel.verifiedPassword.value) {
                     Toast.makeText(context, "Password was not verified", Toast.LENGTH_SHORT).show()
                 }
                 //if email is not valid
                 if (!isValidEmail(uiState.email)) {
                     Toast.makeText(context, "Email is not valid", Toast.LENGTH_SHORT).show()
                 } else {

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
                                     //check if name already used
                                     if (p?.name == uiState.name) {
                                         if (!alreadyUsed) {
                                             Toast.makeText(
                                                 context,
                                                 "This name already used",
                                                 Toast.LENGTH_SHORT
                                             ).show()
                                         }

                                         alreadyUsed = true
                                         isEnabled = true
                                     }
                                     //check if email already used
                                     if (p?.email == uiState.email) {
                                         if (p.name == uiState.name) {
                                             Toast.makeText(
                                                 context,
                                                 "This email already used",
                                                 Toast.LENGTH_SHORT
                                             ).show()
                                         }

                                         alreadyUsed = true
                                         isEnabled = true
                                     }
                                 }
                                 //sign in to app
                                 if (!alreadyUsed) {
                                     val dbPlayers: CollectionReference = db.collection("Players")

                                     val player = com.idos.tictactoe.data.MainPlayerUiState(
                                         name = uiState.name,
                                         email = uiState.email,
                                         score = 0,
                                         password = uiState.password
                                     )

                                     dbPlayers.add(player)
                                         //on success
                                         .addOnSuccessListener {
                                         Toast.makeText(
                                             context, "Welcome to (App Name)", Toast.LENGTH_SHORT
                                         ).show()

                                         onClick()

                                     }
                                         //on failure
                                         .addOnFailureListener { e ->
                                         Toast.makeText(
                                             context,
                                             "Fail! Try again \n$e",
                                             Toast.LENGTH_SHORT
                                         ).show()
                                         isEnabled = true
                                     }
                                 }
                             }
                         }
                         //on failure
                         .addOnFailureListener {
                             Toast.makeText(
                                 context,
                                 "Fail to get the data.",
                                 Toast.LENGTH_SHORT
                             ).show()
                         }
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
            Text(text = "Sign in", fontWeight = FontWeight.SemiBold, fontSize = 30.sp)
            }

        Spacer(modifier = Modifier.height(15.dp))

        //navigation to log in
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Already have an account?")
            TextButton(onClick = onLogInClick) {
                Text(text = "Log in", color = Color.Yellow)
            }
        }
    }
}

@Composable
fun SignUpInputForm(
    onValueChange: (com.idos.tictactoe.data.MainPlayerUiState) -> Unit = {},
    playerUiState: com.idos.tictactoe.data.MainPlayerUiState,
    emailsharedPreferences: SignUpName,
    onEmailValueChange: (SignUpName) -> Unit,
    signUpViewModel: SignUpViewModel
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

    //email text field
    OutlinedTextField(
        value = playerUiState.email,
        onValueChange = { onValueChange(playerUiState.copy(email = it)) },
        singleLine = true,
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
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
            imeAction = ImeAction.Next,
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

    Spacer(modifier = Modifier.height(15.dp))

    //verify password text field
    OutlinedTextField(
        value = signUpViewModel.verifiedPassword.value,
        onValueChange = { signUpViewModel.verifiedPassword.value = it },
        singleLine = true,
        placeholder = { Text(text = "Verify password") },
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
    Spacer(modifier = Modifier.height(15.dp))
}

fun isValidEmail(target: CharSequence?): Boolean {
    return if (TextUtils.isEmpty(target)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(target!!).matches()
    }
}