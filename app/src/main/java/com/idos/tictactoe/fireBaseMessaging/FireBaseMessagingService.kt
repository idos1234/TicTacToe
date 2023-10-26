package com.idos.tictactoe.fireBaseMessaging

import android.Manifest
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.idos.tictactoe.MainActivity
import com.idos.tictactoe.R
import com.idos.tictactoe.dataStore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FireBaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        //When App in background notification is handle by system
        // and it used notification payload and used title and body

        // and app in foreground i am using data payload

        // now send only data payload on that case onMessageReceived also called in background.


        //Log incoming message
        Log.v("CloudMessage", "From ${message.from}")

        //Log Data Payload
        if (message.data.isNotEmpty()) {
            Log.v("CloudMessage", "Message Data ${message.data}")
        }

        //Check if message contains a notification payload

        message.data.let {
            Log.v("CloudMessage", "Message Data Body ${it["body"]}")
            Log.v("CloudMessage", "Message Data Title  ${it["title"]}")
            showNotificationOnStatusBar(message.data["title"], message.data["body"])

        }
    }
    
    private fun showNotificationOnStatusBar(title: String?, body: String?) {

        //Create Intent it will be launched when user tap on notification from status bar.
        val intent = Intent(this, MainActivity::class.java).apply {
            flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        intent.putExtra("title", title)
        intent.putExtra("body", body)

        // it should be unqiue when push comes.
        var requestCode = System.currentTimeMillis().toInt()
        var pendingIntent : PendingIntent
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent =
                PendingIntent.getActivity(this, requestCode,intent, FLAG_MUTABLE)
        }else{
            pendingIntent =
                PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT.toInt())
        }

        val builder = NotificationCompat.Builder(this,"Global").setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText((body)))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_game_icon)


        with(NotificationManagerCompat.from(this)){
            if (ActivityCompat.checkSelfPermission(
                    this@FireBaseMessagingService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(requestCode,builder.build())
        }


    }
    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        GlobalScope.launch {
            saveGCMToken(token)
        }
    }

    private suspend fun saveGCMToken(token: String) {
        val gcmTokenKey = stringPreferencesKey("gcm_token")
        baseContext.dataStore.edit {
            it[gcmTokenKey] = token
        }
    }

}