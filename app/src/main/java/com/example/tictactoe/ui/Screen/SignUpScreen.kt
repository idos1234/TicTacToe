@file:Suppress("DEPRECATION")

package com.example.tictactoe.ui.Screen

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.data.isValid
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    context: Context,
    emailsharedPreferences: sharedPreferences,
    onClick: () -> Unit,
    signUpViewModel: SignUpViewModel,
    onLogInClick: () -> Unit,
    ) {

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

        SignUpInputForm(
            playerUiState = uiState,
            onValueChange = viewModel::updateUiState,
            onEmailValueChange = viewModel::updateEmail,
            emailsharedPreferences = emailsharedPreferences,
            signUpViewModel = signUpViewModel
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

        checkEmail(uiState.email)

        Button(
            onClick = {
             if (uiState.isValid() && signUpViewModel.verifiedPassword.value.isNotEmpty() && uiState.email.isNotEmpty()) {
                 if (uiState.password != signUpViewModel.verifiedPassword.value) {
                     Toast.makeText(context, "Password was not verified", Toast.LENGTH_SHORT).show()
                 } else {

                     isEnabled = false

                     val db: FirebaseFirestore = FirebaseFirestore.getInstance()

                     db.collection("Players").get()
                         .addOnSuccessListener { queryDocumentSnapshots ->
                             if (!queryDocumentSnapshots.isEmpty) {
                                 val list = queryDocumentSnapshots.documents
                                 for (d in list) {
                                     val p: MainPlayerUiState? = d.toObject(MainPlayerUiState::class.java)
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
                                 if (!alreadyUsed) {
                                     val dbPlayers: CollectionReference = db.collection("Players")

                                     val player = MainPlayerUiState(
                                         uiState.name,
                                         uiState.email,
                                         0,
                                         uiState.password,
                                     )

                                     dbPlayers.add(player).addOnSuccessListener {
                                         Toast.makeText(
                                             context, "Welcome to (App Name)", Toast.LENGTH_SHORT
                                         ).show()

                                         onClick()

                                     }.addOnFailureListener { e ->
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
    onValueChange: (MainPlayerUiState) -> Unit = {},
    playerUiState: MainPlayerUiState,
    emailsharedPreferences: sharedPreferences,
    onEmailValueChange: (sharedPreferences) -> Unit,
    signUpViewModel: SignUpViewModel
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

val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
    "[a-zA-Z0-9+._%\\-]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)


private fun checkEmail(email: String): Boolean {
    return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
}