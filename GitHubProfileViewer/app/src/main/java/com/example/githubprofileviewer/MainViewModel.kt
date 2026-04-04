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
    fun fetchUser(username: String, context: Context) {

        viewModelScope.launch {

            state = UiState.Loading

            // 🔥 CHECK INTERNET FIRST
            if (!com.example.githubprofileviewer.utils.isInternetAvailable(context)) {
                state = UiState.Error("No internet connection 🌐")
                return@launch
            }

            try {

                val userResponse = RetrofitInstance.api.getUser(username)
                val repoResponse = RetrofitInstance.api.getRepos(username)

                if (userResponse.isSuccessful && repoResponse.isSuccessful) {

                    val user = userResponse.body()
                    val repos = repoResponse.body()

                    if (user != null && repos != null) {
                        state = UiState.Success(user, repos)
                    } else {
                        state = UiState.Error("Something went wrong 😕")
                    }

                } else {

                    if (userResponse.code() == 404) {
                        state = UiState.Error("User not found 😕")
                    } else {
                        state = UiState.Error("Something went wrong. Try again!")
                    }
                }

            } catch (e: Exception) {
                state = UiState.Error("Network error occurred 🌐")
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



