package com.example.githubprofileviewer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// ✅ CUSTOM LIGHT THEME (USED)
private val AppLightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = Background,
    surface = Surface,
    surfaceVariant = SurfaceVariant,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.Black
)

// ✅ OPTIONAL DARK THEME (basic)
private val AppDarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary
)

@Composable
fun GitHubProfileViewerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // ❌ TURN OFF (important)
    content: @Composable () -> Unit
) {

    val colorScheme = when {

        // ❌ REMOVE dynamic color (it overrides your theme)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme -> AppDarkColorScheme
        else -> AppLightColorScheme // ✅ NOW USING YOUR COLORS
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}