package com.depi.jobsearch.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    secondary = Purple80,
    background = DarkBackground,
    surface = SurfaceDark,
    onSurface = OnSurfaceLight
)

private val LightColorScheme = lightColorScheme(
    primary = DarkPurple, // Primary color for sidebar and profile card background
    secondary = Orange,   // Accent color for icons and buttons
    tertiary = Blue,      // Action button color (dark blue/black)

    background = LightGray, // Background for main content area
    surface = White,       // Content background
    onPrimary = White,     // Text color on primary background
    onSecondary = Black,   // Text color on secondary background
    onTertiary = White,    // Text color on tertiary background
    onBackground = Black,  // Text color on background
    onSurface = Black,     // Text color on surface

)

@Composable
fun JobSearchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}