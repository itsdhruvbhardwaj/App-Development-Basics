package com.example.githubprofileviewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.githubprofileviewer.ui.components.AppHeader
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubprofileviewer.MainViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.unit.dp
import com.example.githubprofileviewer.ui.components.FollowerSkeletonItem


import com.example.githubprofileviewer.ui.components.FollowerItem

@Composable
fun FollowingScreen(
    username: String,
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    LaunchedEffect(username) {
        viewModel.fetchFollowing(username)
    }

    val following = viewModel.following
    val isLoading = viewModel.isFollowingLoading

    Scaffold(
        topBar = {
            AppHeader(
                title = "Following",
                showBack = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->

        if (isLoading) {
            Card(
                modifier = Modifier
                    .padding(padding)
                    .wrapContentHeight()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {

                    items(6) { // show 6 skeleton items
                        FollowerSkeletonItem()
                    }
                }
            }
        } else if (following.isEmpty()) {

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No following found")
            }

        } else {
            Card(
                modifier = Modifier
                    .padding(padding)
                    .wrapContentHeight()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    itemsIndexed(following) { index, user ->

                        if (!viewModel.userDetails.containsKey(user.login)) {
                            LaunchedEffect(user.login) {
                                viewModel.fetchUserDetails(user.login)
                            }
                        }

                        val details = viewModel.userDetails[user.login]

                        //Prebuilt follower item composable
                        FollowerItem(
                            user = user,
                            name = details?.name,
                            onClick = {
                                navController.navigate("profile/${user.login}")
                            }
                        )

                        if (index < following.lastIndex) {
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
