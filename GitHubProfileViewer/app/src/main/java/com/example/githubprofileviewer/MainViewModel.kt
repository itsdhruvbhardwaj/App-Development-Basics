package com.example.githubprofileviewer

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubprofileviewer.data.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.content.Context

class MainViewModel : ViewModel() {

    // This holds current UI state
    // Initially we set it to Loading (you can change later)
    var state by mutableStateOf<UiState>(UiState.Idle)
        private set // prevents UI from modifying it directly

    // This function is called when user searches for a GitHub username
    fun fetchUser(username: String) {

        // viewModelScope.launch = runs code in background (coroutines)
        // prevents blocking UI thread (VERY IMPORTANT)
        viewModelScope.launch {

            // Step 1: Show loading state
            state = UiState.Loading

            try {
                // Step 2: Call API (network request)

                // Get user profile
                val userResponse = RetrofitInstance.api.getUser(username)

                // Get user repositories
                val repoResponse = RetrofitInstance.api.getRepos(username)

                // Step 3: Check if API calls were successful
                if (userResponse.isSuccessful && repoResponse.isSuccessful) {

                    // Extract data from response body
                    val user = userResponse.body()
                    val repos = repoResponse.body()

                    // Step 4: Check if data is not null
                    if (user != null && repos != null) {

                        // Step 5: Update UI with success state
                        state = UiState.Success(user, repos)

                    } else {
                        // If response body is empty
                        state = UiState.Error("Empty response from server")
                    }

                } else {
                    // If API returns error (404, 500, etc.)
                    state = UiState.Error(
                        "API Error: ${userResponse.code()}"
                    )
                }

            } catch (e: Exception) {
                // Step 6: Handle exceptions (no internet, timeout, etc.)

                state = UiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }
    // Save multiple recent searches (max 5)
    fun saveLastSearch(context: Context, username: String) {

        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        val existing = prefs.getStringSet("recent_users", mutableSetOf()) ?: mutableSetOf()

        val updated = existing.toMutableSet()

        updated.remove(username) // remove duplicate
        updated.add(username)

        // limit to last 5
        if (updated.size > 5) {
            updated.remove(updated.first())
        }

        prefs.edit().putStringSet("recent_users", updated).apply()
    }

    // Get all recent searches
    fun getLastSearches(context: Context): List<String> {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getStringSet("recent_users", emptySet())?.toList() ?: emptyList()
    }

    // Clear all
    fun clearAllSearches(context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().remove("recent_users").apply()
    }
}



