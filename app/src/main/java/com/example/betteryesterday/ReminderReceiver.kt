package com.example.betteryesterday

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.betteryesterday.data.PrefsDataStoreManager
import com.example.betteryesterday.ui.scheduleRepeatingNotification
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            // Reschedule the alarm based on the stored settings
            rescheduleAlarms(context)
        } else {
            // Handle the alarm trigger and show the notification
            showNotification(context)
        }
    }

    private fun rescheduleAlarms(context: Context) {
        // Reschedule the same alarm
        var time = runBlocking {
            PrefsDataStoreManager.getSavedTimeFlow(context).first()
        }

        val calendar = Calendar.getInstance().apply {
            time = timeInMillis
        }
        calendar.add(Calendar.DAY_OF_YEAR, 1)

        scheduleRepeatingNotification(context, calendar)
    }

    private fun showNotification(context: Context) {
        // Trigger the notification
        NotificationHandler(context).showNotification()
    }
}