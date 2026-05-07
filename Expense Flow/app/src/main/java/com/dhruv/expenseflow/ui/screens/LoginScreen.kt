package com.dhruv.expenseflow.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhruv.expenseflow.ui.theme.*
import com.dhruv.expenseflow.ui.viewmodels.AuthViewModel
import com.dhruv.expenseflow.util.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val googleSignInState by viewModel.googleSignInState.collectAsState()
    val anonymousSignInState by viewModel.anonymousSignInState.collectAsState()

    // Configuration for Google Sign-In
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("142045044407-ighan2mhvp1n66egm3c7eh7l5cdd4m63.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModel.signInWithGoogle(credential)
            } catch (e: ApiException) {
                Toast.makeText(context, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(googleSignInState) {
        if (googleSignInState is Resource.Success) {
            onLoginSuccess()
            viewModel.resetStates()
        } else if (googleSignInState is Resource.Error) {
            Toast.makeText(context, googleSignInState?.message ?: "Error", Toast.LENGTH_LONG).show()
            viewModel.resetStates()
        }
    }

    LaunchedEffect(anonymousSignInState) {
        if (anonymousSignInState is Resource.Success) {
            onLoginSuccess()
            viewModel.resetStates()
        } else if (anonymousSignInState is Resource.Error) {
            Toast.makeText(context, anonymousSignInState?.message ?: "Error", Toast.LENGTH_LONG).show()
            viewModel.resetStates()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ExpenseFlow",
                style = Typography.displayLarge,
                color = PrimaryPurple,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp
            )
            
            Text(
                text = "Manage your expenses effortlessly",
                style = Typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = CardBackground,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Get Started",
                        style = Typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    // Google Sign-In Button
                    OutlinedButton(
                        onClick = { launcher.launch(googleSignInClient.signInIntent) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        enabled = googleSignInState !is Resource.Loading && anonymousSignInState !is Resource.Loading
                    ) {
                        if (googleSignInState is Resource.Loading) {
                            CircularProgressIndicator(color = PrimaryPurple, modifier = Modifier.size(24.dp))
                        } else {
                            Text(text = "Sign in with Google", fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                        Text(text = " OR ", modifier = Modifier.padding(horizontal = 8.dp), color = TextSecondary, fontSize = 12.sp)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Anonymous Sign-In Button
                    TextButton(
                        onClick = { viewModel.signInAnonymously() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = googleSignInState !is Resource.Loading && anonymousSignInState !is Resource.Loading
                    ) {
                        if (anonymousSignInState is Resource.Loading) {
                            CircularProgressIndicator(color = PrimaryPurple, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = "Continue without login",
                                color = PrimaryPurple,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
