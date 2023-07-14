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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.data.isValid
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    context: Context,
) {

    val uiState = viewModel.playerUiState
    var isPasswordOrNameEmpty by remember {
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
            onValueChange = viewModel::updateUiState)

        if (isPasswordOrNameEmpty) {
            Toast.makeText(context, "You have to fill all of the labels", Toast.LENGTH_SHORT).show()
            isPasswordOrNameEmpty = false
        }

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = {
             if (uiState.isValid() && uiState.password.isNotEmpty()) {
                 val db: FirebaseFirestore = FirebaseFirestore.getInstance()

                 val dbCourses: CollectionReference = db.collection("Players")

                 val courses = MainPlayerUiState(uiState.name, 0, uiState.password, listOf())

                 dbCourses.add(courses).addOnSuccessListener {
                     Toast.makeText(
                         context, "Welcome to (App Name)", Toast.LENGTH_SHORT
                     ).show()

                 }.addOnFailureListener { e ->
                     Toast.makeText(context, "Fail! Try again \n$e", Toast.LENGTH_SHORT).show()
                 }
             } else {
                 isPasswordOrNameEmpty = true
             }
        },
            shape = Shapes.large,
            colors = ButtonDefaults.buttonColors(Secondery),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)

        ) {
            Text(text = "Next", fontWeight = FontWeight.SemiBold, fontSize = 30.sp)
            }
    }
}

@Composable
fun SignUpInputForm(
    onValueChange: (MainPlayerUiState) -> Unit = {},
    playerUiState: MainPlayerUiState
) {
    val focusManager = LocalFocusManager.current

    var showPassword by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = playerUiState.name,
        onValueChange = { onValueChange(playerUiState.copy(name = it)) },
        singleLine = true,
        placeholder = { Text(text = "Username") },
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