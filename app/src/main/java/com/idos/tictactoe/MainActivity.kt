package com.idos.tictactoe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.idos.tictactoe.ui.screen.TicTacToeApp
import com.idos.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    private var updateType = AppUpdateType.IMMEDIATE
    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        actionBar?.hide()

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkForUpdates()

        setContent {
            TicTacToeTheme {
                TicTacToeApp()
            }
        }
    }

    @SuppressLint("UnsafeIntentLaunch")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            // handle callback
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                    applicationContext,
                    "You can't continue without updating",
                    Toast.LENGTH_LONG
                ).show()
                // If the update is canceled,
                // you can request to start the update again.
                checkForUpdates()
            } else if(requestCode == RESULT_OK) {
                Toast.makeText(
                    applicationContext,
                    "Restarting...",
                    Toast.LENGTH_LONG
                ).show()

                finish()
                startActivity(intent)
            }
        }
    }

    private fun checkForUpdates() {
        // Checks that the platform will allow the specified type of update.
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateSupported = when(updateType) {
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                else -> false
            }

            if(isUpdateAvailable && isUpdateSupported) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateType,
                    this,
                    1
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (updateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        updateType,
                        this,
                        1
                    )
                }
            }
        }
    }
}