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
import androidx.compose.ui.platform.LocalContext
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    context: Context,
    databaseReference: DatabaseReference
) {
    val Context = LocalContext.current

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
            Toast.makeText(Context, "You have to fill all of the labels", Toast.LENGTH_SHORT).show()
            isPasswordOrNameEmpty = false
        }

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = {
             if (uiState.isValid() && uiState.password.isNotEmpty()) {
                 val empObj =
                     MainPlayerUiState(name = uiState.name, password = uiState.password, score = 0, secondPlayers = listOf())

                 databaseReference.addValueEventListener(object : ValueEventListener {
                     override fun onDataChange(snapshot: DataSnapshot) {

                         databaseReference.setValue(empObj)

                         Toast.makeText(
                             context,
                             "Data added to Firebase Database",
                             Toast.LENGTH_SHORT
                         ).show()
                     }

                     override fun onCancelled(error: DatabaseError) {

                         Toast.makeText(
                             context,
                             "Fail to add data $error",
                             Toast.LENGTH_SHORT
                         ).show()
                     }
                 })
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
        label = { Text(text = "Username") },
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
        label = { Text(text = "Password") },
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