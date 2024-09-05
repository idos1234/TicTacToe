package com.idos.tictactoe.data


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idos.tictactoe.R.drawable.o_10
import com.idos.tictactoe.R.drawable.o_11
import com.idos.tictactoe.R.drawable.o_12
import com.idos.tictactoe.R.drawable.o_13
import com.idos.tictactoe.R.drawable.o_14
import com.idos.tictactoe.R.drawable.o_15
import com.idos.tictactoe.R.drawable.o_2
import com.idos.tictactoe.R.drawable.o_3
import com.idos.tictactoe.R.drawable.o_4
import com.idos.tictactoe.R.drawable.o_5
import com.idos.tictactoe.R.drawable.o_6
import com.idos.tictactoe.R.drawable.o_7
import com.idos.tictactoe.R.drawable.o_8
import com.idos.tictactoe.R.drawable.o_9
import com.idos.tictactoe.R.drawable.x_10
import com.idos.tictactoe.R.drawable.x_11
import com.idos.tictactoe.R.drawable.x_12
import com.idos.tictactoe.R.drawable.x_13
import com.idos.tictactoe.R.drawable.x_14
import com.idos.tictactoe.R.drawable.x_15
import com.idos.tictactoe.R.drawable.x_2
import com.idos.tictactoe.R.drawable.x_3
import com.idos.tictactoe.R.drawable.x_4
import com.idos.tictactoe.R.drawable.x_5
import com.idos.tictactoe.R.drawable.x_6
import com.idos.tictactoe.R.drawable.x_7
import com.idos.tictactoe.R.drawable.x_8
import com.idos.tictactoe.R.drawable.x_9
import com.idos.tictactoe.R.drawable.xo_10
import com.idos.tictactoe.R.drawable.xo_11
import com.idos.tictactoe.R.drawable.xo_12
import com.idos.tictactoe.R.drawable.xo_13
import com.idos.tictactoe.R.drawable.xo_14
import com.idos.tictactoe.R.drawable.xo_15
import com.idos.tictactoe.R.drawable.xo_2
import com.idos.tictactoe.R.drawable.xo_3
import com.idos.tictactoe.R.drawable.xo_4
import com.idos.tictactoe.R.drawable.xo_5
import com.idos.tictactoe.R.drawable.xo_6
import com.idos.tictactoe.R.drawable.xo_7
import com.idos.tictactoe.R.drawable.xo_8
import com.idos.tictactoe.R.drawable.xo_9

data class Skin(
    val image: Int,
    val name: String,
    val price: Int,
    val tag: String,
    val onClick: @Composable ((String) -> Unit)?
)

val Xs = listOf(
    Skin(x_2, "Candy", 250, "x_2") { BuyX(playerName = it, x = "x_2", price = 250) },
    Skin(x_3, "Sport", 400, "x_3") { BuyX(playerName = it, x = "x_3", price = 400) },
    Skin(x_4, "Food", 600, "x_4") { BuyX(playerName = it, x = "x_4", price = 600) },
    Skin(x_5, "Nature", 800, "x_5") { BuyX(playerName = it, x = "x_5", price = 800) },
    Skin(x_6, "Rainbow", 1000, "x_6") { BuyX(playerName = it, x = "x_6", price = 1000) },
    Skin(x_7, "Knight", 1300, "x_7") { BuyX(playerName = it, x = "x_7", price = 1300) },
    Skin(x_8, "Emoji", 1600, "x_8") { BuyX(playerName = it, x = "x_8", price = 1600) },
    Skin(x_9, "Neon", 1900, "x_9") { BuyX(playerName = it, x = "x_9", price = 1900) },
    Skin(x_10, "Hacking", 2000, "x_10") { BuyX(playerName = it, x = "x_10", price = 2000) },
    Skin(x_11, "Pirate", 2350, "x_11") { BuyX(playerName = it, x = "x_11", price = 2350) },
    Skin(x_12, "Music", 2500, "x_12") { BuyX(playerName = it, x = "x_12", price = 2500) },
    Skin(x_13, "symbols", 2650, "x_13") { BuyX(playerName = it, x = "x_13", price = 2650) },
    Skin(x_14, "Animals", 2700, "x_14") { BuyX(playerName = it, x = "x_14", price = 2800) },
    Skin(x_15, "Space", 7000, "x_15") { BuyX(playerName = it, x = "x_15", price = 7000) }
)

val Os = listOf(
    Skin(o_2, "Candy", 250, "o_2") { BuyO(playerName = it, o = "o_2", price = 250) },
    Skin(o_3, "Sport", 400, "o_3") { BuyO(playerName = it, o = "o_3", price = 400) },
    Skin(o_4, "Food", 600, "o_4") { BuyO(playerName = it, o = "o_4", price = 600) },
    Skin(o_5, "Nature", 800, "o_5") { BuyO(playerName = it, o = "o_5", price = 800) },
    Skin(o_6, "Rainbow", 1000, "o_6") { BuyO(playerName = it, o = "o_6", price = 1000) },
    Skin(o_7, "Knight", 1300, "o_7") { BuyO(playerName = it, o = "o_7", price = 1300) },
    Skin(o_8, "Emoji", 1600, "o_8") { BuyO(playerName = it, o = "o_8", price = 1600) },
    Skin(o_9, "Neon", 1900, "o_9") { BuyO(playerName = it, o = "o_9", price = 1900) },
    Skin(o_10, "Hacking", 2000, "o_10") { BuyO(playerName = it, o = "o_10", price = 2000) },
    Skin(o_11, "Pirate", 2350, "o_11") { BuyO(playerName = it, o = "o_11", price = 2350) },
    Skin(o_12, "Music", 2500, "o_12") { BuyO(playerName = it, o = "o_12", price = 2500) },
    Skin(o_13, "symbols", 2650, "o_13") { BuyO(playerName = it, o = "o_13", price = 2650) },
    Skin(o_14, "Animals", 2800, "o_14") { BuyO(playerName = it, o = "o_14", price = 2800) },
    Skin(o_15, "Space", 7000, "o_15") { BuyO(playerName = it, o = "o_15", price = 7000) }
)

