package com.hackathon.todolist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(

    // Brand Color (used for top bar, FAB, buttons)
    primary = PinkPrimary,
    secondary = PinkPrimary,
    tertiary = PinkPrimary,

    // App Backgrounds
    background = Color.White,
    surface = Color.White,

    // Text & On-color values
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun ToDoListTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
