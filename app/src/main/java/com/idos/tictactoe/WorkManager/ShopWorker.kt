package com.idos.tictactoe.WorkManager

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.idos.tictactoe.ui.screens.Shop.GetDealsFromSharedPreferences
import com.idos.tictactoe.ui.screens.Shop.SetNewDeals
import com.idos.tictactoe.ui.screens.Shop.fifthDeal
import com.idos.tictactoe.ui.screens.Shop.firstDeal
import com.idos.tictactoe.ui.screens.Shop.fourthDeal
import com.idos.tictactoe.ui.screens.Shop.secondDeal
import com.idos.tictactoe.ui.screens.Shop.sixthDeal
import com.idos.tictactoe.ui.screens.Shop.text
import com.idos.tictactoe.ui.screens.Shop.thirdDeal
import java.util.concurrent.TimeUnit

private val times = mutableIntStateOf(1)

class ShopWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    val c = context

    override fun doWork(): Result {
        if(times.intValue == 1) {
            SetNewDeals(c)
            GetDealsFromSharedPreferences(c)
            text.value = "tomorrow"

            Log.i("Deals", "Deal 1: ${firstDeal.value.tag}")
            Log.i("Deals", "Deal 2: ${secondDeal.value.tag}")
            Log.i("Deals", "Deal 3: ${thirdDeal.value.tag}")
            Log.i("Deals", "Deal 4: ${fourthDeal.value.tag}")
            Log.i("Deals", "Deal 5: ${fifthDeal.value.tag}")
            Log.i("Deals", "Deal 6: ${sixthDeal.value.tag}")

            SetNewDelay(
                hour = 11,
                min = 0,
                context = c
            )
            ++times.intValue
        }

        return Result.success()
    }
}

fun SetNewDelay(hour: Int, min: Int, context: Context) {
    val currentDate = Calendar.getInstance()
    val dueDate = Calendar.getInstance()
    dueDate.set(Calendar.HOUR_OF_DAY, hour)
    dueDate.set(Calendar.MINUTE, min)
    dueDate.set(Calendar.SECOND, 0)
    if (dueDate.before(currentDate)) {
        dueDate.add(Calendar.HOUR_OF_DAY, 24)
    }

    val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
    val dailyWorkRequest = OneTimeWorkRequestBuilder<ShopWorker>()
        .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
        .build()
    WorkManager.getInstance(context).enqueue(dailyWorkRequest)
}