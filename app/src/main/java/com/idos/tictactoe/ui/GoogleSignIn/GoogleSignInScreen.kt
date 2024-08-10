package com.idos.tictactoe.ui.GoogleSignIn

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.network.Connection.ConnectionState
import com.example.network.Connection.connectivityState
import com.google.android.gms.base.R
import com.google.android.gms.common.api.ApiException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.WorkManager.SetNewDelay
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.ui.Screen.GameScreen

@Composable
fun GoogleSignInScreen(viewModel: GoogleSignInViewModel, navController: NavController, changeEmail: (String) -> Unit, onClick: @Composable () -> Unit) {
    val user by remember(viewModel) {
        viewModel.googleUser
    }.collectAsState()
    val signInRequestCode = 1
    val context = LocalContext.current
    var searchedPlayer by remember {
        mutableStateOf(false)
    }
    var setDelay by remember {
        mutableStateOf(false)
    }

    val authResultLauncher = rememberLauncherForActivityResult(contract = GoogleApiContract()) {
        try {
            val account = it?.getResult(ApiException::class.java)
            //if sign in failed
            if (account == null) {
                Toast.makeText(
                    context,
                    "Something went wrong...",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //getting user data
                viewModel.fetchSignInUser(email = account.email!!, name = account.displayName!!)
            }
        } catch (_: ApiException) { }
    }

    //sign in button
    ScreenView(
        onClick = {
            authResultLauncher.launch(signInRequestCode)
                  }
    )

    user?.let {
        var isPlayerExisted by remember {
            mutableStateOf(false)
        }

        //get database
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Players")
        //get Players collection from database
        databaseReference.addValueEventListener(object : ValueEventListener {
            //on success
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                 try {
                     list.find {
                         it.getValue(MainPlayerUiState::class.java)!!.email == user?.email!!.toSHA256()
                     }?.getValue(MainPlayerUiState::class.java)!!
                     isPlayerExisted = true
                     searchedPlayer = true
                } catch (_: Exception) {
                     isPlayerExisted = false
                     searchedPlayer = true
                }

            }
            override fun onCancelled(error: DatabaseError) {}
        })

        //if user is existed
        if (isPlayerExisted) {
            //log in
            changeEmail(user?.email!!.toSHA256())
            if(!setDelay) {
                SetNewDelay(
                    hour = 11,
                    min = 0,
                    context = context
                )
                setDelay = true
            }
            onClick()
        } else if (!isPlayerExisted && searchedPlayer) {
            //sign up
            viewModel.updateEmail(viewModel.emailState.copy(email2 = user!!.email))
            navController.navigate(GameScreen.NewName.title)
        }
    }
}


@Composable
fun SignInBackGround() {

    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(listOf(colors.background, colors.primary))
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = AbsoluteAlignment.TopLeft
        ) {
            Text(
                text = "X",
                fontSize = screenHeight.value.sp * 0.3,
                color = colors.onBackground.copy(0.3f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.rotate(35f)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "O",
                fontSize = screenHeight.value.sp * 0.3,
                color = colors.onBackground.copy(0.3f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.rotate(35f)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = AbsoluteAlignment.BottomRight
        ) {
            Text(
                text = "X",
                fontSize = screenHeight.value.sp * 0.3,
                color = colors.onBackground.copy(0.3f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.rotate(35f)
            )
        }
    }
}

@Composable
private fun GoogleSignInButton(modifier: Modifier, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val connection by connectivityState()

    OutlinedButton(
        onClick = onClick,
        enabled = connection == ConnectionState.Available,
        colors = ButtonDefaults.buttonColors(colors.primary),
        border = BorderStroke(1.dp, colors.onPrimary),
        modifier = modifier,
        shape = CircleShape
    ) {
        Row(
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.googleg_standard_color_18),
                contentDescription = "google sign in icon"
            )
            Text(
                text = "Sign up with Google",
                fontSize = screenHeight.value.sp * 0.02,
                color = colors.onBackground
            )
        }
    }
}

@Composable
fun ScreenView(
    onClick: () -> Unit
) {

    val colors = MaterialTheme.colorScheme
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(modifier = Modifier.fillMaxSize()) {
        SignInBackGround()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            Row(
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Welcome",
                    fontSize = screenHeight.value.sp * 0.07,
                    color = colors.onBackground
                )
                Text(
                    text = "!",
                    fontSize = screenHeight.value.sp * 0.07,
                    color = colors.onBackground
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.4f))
            GoogleSignInButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(0.7f))
        }
    }
}