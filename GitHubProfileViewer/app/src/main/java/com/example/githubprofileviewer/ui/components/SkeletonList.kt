package com.example.githubprofileviewer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SkeletonList() {

    Column {

        // 👤 Profile loading
        ProfileSkeleton()

        Spacer(modifier = Modifier.height(16.dp))

        // 📦 Repo list loading
        repeat(5) {
            SkeletonItem()
        }
    }
}