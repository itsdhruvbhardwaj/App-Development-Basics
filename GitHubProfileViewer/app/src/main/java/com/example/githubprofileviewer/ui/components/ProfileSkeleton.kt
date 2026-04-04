package com.example.githubprofileviewer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileSkeleton() {

    val brush = shimmerBrush()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        // 🔹 Top Section (Avatar + Info)
        Row {

            // Avatar (improved)
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(brush, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                // Name
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(20.dp)
                        .background(brush, RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bio line 1
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(14.dp)
                        .background(brush, RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Bio line 2
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp)
                        .background(brush, RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Stats Section (NEW)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            repeat(3) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(18.dp)
                            .background(brush, RoundedCornerShape(6.dp))
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(12.dp)
                            .background(brush, RoundedCornerShape(6.dp))
                    )
                }
            }
        }
    }
}