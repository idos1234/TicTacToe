package com.idos.tictactoe.workManager

import android.content.Context
import android.icu.util.Calendar
import androidx.compose.runtime.mutableIntStateOf
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.idos.tictactoe.ui.screen.menu.shop.setNewDeals
import java.util.concurrent.TimeUnit

private val times = mutableIntStateOf(1)

class ShopWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    private val c = context

    override fun doWork(): Result {
        if(times.intValue == 1) {
            setNewDeals(c)

            setNewDelay(
                hour = 11,
                min = 0,
                context = c
            )
            ++times.intValue
        }

        return Result.success()
    }
}

fun setNewDelay(hour: Int, min: Int, context: Context) {
    val currentDate = Calendar.getInstance()
    val dueDate = Calendar.getInstance()
    dueDate.set(Calendar.HOUR_OF_DAY, hour)
    dueDate.set(Calendar.MINUTE, min)
    dueDate.set(Calendar.SECOND, 0)
    dueDate.set(Calendar.MILLISECOND, 0)
    if (dueDate.before(currentDate)) {
        dueDate.add(Calendar.HOUR_OF_DAY, 24)
    }

    val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
    val dailyWorkRequest = OneTimeWorkRequestBuilder<ShopWorker>()
        .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
        .build()
    WorkManager.getInstance(context).enqueue(dailyWorkRequest)
}