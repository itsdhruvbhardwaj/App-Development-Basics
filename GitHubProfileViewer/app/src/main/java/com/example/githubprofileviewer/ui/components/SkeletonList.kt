package com.example.githubprofileviewer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SkeletonList() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(16.dp))
            ProfileSkeleton()
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(5) {
            SkeletonItem()
        }
    }
}