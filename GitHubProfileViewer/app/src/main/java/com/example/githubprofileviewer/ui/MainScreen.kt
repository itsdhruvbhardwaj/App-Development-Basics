package com.example.githubprofileviewer.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.example.githubprofileviewer.MainViewModel
import com.example.githubprofileviewer.ui.components.AppHeader
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {

    var username by remember { mutableStateOf("") }
    val context = LocalContext.current

    var recentUsers by remember {
        mutableStateOf(viewModel.getLastSearches(context))
    }

    Scaffold(

        // 🔥 FULL WIDTH HEADER
        topBar = {
            AppHeader(
                title = "GitHub",
                subtitle = "Profile Viewer"
            )
        }

    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = 16.dp,   // ✅ SAME FIX
                bottom = 24.dp
            )
        ) {

            // 🔍 SEARCH CARD
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text("Search GitHub User")

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            placeholder = { Text("Enter username...") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val isEnabled = username.isNotBlank()

                        Button(
                            onClick = {
                                val cleanUsername = username.trim()

                                if (cleanUsername.isNotEmpty()) {

                                    viewModel.saveLastSearch(context, cleanUsername)
                                    recentUsers = viewModel.getLastSearches(context)

                                    navController.navigate("profile/$cleanUsername")
                                }
                            },

                            enabled = isEnabled, // ✅ controls click + ripple

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),

                            shape = RoundedCornerShape(24.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isEnabled)
                                    MaterialTheme.colorScheme.primary   // 🔥 filled when active
                                else
                                    MaterialTheme.colorScheme.surfaceVariant, // light when empty

                                contentColor = if (isEnabled)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text("Search")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 🟢 RECENT SEARCHES
            if (recentUsers.isNotEmpty()) {

                item {
                    Text("Recent Searches")
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(recentUsers) { user ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val cleanUser = user.trim()
                                    navController.navigate("profile/$cleanUser")
                                }
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(user)

                            IconButton(onClick = {

                                val updated = recentUsers.toMutableList()
                                updated.remove(user)
                                recentUsers = updated

                                val prefs = context.getSharedPreferences(
                                    "app_prefs",
                                    Context.MODE_PRIVATE
                                )
                                prefs.edit()
                                    .putStringSet("recent_users", updated.toSet())
                                    .apply()

                            }) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}