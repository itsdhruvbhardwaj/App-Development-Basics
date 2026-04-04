package com.example.githubprofileviewer.ui

import android.content.Intent
import androidx.compose.foundation.clickable
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
import android.net.Uri
import androidx.compose.ui.text.font.FontWeight
import com.example.githubprofileviewer.ui.components.ProfileStat
import com.example.githubprofileviewer.utils.formatGithubDate

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
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.Top
                                ) {
                                    AsyncImage(
                                        model = user.avatarUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        // 🔥 Real Name (PRIMARY)
                                        user.name?.let {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        // 🔹 Username (SECONDARY)
                                        Text(
                                            text = "@${user.login}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        user.bio?.let {
                                            Text(it)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ProfileStat(
                                        label = "Followers",
                                        value = user.followers,
                                        modifier = Modifier.weight(1f)
                                    )
                                    ProfileStat(
                                        label = "Following",
                                        value = user.following,
                                        modifier = Modifier.weight(1f)
                                    )
                                    ProfileStat(
                                        label = "Repos",
                                        value = user.publicRepos,
                                        modifier = Modifier.weight(1f)
                                    )
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
                                .padding(vertical = 6.dp)
                                .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(repo.url)
                                )
                                context.startActivity(intent)
                                },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        text = repo.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    Text(
                                        text = formatGithubDate(repo.updatedAt),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text("⭐ ${repo.stars}")
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