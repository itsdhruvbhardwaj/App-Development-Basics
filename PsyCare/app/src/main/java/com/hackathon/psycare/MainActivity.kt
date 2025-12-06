package com.hackathon.psycare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hackathon.psycare.navigation.AppNavigation
import com.hackathon.psycare.ui.theme.PsyCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PsyCareTheme {
                AppNavigation()
            }
        }
    }
}
@Preview(showBackground = true, name = "App Main Preview")
@Composable
fun DefaultPreview() {
    PsyCareTheme {
        AppNavigation()
    }
}