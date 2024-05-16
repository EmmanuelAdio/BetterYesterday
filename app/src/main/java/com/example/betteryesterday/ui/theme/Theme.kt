package com.example.betteryesterday.ui.theme

import SettingsViewModel
import android.app.Activity
/*import androidx.compose.foundation._isSystemInDarkTheme*/
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    background = Black,
    primaryContainer = DarkPurple,
    surface = DarkPurple,
    tertiary = Pink,
    onSurface = Purple80
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleGrey40,
    secondary = Purple80,
    background = White,
    primaryContainer = LightPurple,
    surface = LightPurple,
    tertiary = Pink,
    onSurface = PurpleGrey40
)


//these two functions mimic the systems is system in dark mode functions to allow the user to switch between dark mode and light mode
@Composable
fun isDarkModeONTheme() = DarkmodeToggleTheme()

@Composable
fun DarkmodeToggleTheme(): Boolean {
    val settingsViewModel: SettingsViewModel = viewModel()
    val darkMode by settingsViewModel.darkMode.collectAsState(initial = false)
    return darkMode
}


@Composable
fun BetterYesterdayTheme(
    darkTheme: Boolean = isDarkModeONTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val settingsViewModel: SettingsViewModel = viewModel()
    val colorScheme = when {


        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}