package com.dhruv.expenseflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dhruv.expenseflow.ui.screens.AddExpenseScreen
import com.dhruv.expenseflow.ui.screens.DashboardScreen
import com.dhruv.expenseflow.ui.screens.LoginScreen
import com.dhruv.expenseflow.ui.theme.ExpenseFlowTheme
import com.dhruv.expenseflow.ui.viewmodels.AuthViewModel
import com.dhruv.expenseflow.ui.viewmodels.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseFlowTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()
                val expenseViewModel: ExpenseViewModel = hiltViewModel()

                // Check if user is already logged in
                val startUser = authViewModel.isUserLoggedIn()
                val startDestination = if (startUser) "dashboard" else "login"

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable("login") {
                        LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    
                    composable("dashboard") {
                        DashboardScreen(
                            authViewModel = authViewModel,
                            expenseViewModel = expenseViewModel,
                            onAddExpense = {
                                navController.navigate("add_expense")
                            }
                        )
                    }

                    composable("add_expense") {
                        AddExpenseScreen(
                            viewModel = expenseViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
