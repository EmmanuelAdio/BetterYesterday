package com.example.betteryesterday.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings_prefs")

object PrefsDataStoreManager {
    private val SELECTED_TIME_KEY = longPreferencesKey("selected_time")
    private val NOTIFICATION_TOGGLE = booleanPreferencesKey("show_notifications")

    // Function to get the saved time flow
    fun getSavedTimeFlow(context: Context): Flow<Long> {
        return context.dataStore.data
            .map { preferences ->
                // Return the saved time or current time if none saved
                preferences[SELECTED_TIME_KEY] ?: System.currentTimeMillis()
            }
    }

    // Suspend function to save the selected time
    suspend fun saveSelectedTime(context: Context, time: Long) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_TIME_KEY] = time
        }
    }

    // Function to get the notification toggle flow
    fun getNotificationToggle(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[NOTIFICATION_TOGGLE] ?: false  // Default is `true` if not set
            }
    }

    // Suspend function to save the notification toggle state
    suspend fun saveNotificationToggle(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_TOGGLE] = enabled
        }
    }
}