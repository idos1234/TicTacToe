@file:Suppress("DEPRECATION")

package com.example.tictactoe.ui.Screen

import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tictactoe.data.MainPlayerUiState
import com.example.tictactoe.data.isValid
import com.example.tictactoe.ui.theme.BackGround
import com.example.tictactoe.ui.theme.Secondery
import com.example.tictactoe.ui.theme.Shapes
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    context: Context,
    emailsharedPreferences: sharedPreferences,
    onClick: () -> Unit
) {

    val uiState = viewModel.playerUiState
    var isPasswordOrNameEmpty by remember {
        mutableStateOf(false)
    }
    var isEnabled by remember {
        mutableStateOf(true)
    }

    var verifyPassword by remember {
        mutableStateOf("")
    }
    var showPassword by remember {
        mutableStateOf(false)
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(BackGround))
    {
        Spacer(modifier = Modifier.height(80.dp))
        Text(text = "Welcome To (app name)", fontSize = 30.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(125.dp),
            elevation = 10.dp,
            modifier = Modifier
                .size(200.dp)
                .clickable(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
        ) {
            if (selectedImageUri == null) {
                Image(imageVector = Icons.Default.TagFaces, contentDescription = null, contentScale = ContentScale.FillBounds)
            } else {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SignUpInputForm(
            playerUiState = uiState,
            onValueChange = viewModel::updateUiState,
            onEmailValueChange = viewModel::updateEmail,
            emailsharedPreferences = emailsharedPreferences
        )

        OutlinedTextField(
            value = verifyPassword,
            onValueChange = { verifyPassword = it },
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
             if (uiState.isValid() && verifyPassword.isNotEmpty()) {
                 if (uiState.password != verifyPassword) {
                     Toast.makeText(context, "Password was not verified", Toast.LENGTH_SHORT).show()
                 } else {

                     isEnabled = false
                     val db: FirebaseFirestore = FirebaseFirestore.getInstance()

                     val dbPlayers: CollectionReference = db.collection("Players")

                     val player = MainPlayerUiState(
                         uiState.name,
                         0,
                         uiState.password,
                         selectedImageUri.toString()
                     )

                     dbPlayers.add(player).addOnSuccessListener {
                         Toast.makeText(
                             context, "Welcome to (App Name)", Toast.LENGTH_SHORT
                         ).show()

                         onClick()

                     }.addOnFailureListener { e ->
                         Toast.makeText(context, "Fail! Try again \n$e", Toast.LENGTH_SHORT).show()
                         isEnabled = true
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
            Text(text = "Next", fontWeight = FontWeight.SemiBold, fontSize = 30.sp)
            }
    }
}

@Composable
fun SignUpInputForm(
    onValueChange: (MainPlayerUiState) -> Unit = {},
    playerUiState: MainPlayerUiState,
    emailsharedPreferences: sharedPreferences,
    onEmailValueChange: (sharedPreferences) -> Unit
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
}