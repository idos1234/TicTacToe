package com.idos.tictactoe.fireBaseMessaging

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.idos.tictactoe.MainActivity
import com.idos.tictactoe.R
import com.idos.tictactoe.ui.Screen.getSecuredSharedPreferences
import com.idos.tictactoe.ui.Screen.sharedPreferences

data class notificationData(
    var days: Int = 0,
    var DaysAfterNotification: Int = 0
)

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FireBaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val sharedPreferencesUiState by mutableStateOf(sharedPreferences())
        var NotificationData by mutableStateOf(notificationData())

        val sharedPreferences = getSecuredSharedPreferences(this, "myPref1")

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        //get Players collection from database
        db.collection("NotificationData").get()
            //on success
            .addOnSuccessListener { queryDocumentSnapshots ->
                //check if collection is empty
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    for (d in list) {
                        //add every player to player list
                        val p: notificationData? = d.toObject(notificationData::class.java)
                        NotificationData = p!!

                    }
                    //sort players list by players' score
                }
            }
            //on failure
            .addOnFailureListener {
            }

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
            if (!sharedPreferencesUiState.messageSent){
                if (sharedPreferencesUiState.lastTimeSeen <= (System.currentTimeMillis()/1000) - NotificationData.days*3600) {
                    showNotificationOnStatusBar(message.data["title"], message.data["body"])
                    sharedPreferencesUiState.messageSent = true
                    sharedPreferencesUiState.messagingSendingTime = (System.currentTimeMillis()/1000)
                    sharedPreferences.edit().putBoolean("messageSent", sharedPreferencesUiState.messageSent).apply()
                    sharedPreferences.edit().putLong("messagingSendingTime", sharedPreferencesUiState.messagingSendingTime).apply()
                }
            } else {
                if (sharedPreferencesUiState.messagingSendingTime <= (System.currentTimeMillis()/1000) - NotificationData.DaysAfterNotification*3600) {
                    showNotificationOnStatusBar(message.data["title"], message.data["body"])
                    sharedPreferencesUiState.messagingSendingTime = (System.currentTimeMillis()/1000)
                    sharedPreferences.edit().putLong("messagingSendingTime", sharedPreferencesUiState.messagingSendingTime).apply()
                }
            }
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
        val requestCode = System.currentTimeMillis().toInt()
        val pendingIntent : PendingIntent
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
            .setSmallIcon(R.mipmap.ic_launcher_round)


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
}