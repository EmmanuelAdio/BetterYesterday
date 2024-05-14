package com.example.betteryesterday

import SettingsViewModel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.betteryesterday.data.PrefsDataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class NotificationHandler(
    private val context: Context
) {
    private val notificationManager = context.getSystemService((NotificationManager::class.java)) as NotificationManager

    fun showNotification(){
        // Use runBlocking to synchronously access DataStore
        val showNotification = runBlocking {
            PrefsDataStoreManager.getNotificationToggle(context).first()
        }

        //only show the notification if the user wants has enables notification viewing
        if (showNotification) {
            Log.d("NOTIFICATION 2", "The Notification Is being Received")
            val activityIntent = Intent(context, MainActivity::class.java)
            val pendingActivityIntent = PendingIntent.getActivity(
                context,
                1,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            )

            // Expandable style for more text
            val bigTextStyle = NotificationCompat.BigTextStyle()
                .bigText("Remember to go onto the BetterYesterday app and tick off your Milestones and Goals!!")
                .setBigContentTitle("BetterYesterday Reminder")
                .setSummaryText("BetterYesterday Reminder Summary")

            // Building the notification
            val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("BetterYesterday Reminder")
                .setContentText("Remember to go onto the BetterYesterday app and tick off your Milestones and Goals!!")
                .setContentIntent(pendingActivityIntent)
                .setStyle(bigTextStyle)  // Set the expandable style
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // Set the priority to high
                .build()

            notificationManager.notify(1, notification)
        }
    }

    companion object{
        const val REMINDER_CHANNEL_ID = "reminder_channel"
    }
}