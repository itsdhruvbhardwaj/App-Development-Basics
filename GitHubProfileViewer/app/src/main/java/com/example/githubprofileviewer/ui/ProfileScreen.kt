package com.example.githubprofileviewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext

import com.example.githubprofileviewer.MainViewModel
import com.example.githubprofileviewer.UiState
import com.example.githubprofileviewer.ui.components.SkeletonList

@Composable
fun ProfileScreen(
    username: String,
    viewModel: MainViewModel = viewModel()
) {

    // 🔥 Trigger API when screen opens
    LaunchedEffect(Unit) {
        viewModel.fetchUser(username)
    }

    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()   // ✅ FIX: adds space from top (IMPORTANT)
            .padding(16.dp)
    ) {

        when (state) {

            is UiState.Loading -> {
                SkeletonList()
            }

            is UiState.Success -> {

                // 👤 User Info
                Text(
                    text = "User: ${state.user.login}",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Followers: ${state.user.followers}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Repositories:",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 📦 Repo list
                state.repos.forEach {
                    Text("- ${it.name} ⭐ ${it.stargazers_count}")
                }
            }

            is UiState.Error -> {
                Text("Error: ${state.message}")
            }

            else -> {}
        }
    }
}