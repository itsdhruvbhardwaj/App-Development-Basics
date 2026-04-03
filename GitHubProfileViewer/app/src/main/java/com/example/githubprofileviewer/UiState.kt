package com.example.githubprofileviewer

// Sealed class = used to represent different states of UI
// UI can only be in ONE of these states at a time
sealed class UiState {

    object Idle : UiState()   // 👈 NEW (nothing searched yet)


    // When API is running (show loader)
    object Loading : UiState()

    // When API call is successful
    data class Success(
        val user: User,          // user profile data
        val repos: List<Repo>    // list of repositories
    ) : UiState()

    // When something goes wrong (network error, wrong username, etc.)
    data class Error(
        val message: String      // error message to show in UI
    ) : UiState()
}