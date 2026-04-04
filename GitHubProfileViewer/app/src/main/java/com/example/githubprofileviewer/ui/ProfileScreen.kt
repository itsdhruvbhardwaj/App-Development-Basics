package com.example.githubprofileviewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil.compose.AsyncImage

import com.example.githubprofileviewer.MainViewModel
import com.example.githubprofileviewer.UiState
import com.example.githubprofileviewer.ui.components.AppHeader
import com.example.githubprofileviewer.ui.components.SkeletonList


@Composable
fun ProfileScreen(
    username: String,
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(username) {

        if (username.isNotBlank()) {
            viewModel.fetchUser(username, context)
        }
    }

    val state = viewModel.state

    Scaffold(

        // ✅ HEADER (WILL NOW BE VISIBLE)
        topBar = {
            AppHeader(
                title = username,
                showBack = true,
                onBackClick = { navController.popBackStack() }
            )
        }

    ) { padding ->

        when (state) {

            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    SkeletonList()
                }
            }

            is UiState.Success -> {

                val user = state.user
                val repos = state.repos

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp),

                    // 🔥 ADD THIS (THIS IS THE FIX)
                    contentPadding = PaddingValues(
                        top = 16.dp,      // ✅ adds gap from header
                        bottom = 32.dp
                    )
                ) {

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                AsyncImage(
                                    model = user.avatar_url,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(user.login)
                                    Text("Followers: ${user.followers}")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Repositories")

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(repos) { repo ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Text(repo.name)

                                Spacer(modifier = Modifier.height(4.dp))

                                Text("⭐ ${repo.stargazers_count}")
                            }
                        }
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.message}")
                }
            }

            else -> {}
        }
    }
}