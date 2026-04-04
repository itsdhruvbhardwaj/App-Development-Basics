package com.example.githubprofileviewer.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatGithubDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = inputFormat.parse(dateString)

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = date!!

        val repoYear = dateCalendar.get(Calendar.YEAR)

        val outputFormat = if (repoYear == currentYear) {
            SimpleDateFormat("MMM d", Locale.getDefault()) // Apr 4
        } else {
            SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) // Dec 6, 2025
        }

        "Updated on ${outputFormat.format(date)}"

    } catch (e: Exception) {
        ""
    }
}