package com.idos.tictactoe.ui.Screen.Menu.Shop

import android.content.Context
import android.content.SharedPreferences
import com.idos.tictactoe.data.Images
import com.idos.tictactoe.data.MainPlayerUiState
import com.idos.tictactoe.data.Os
import com.idos.tictactoe.data.Xs

class ShopPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("ShopPreferences", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}

fun DealOptions(range: IntRange, player: MainPlayerUiState): MutableList<String> {
    val list = emptyList<String>().toMutableList()
    for (i in range) {
        if (player.lockedX.contains(Xs[i].tag)) {
            list.add(Xs[i].tag)
        }
    }
    for (i in range) {
        if (player.lockedO.contains(Os[i].tag)) {
            list.add(Os[i].tag)
        }
    }
    for (i in range) {
        if (player.lockedImages.contains(Images[i].tag)) {
            list.add(Images[i].tag)
        }
    }

    if(list.size == 0) {
        return listOf("").toMutableList()
    } else {
        return list
    }
}