package com.example.githubprofileviewer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.githubprofileviewer.ui.FollowersScreen
import com.example.githubprofileviewer.ui.MainScreen
import com.example.githubprofileviewer.ui.ProfileScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {

        // 🟢 Search Screen
        composable("search") {
            MainScreen(navController)
        }

        // 🟢 Profile Screen
        composable("profile/{username}") { backStackEntry ->

            val username = backStackEntry.arguments?.getString("username") ?: ""

            ProfileScreen(
                username = username ?: "",
                navController = navController // ✅ pass navController
            )

        }

        // 🟢 Followers Screen
        composable("followers/{username}") { backStackEntry ->

            val username = backStackEntry.arguments?.getString("username") ?: ""

            FollowersScreen(
                username = username,
                navController = navController
            )
        }
    }
}