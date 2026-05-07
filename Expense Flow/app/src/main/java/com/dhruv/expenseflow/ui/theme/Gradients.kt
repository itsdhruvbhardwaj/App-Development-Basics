package com.dhruv.expenseflow.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// The 3-point background gradient from the image
val MainBackgroundGradient = Brush.linearGradient(
    colors = listOf(
        LightBlue,
        SoftPink,
        SoftLavender
    ),
    start = Offset(Float.POSITIVE_INFINITY, 0f), // Top Right
    end = Offset(0f, Float.POSITIVE_INFINITY)    // Bottom Left
)

// The vibrant purple gradient for the top card
val PrimaryPurpleGradient = Brush.verticalGradient(
    colors = listOf(
        MainDeepPurple,
        PrimaryPurple
    )
)