val Images = listOf(
    Skin(xo_2, "Candy", 250, "xo_2") { BuyImage(playerName = it, image = "xo_2", price = 250) },
    Skin(xo_3, "Sport", 400, "xo_3") { BuyImage(playerName = it, image = "xo_3", price = 400) },
    Skin(xo_4, "Food", 600, "xo_4") { BuyImage(playerName = it, image = "xo_3", price = 600) },
    Skin(xo_5, "Nature", 800, "xo_5") { BuyImage(playerName = it, image = "xo_5", price = 800) },
    Skin(xo_6, "Rainbow", 1000, "xo_6") { BuyImage(playerName = it, image = "xo_6", price = 1000) },
    Skin(xo_7, "Knight", 1300, "xo_7") { BuyImage(playerName = it, image = "xo_7", price = 1300) },
    Skin(xo_8, "Emoji", 1600, "xo_8") { BuyImage(playerName = it, image = "xo_8", price = 1600) },
    Skin(xo_9, "Neon", 1900, "xo_9") { BuyImage(playerName = it, image = "xo_9", price = 1900) },
    Skin(xo_10, "Hacking", 2000, "xo_10") { BuyImage(playerName = it, image = "xo_10", price = 2000) },
    Skin(xo_11, "Pirate", 2350, "xo_11") { BuyImage(playerName = it, image = "xo_11", price = 2350) },
    Skin(xo_12, "Music", 2500, "xo_12") { BuyImage(playerName = it, image = "xo_12", price = 2500) },
    Skin(xo_13, "symbols", 2650, "xo_13") { BuyImage(playerName = it, image = "xo_13", price = 2650) },
    Skin(xo_14, "Animals", 2800, "xo_14") { BuyImage(playerName = it, image = "xo_14", price = 2800) },
    Skin(xo_15, "Space", 7000, "xo_15") { BuyImage(playerName = it, image = "xo_15", price = 7000) }
)


@Composable
fun BuyX(playerName: String, x: String, price: Int) {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var buy by remember {
        mutableStateOf(false)
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Players")
    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            if(!buy) {
                val list = snapshot.children
                try {
                    player = list.find {
                        it.getValue(MainPlayerUiState::class.java)!!.email == playerName
                    }?.getValue(MainPlayerUiState::class.java)!!

                    val updatedPlayer = player.copy(
                        unlockedX = player.unlockedX.plus(x),
                        lockedX = player.lockedX.minus(x),
                        coins = player.coins - price
                    )

                    databaseReference.child(player.key).setValue(updatedPlayer)
                } catch (_: Exception) { }
                buy = true
            }
        }

        override fun onCancelled(error: DatabaseError) {}
    })
}

@Composable
fun BuyO(playerName: String, o: String, price: Int) {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var buy by remember {
        mutableStateOf(false)
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Players")
    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            if(!buy) {
                val list = snapshot.children
                try {
                    player = list.find {
                        it.getValue(MainPlayerUiState::class.java)!!.email == playerName
                    }?.getValue(MainPlayerUiState::class.java)!!

                    val updatedPlayer = player.copy(
                        unlockedO = player.unlockedO.plus(o),
                        lockedO = player.lockedO.minus(o),
                        coins = player.coins - price
                    )

                    databaseReference.child(player.key).setValue(updatedPlayer)
                } catch (_: Exception) { }
                buy = true
            }
        }

        override fun onCancelled(error: DatabaseError) {}
    })
}

@Composable
fun BuyImage(playerName: String, image: String, price: Int) {
    var player by remember {
        mutableStateOf(MainPlayerUiState())
    }
    var buy by remember {
        mutableStateOf(false)
    }

    //get database
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Players")
    //get Players collection from database
    databaseReference.addValueEventListener(object : ValueEventListener {
        //on success
        override fun onDataChange(snapshot: DataSnapshot) {
            if(!buy) {
                val list = snapshot.children
                try {
                    player = list.find {
                        it.getValue(MainPlayerUiState::class.java)!!.email == playerName
                    }?.getValue(MainPlayerUiState::class.java)!!

                    val updatedPlayer = player.copy(
                        unlockedImages = player.unlockedImages.plus(image),
                        lockedImages = player.lockedImages.minus(image),
                        coins = player.coins - price
                    )

                    databaseReference.child(player.key).setValue(updatedPlayer)
                } catch (_: Exception) { }
                buy = true
            }
        }

        override fun onCancelled(error: DatabaseError) {}
    })
}