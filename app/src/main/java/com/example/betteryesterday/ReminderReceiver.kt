package com.example.betteryesterday

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.betteryesterday.ui.scheduleRepeatingNotification
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Handle the alarm trigger
        showNotification(context)
    }

    private fun rescheduleAlarms(context: Context) {
        // Reschedule the same alarm
        val calendar = Calendar.getInstance().apply {
            // Example: Set for 10 AM
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        scheduleRepeatingNotification(context, calendar)
    }

    private fun showNotification(context: Context) {
        // Trigger the notification
        NotificationHandler(context).showNotification()
    }
}