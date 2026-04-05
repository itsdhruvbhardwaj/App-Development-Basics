package com.example.githubprofileviewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.githubprofileviewer.ui.components.AppHeader

@Composable
fun FollowersScreen(
    username: String,
    navController: NavController
) {
    Scaffold(
        topBar = {
            AppHeader(
                title = "Followers",
                showBack = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Followers of $username")
        }
    }
}