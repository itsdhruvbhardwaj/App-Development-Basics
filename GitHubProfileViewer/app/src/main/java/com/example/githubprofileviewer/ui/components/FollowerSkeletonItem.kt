package com.example.githubprofileviewer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FollowerSkeletonItem() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        // Avatar
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    brush = shimmerBrush(),
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {

            // Username
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(120.dp)
                    .background(shimmerBrush())
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(80.dp)
                    .background(shimmerBrush())
            )
        }
    }
}