package com.idos.tictactoe.data

import com.idos.tictactoe.R.drawable.o_1
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
import com.idos.tictactoe.R.drawable.x_1
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
import com.idos.tictactoe.R.drawable.xo_1
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

data class Image(
    val x: Int,
    val o: Int,
    val image: Int,
    val name: String
)

val images = listOf(
    Image(x_1, o_1, xo_1, "Classic"),
    Image(x_2, o_2, xo_2, "Candy"),
    Image(x_3, o_3, xo_3, "Sport"),
    Image(x_4, o_4, xo_4, "Food"),
    Image(x_5, o_5, xo_5, "Nature"),
    Image(x_6, o_6, xo_6, "Rainbow"),
    Image(x_7, o_7, xo_7, "Knight"),
    Image(x_8, o_8, xo_8, "Emoji"),
    Image(x_9, o_9, xo_9, "Neon"),
    Image(x_10, o_10, xo_10, "Hacking"),
    Image(x_11, o_11, xo_11, "Pirate"),
    Image(x_12, o_12, xo_12, "Music"),
    Image(x_13, o_13, xo_13, "symbols"),
    Image(x_14, o_14, xo_14, "Animals"),
    Image(x_15, o_15, xo_15, "Space")
)
