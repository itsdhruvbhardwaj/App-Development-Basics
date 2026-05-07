package com.dhruv.expenseflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhruv.expenseflow.domain.Expense
import com.dhruv.expenseflow.domain.TransactionType
import com.dhruv.expenseflow.ui.theme.*
import com.dhruv.expenseflow.ui.viewmodels.AuthViewModel
import com.dhruv.expenseflow.ui.viewmodels.ExpenseViewModel
import com.dhruv.expenseflow.util.Resource
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    authViewModel: AuthViewModel,
    expenseViewModel: ExpenseViewModel,
    onAddExpense: () -> Unit
) {
    val userName = authViewModel.getCurrentUserName()
    val recentTransactionsState by expenseViewModel.recentTransactionsState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackgroundGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddExpense,
                    containerColor = MainDeepPurple,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(60.dp)
                        .offset(y = 50.dp)
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add Expense", modifier = Modifier.size(32.dp))
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                CustomBottomBar()
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                item {
                    DashboardHeader(userName = userName)
                    
                    // Main Balance & Budget Card (Defaults set to 0)
                    MainBalanceCard(
                        totalBalance = 0.00,
                        monthlyBudgetSpent = 0.00f
                    )
                    
                    Spacer(modifier = Modifier.height(28.dp))
                    
                    SectionHeader(title = "Monthly Budgets", count = "0")
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CategoryBudgetCard(
                            modifier = Modifier.weight(1f),
                            category = "Food & Drinks",
                            amountSpent = "₹0.00",
                            icon = Icons.Rounded.Restaurant,
                            iconColor = CategoryPink,
                            iconBg = CategoryPinkBg,
                            progress = 0.00f,
                            progressColor = CategoryBlue
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        CategoryBudgetCard(
                            modifier = Modifier.weight(1f),
                            category = "Transport",
                            amountSpent = "₹0.00",
                            icon = Icons.Rounded.DirectionsBus,
                            iconColor = CategoryOrange,
                            iconBg = CategoryOrangeBg,
                            progress = 0.00f,
                            progressColor = CategoryOrange
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(28.dp))
                    SectionHeader(title = "Recent Transactions", count = "")
                    Spacer(modifier = Modifier.height(16.dp))
                }

                when (val state = recentTransactionsState) {
                    is Resource.Loading -> {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = PrimaryPurple)
                            }
                        }
                    }
                    is Resource.Success -> {
                        val transactions = state.data ?: emptyList()
                        if (transactions.isEmpty()) {
                            item {
                                Text(
                                    text = "No recent transactions",
                                    modifier = Modifier.padding(vertical = 20.dp),
                                    color = TextSecondary
                                )
                            }
                        } else {
                            items(transactions) { expense ->
                                TransactionItem(expense)
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                    is Resource.Error -> {
                        item {
                            Text(text = state.message ?: "Error loading transactions", color = Color.Red)
                        }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(120.dp)) }
            }
        }
    }
}

@Composable
fun TransactionItem(expense: Expense) {
    val categoryIcon = when (expense.categoryName.lowercase()) {
        "food" -> Icons.Rounded.Restaurant
        "transport" -> Icons.Rounded.DirectionsBus
        "salary" -> Icons.Rounded.Payments
        "shopping" -> Icons.Rounded.ShoppingCart
        "rent" -> Icons.Rounded.Home
        else -> Icons.Rounded.Category
    }
    
    val date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(expense.timestamp))

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PrimaryPurple.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(categoryIcon, contentDescription = null, tint = PrimaryPurple)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = expense.note.ifEmpty { expense.categoryName }, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                Text(
                    text = date,
                    style = Typography.labelMedium,
                    color = TextSecondary
                )
            }
            
            Text(
                text = "${if (expense.type == TransactionType.EXPENSE) "-" else "+"}₹${String.format("%.2f", expense.amount)}",
                fontWeight = FontWeight.Bold,
                color = if (expense.type == TransactionType.EXPENSE) Color.Red else CategoryGreen,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun MainBalanceCard(totalBalance: Double, monthlyBudgetSpent: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(brush = PrimaryPurpleGradient, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Total Balance",
                    color = Color.White.copy(alpha = 0.8f),
                    style = Typography.labelMedium
                )
                Text(
                    text = "₹${String.format("%.2f", totalBalance)}",
                    color = Color.White,
                    style = Typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Add Income", color = PrimaryPurple, fontWeight = FontWeight.Bold)
                }
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { monthlyBudgetSpent },
                    modifier = Modifier.size(80.dp),
                    color = Color.White,
                    strokeWidth = 8.dp,
                    trackColor = Color.White.copy(alpha = 0.2f),
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(monthlyBudgetSpent * 100).toInt()}%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Spent",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBudgetCard(
    modifier: Modifier = Modifier,
    category: String,
    amountSpent: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    progress: Float,
    progressColor: Color
) {
    Surface(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(20.dp),
        color = CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = category, style = Typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = amountSpent,
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = progressColor,
                trackColor = progressColor.copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, count: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, style = Typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.width(12.dp))
        if (count.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PrimaryPurple.copy(alpha = 0.1f))
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text(text = count, color = PrimaryPurple, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun CustomBottomBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
        color = Color.White,
        shadowElevation = 15.dp
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .height(56.dp)
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Rounded.Home, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(28.dp))
            Icon(Icons.Rounded.BarChart, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(50.dp))
            Icon(Icons.AutoMirrored.Rounded.ReceiptLong, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(28.dp))
            Icon(Icons.Rounded.Person, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun DashboardHeader(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 0.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "Welcome back,", style = Typography.labelMedium, color = TextSecondary)
                Text(text = userName, style = Typography.titleLarge, fontWeight = FontWeight.Bold)
            }
        }
        
        IconButton(
            onClick = { },
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.5f))
        ) {
            Icon(Icons.Rounded.Notifications, contentDescription = "Notifications", tint = TextPrimary)
        }
    }
}
