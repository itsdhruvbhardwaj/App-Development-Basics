package com.example.githubprofileviewer.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import com.example.githubprofileviewer.MainViewModel

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {

    var username by remember { mutableStateOf("") }

    val context = LocalContext.current

    // ✅ FIX: now list instead of single user
    var recentUsers by remember {
        mutableStateOf(viewModel.getLastSearches(context))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()   // ✅ FIX 1: adds proper top spacing
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Enter GitHub Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (username.isNotBlank()) {

                    viewModel.saveLastSearch(context, username)

                    // refresh list
                    recentUsers = viewModel.getLastSearches(context)

                    navController.navigate("profile/$username")
                }
            },
            enabled = username.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🟢 Recent Searches List
        if (recentUsers.isNotEmpty()) {

            Text("Recent Searches", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {

                items(recentUsers) { user ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = user,
                            modifier = Modifier.clickable {
                                navController.navigate("profile/$user")
                            }
                        )

                        IconButton(onClick = {

                            // remove one item manually
                            val updated = recentUsers.toMutableList()
                            updated.remove(user)

                            recentUsers = updated

                            val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                            prefs.edit().putStringSet("recent_users", updated.toSet()).apply()

                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}