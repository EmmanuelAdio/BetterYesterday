package com.example.betteryesterday.ui

import SettingsViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.betteryesterday.MainActivity
import com.example.betteryesterday.NotificationHandler
import com.example.betteryesterday.ReminderReceiver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale



@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Observe showSettings state from ViewModel
    val showSettings by settingsViewModel.showSettings.collectAsState(initial = false)
    val savedTime by settingsViewModel.savedTime.collectAsState(initial = System.currentTimeMillis())
    val darkMode by settingsViewModel.darkMode.collectAsState(initial = false)

    // Permission Handling
    val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val showPermissionExplanationDialog = remember { mutableStateOf(false) }

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val selectedTime = Calendar.getInstance().apply {
        timeInMillis = savedTime
    }
    var timeText = timeFormat.format(selectedTime.time)
    val showTimePicker = remember { mutableStateOf(false) }

    //make sure when the settings page if they do not have permission for notification they cannot access this setting
    if (!postNotificationPermission.status.isGranted) {
        scope.launch {
            settingsViewModel.saveNotificationToggle(false)
        }
    }

    // Event Handlers and UI Logic
    if (showTimePicker.value) {
        TimePickerDialog(context, { _, hour, minute ->
            val newTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }
            timeText = timeFormat.format(newTime.time)
            showTimePicker.value = false

            //set up what happens when the user changes the time thy want to display the reminder.
            scope.launch {
                settingsViewModel.saveTime(newTime.timeInMillis)
            }

            Toast
                .makeText(context, "Reminder set for $timeText", Toast.LENGTH_SHORT)
                .show()

            scheduleRepeatingNotification(context, newTime)

        }, selectedTime.get(Calendar.HOUR_OF_DAY), selectedTime.get(Calendar.MINUTE), true)
            .apply {
                setOnCancelListener {
                    showTimePicker.value = false // Set to false when dialog is canceled
                }
            }.show()
    }

    // Permission Dialog
    if (showPermissionExplanationDialog.value) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Permission Required") },
            text = { Text("This app requires notification permission to function properly.") },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionExplanationDialog.value = false
                        postNotificationPermission.launchPermissionRequest()
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                Button(
                    onClick = { showPermissionExplanationDialog.value = false }
                ) { Text("Cancel") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                    Text("Enable Notifications: ")
                    Switch(
                        checked = showSettings,
                        onCheckedChange = { isChecked ->
                            scope.launch {
                                settingsViewModel.saveNotificationToggle(isChecked)
                            }
                            if (isChecked) {
                                if (postNotificationPermission.status.isGranted) {
                                    scope.launch {
                                        settingsViewModel.saveNotificationToggle(true)
                                    }
                                } else {
                                    scope.launch {
                                        settingsViewModel.saveNotificationToggle(false)
                                    }
                                    showPermissionExplanationDialog.value = true
                                }
                            } else {
                                scope.launch {
                                    settingsViewModel.saveNotificationToggle(false)
                                }
                            }
                        }
                    )
                }
            }

            item {
                if (showSettings) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Daily reminder set for",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Button(
                            onClick = {
                                showTimePicker.value = true

                                scheduleRepeatingNotification(context, selectedTime)
                            },
                            modifier = Modifier.padding(start = 8.dp),
                        ) {
                            // Button text to show selected time
                            Text(
                                text = timeText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {

                    Text("Enable Dark Mode: ")
                    Switch(
                        checked = darkMode,
                        onCheckedChange = { isChecked ->
                            scope.launch {
                                settingsViewModel.saveDarkModeToggle(isChecked)
                            }
                        }
                    )
                }
            }
        }
    }
}

@SuppressLint("ScheduleExactAlarm")
fun scheduleRepeatingNotification(context: Context, calendar: Calendar) {
    Log.v("NOTIFICATION 5", "The Notification Is being Received")
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 3, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    // Format the calendar time for logging
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val calendarTime = formatter.format(calendar.time)

    // Check if the scheduled time has already passed for today; if so, set for the next day
    if (calendar.timeInMillis <= System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    // Log the exact time we are setting the alarm for
    Log.d("AlarmSetup", "Scheduling alarm at: $calendarTime")

    // Use setExactAndAllowWhileIdle for precise scheduling
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    } else {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}

