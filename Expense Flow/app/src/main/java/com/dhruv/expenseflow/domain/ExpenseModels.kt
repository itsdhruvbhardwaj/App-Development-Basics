package com.dhruv.expenseflow.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Expense(
    val id: String = "",
    val amount: Double = 0.0,
    val note: String = "",
    val categoryName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: TransactionType = TransactionType.EXPENSE
)

enum class TransactionType {
    INCOME, EXPENSE
}

data class Category(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val bgColor: Color,
    val budget: Double = 0.0,
    val spent: Double = 0.0
)
