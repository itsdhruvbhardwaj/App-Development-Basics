package com.example.githubprofileviewer.utils

import androidx.compose.ui.graphics.Color

object LanguageColor {

    fun getColor(language: String?): Color {
        return when (language?.lowercase()) {
            "kotlin" -> Color(0xFF7F52FF)
            "java" -> Color(0xFFB07219)
            "javascript" -> Color(0xFFF1E05A)
            "typescript" -> Color(0xFF3178C6)
            "python" -> Color(0xFF3572A5)
            "go" -> Color(0xFF00ADD8)
            "c++" -> Color(0xFFF34B7D)
            "c" -> Color(0xFF555555)
            "swift" -> Color(0xFFFFAC45)
            else -> Color.Gray
        }
    }
}