package com.dhruv.expenseflow.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhruv.expenseflow.ui.theme.*
import com.dhruv.expenseflow.ui.viewmodels.AuthViewModel
import com.dhruv.expenseflow.util.Resource

@Composable
fun OtpVerificationScreen(
    viewModel: AuthViewModel,
    phoneNumber: String,
    onVerificationSuccess: () -> Unit
) {
    var otpCode by remember { mutableStateOf("") }
    val context = LocalContext.current
    val otpVerifyState by viewModel.otpVerifyState.collectAsState()

    // Handle OTP Verification State
    LaunchedEffect(otpVerifyState) {
        when (otpVerifyState) {
            is Resource.Success -> {
                onVerificationSuccess()
                viewModel.resetStates()
            }
            is Resource.Error -> {
                Toast.makeText(context, otpVerifyState?.message ?: "Invalid OTP", Toast.LENGTH_SHORT).show()
            }
            else -> {}
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
                text = "Verify OTP",
                style = Typography.displayLarge,
                color = PrimaryPurple,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            )
            
            Text(
                text = "Code sent to $phoneNumber",
                style = Typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
            )

            // Glassmorphic Card
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
                        text = "Enter 6-digit code",
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { if (it.length <= 6) otpCode = it },
                        label = { Text("OTP Code") },
                        placeholder = { Text("123456") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null, tint = PrimaryPurple) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.White.copy(alpha = 0.5f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                            cursorColor = PrimaryPurple
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (otpCode.length == 6) {
                                viewModel.verifyOtp(otpCode)
                            } else {
                                Toast.makeText(context, "Please enter 6-digit code", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        enabled = otpVerifyState !is Resource.Loading
                    ) {
                        if (otpVerifyState is Resource.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = "Verify & Continue",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                    
                    TextButton(
                        onClick = { /* TODO: Resend logic */ },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "Resend Code", color = PrimaryPurple)
                    }
                }
            }
        }
    }
}
