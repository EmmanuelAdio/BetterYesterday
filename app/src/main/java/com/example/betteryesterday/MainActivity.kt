package com.example.betteryesterday

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.betteryesterday.ui.BetterYesterdayApp
import com.example.betteryesterday.ui.theme.BetterYesterdayTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val service = NotificationHandler(applicationContext)

        setContent {
            BetterYesterdayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BetterYesterdayApp(service, this)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v("Activity Lifecycle Methods","onStart");
    }

    override fun onResume() {
        super.onResume()
        Log.v("Activity Lifecycle Methods","onResume");
    }

    override fun onPause() {
        super.onPause()
        Log.v("Activity Lifecycle Methods","onPause");
    }

    override fun onStop() {
        super.onStop()
        Log.v("Activity Lifecycle Methods","onStop");
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.v("Activity Lifecycle Methods","onDestroy");
    }
}