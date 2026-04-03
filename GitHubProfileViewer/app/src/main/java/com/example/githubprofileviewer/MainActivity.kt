package com.example.githubprofileviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.githubprofileviewer.ui.MainScreen
import com.example.githubprofileviewer.ui.navigation.AppNavigation
import com.example.githubprofileviewer.ui.theme.GitHubProfileViewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GitHubProfileViewerTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    // ✅ THIS is your real app UI
                    AppNavigation()
                }
            }
        }
    }
}

