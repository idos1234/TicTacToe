package com.idos.tictactoe

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

//Create Singleton instance of DataStore Preference
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //Let's call the function.
        createNotificationChannel()
    }

    //Create Notification Channel.
    private fun createNotificationChannel(){
        val name = "JetpackPushNotification"
        val description ="Jetpack Push Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        //Now Create Notification Channel.
        // it take three parameters. notification id,name, and importance.
        val channel = NotificationChannel("idoChannel",name,importance)
        channel.description = description

        // Get Notification Manager.
        val notificationManager : NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Lets Create Notification channel.
        notificationManager.createNotificationChannel(channel)

    }

}