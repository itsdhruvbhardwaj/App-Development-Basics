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
import androidx.compose.runtime.*
import kotlin.collections.plus

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
                val repoResponse = try {
                    RetrofitInstance.api.getRepos(username)
                } catch (e: Exception) {
                    null
                }

                if (userResponse.isSuccessful) {

                    val user = userResponse.body()

                    val repos = if (repoResponse != null && repoResponse.isSuccessful) {
                        repoResponse.body() ?: emptyList()
                    } else {
                        emptyList()
                    }

                    if (user != null) {

                        val sortedRepos = repos.sortedByDescending { it.updatedAt }

                        state = UiState.Success(user, sortedRepos)

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

        val existing = prefs.getStringSet("recent_users", emptySet()) ?: emptySet()

        // Convert to list to maintain order
        val updatedList = mutableListOf<String>()

        // Add new username FIRST
        updatedList.add(username)

        // Add old ones (excluding duplicate)
        existing.forEach {
            if (it != username) {
                updatedList.add(it)
            }
        }

        // Limit to 5
        val finalList = updatedList.take(5)

        prefs.edit().putStringSet("recent_users", finalList.toSet()).apply()
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

    //Followrs Screen Logic
    var followers by mutableStateOf<List<User>>(emptyList())
        private set

    var isFollowersLoading by mutableStateOf(false)
        private set

    fun fetchFollowers(username: String) {
        viewModelScope.launch {

            isFollowersLoading = true

            try {
                val response = RetrofitInstance.api.getFollowers(username)

                if (response.isSuccessful) {
                    followers = response.body() ?: emptyList()
                } else {
                    followers = emptyList()
                }

            } catch (e: Exception) {
                followers = emptyList()
            }

            isFollowersLoading = false
        }
    }

//    var followerDetails by mutableStateOf<Map<String, User>>(emptyMap())
//        private set
//
//    fun fetchFollowerDetails(username: String) {
//
//        // ✅ Prevent duplicate API calls
//        if (followerDetails.containsKey(username)) return
//
//        viewModelScope.launch {
//            try {
//                val response = RetrofitInstance.api.getUser(username)
//
//                if (response.isSuccessful) {
//                    response.body()?.let { user ->
//                        followerDetails = followerDetails + (username to user)
//                    }
//                }
//
//            } catch (e: Exception) {
//                // ignore
//            }
//        }
//    }

    //Following Screen Logic
    var following by mutableStateOf<List<User>>(emptyList())
        private set

    var isFollowingLoading by mutableStateOf(false)
        private set

    fun fetchFollowing(username: String) {
        viewModelScope.launch {

            isFollowingLoading = true

            try {
                val response = RetrofitInstance.api.getFollowing(username)

                if (response.isSuccessful) {
                    following = response.body() ?: emptyList()
                } else {
                    following = emptyList()
                }

            } catch (e: Exception) {
                following = emptyList()
            }

            isFollowingLoading = false
        }
    }

//    var followingDetails by mutableStateOf<Map<String, User>>(emptyMap())
//        private set
//
//    fun fetchFollowingDetails(username: String) {
//
//        // ✅ Prevent duplicate API calls
//        if (followingDetails.containsKey(username)) return
//
//        viewModelScope.launch {
//            try {
//                val response = RetrofitInstance.api.getUser(username)
//
//                if (response.isSuccessful) {
//                    response.body()?.let { user ->
//                        followingDetails = followingDetails + (username to user)
//                    }
//                }
//
//            } catch (e: Exception) {
//                // ignore
//            }
//        }
//    }

    // 🔥 Shared user details cache (followers + following)
    var userDetails by mutableStateOf<Map<String, User>>(emptyMap())
        private set

    fun fetchUserDetails(username: String) {

        // Prevent duplicate API calls
        if (userDetails.containsKey(username)) return

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUser(username)

                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        userDetails = userDetails + (username to user)
                    }
                }

            } catch (e: Exception) {
                // ignore
            }
        }
    }
}



