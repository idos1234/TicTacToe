package com.idos.tictactoe.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.idos.tictactoe.WorkManager.SetNewDelay
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.ui.GoogleSignIn.GoogleEmail
import com.idos.tictactoe.ui.GoogleSignIn.GoogleSignInViewModel
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
    onClick: () -> Unit
) {

    var done by remember {
        mutableStateOf(false)
    }
    var setDelay by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        BasicAlertDialog(onDismissRequest = {},
            content = {
                Column {
                    TextField(
                        value = emailState.name!!,
                        onValueChange = { viewModel.updateEmail(emailState.copy(name = it)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { done = true })
                    )
                    Button(onClick = {
                        done = true
                    }) {
                        Text(text = "Next")
                    }
                }
            }
        )
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

                val player = MainPlayerUiState(
                    name = emailState.name!!,
                    email = emailState.email2!!.toSHA256(),
                    score = 0,
                )

                dbPlayers.add(player)
                    //on success
                    .addOnSuccessListener {
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
                    //on failure
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Fail! Try again \n$e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
    }
}

@Composable
fun TimeUp(navController: NavController) {
    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))

    Box(modifier = Modifier
        .background(brush)
        .fillMaxSize()) {
        Dialog(onDismissRequest = { navController.navigate(GameScreen.Start.title) }) {
            Column(
                modifier = Modifier
                    .background(colors.primary)
                    .fillMaxWidth(0.85f)
                    .padding((LocalConfiguration.current.screenWidthDp * 12 / 412).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "There are no players online",
                    fontSize = 20.sp,
                    color = colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = "Please try again later",
                    fontSize = 20.sp,
                    color = colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    TextButton(
                        onClick = { navController.navigate(GameScreen.Start.title) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Back",
                            color = colors.onBackground
                        )
                    }
                    TextButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Try again",
                            color = colors.onBackground
                        )
                    }
                }
            }
        }
    }
}