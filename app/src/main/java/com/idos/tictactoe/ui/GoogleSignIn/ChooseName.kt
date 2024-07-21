package com.idos.tictactoe.ui.GoogleSignIn

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import com.idos.tictactoe.WorkManager.SetNewDelay
import com.idos.tictactoe.data.MainPlayerUiState
import java.security.MessageDigest

fun String.toSHA256(): String {
    val HEX_CHARS = "0123456789ABCDEF"
    val digest = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return digest.joinToString(
        separator = "",
        transform = {
            String(
                charArrayOf(
                    HEX_CHARS[it.toInt() shr 4 and 0x0f],
                    HEX_CHARS[it.toInt() and 0x0f]
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ChooseName(
    viewModel: GoogleSignInViewModel,
    context: Context,
    emailState: GoogleEmail,
    changeEmail: (String) -> Unit,
    onClick: @Composable () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var done by remember {
        mutableStateOf(false)
    }
    var setDelay by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SignInBackGround()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.3f))
            Text(
                text = "Choose your name",
                fontSize = screenHeight.value.sp * 0.05,
                color = colors.onBackground
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            OutlinedTextField(
                value = emailState.name!!,
                onValueChange = { viewModel.updateEmail(emailState.copy(name = it)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { done = true }),
                textStyle = TextStyle(
                    color = colors.onPrimary,
                    fontSize = screenHeight.value.sp * 0.03
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = colors.primary.copy(alpha = 0.3f),
                    focusedContainerColor = colors.primary,
                    focusedTextColor = colors.onPrimary,
                    unfocusedTextColor = colors.onPrimary,
                    focusedBorderColor = colors.onPrimary,
                    unfocusedBorderColor = colors.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f),
                shape = CircleShape
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Button(
                onClick = {
                    done = true
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                shape = CircleShape
            ) {
                Text(
                    text = "Confirm",
                    fontSize = screenHeight.value.sp * 0.03,
                    color = colors.onBackground
                )
            }
        }
    }

    if(done) {
        //if label is empty
        if (viewModel.emailState.name!! == "") {
            Toast.makeText(
                context,
                "You have to fill the label",
                Toast.LENGTH_SHORT
            ).show()
        } else
        //if name is bigger than 8 chars
            if (emailState.name!!.length > 10) {
                Toast.makeText(
                    context,
                    "Maximum chars: 10",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //get database
                val firebaseDatabase = FirebaseDatabase.getInstance()
                val databaseReference = firebaseDatabase.getReference("Players")

                val key: String = databaseReference.push().key!!
                val player = MainPlayerUiState(
                    name = emailState.name!!,
                    email = emailState.email2!!.toSHA256(),
                    score = 0,
                    key = key
                )
                databaseReference.child(key).setValue(player)
                changeEmail(emailState.email2!!.toSHA256())
                done = false
                if(!setDelay) {
                    SetNewDelay(
                        hour = 11,
                        min = 0,
                        context = context
                    )
                    setDelay = true
                }
                onClick()
            }
    }
}