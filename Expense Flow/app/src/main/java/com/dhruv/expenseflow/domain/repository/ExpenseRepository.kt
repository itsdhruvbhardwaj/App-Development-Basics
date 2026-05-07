package com.dhruv.expenseflow.domain.repository

import com.dhruv.expenseflow.domain.Expense
import com.dhruv.expenseflow.util.Resource
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun addExpense(expense: Expense): Flow<Resource<Boolean>>
    fun getExpenses(): Flow<Resource<List<Expense>>>
    fun deleteExpense(expenseId: String): Flow<Resource<Boolean>>
    fun getRecentTransactions(limit: Int): Flow<Resource<List<Expense>>>
}
