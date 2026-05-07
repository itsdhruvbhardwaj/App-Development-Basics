package com.dhruv.expenseflow.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.expenseflow.domain.Expense
import com.dhruv.expenseflow.domain.repository.ExpenseRepository
import com.dhruv.expenseflow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _expensesState = MutableStateFlow<Resource<List<Expense>>>(Resource.Loading())
    val expensesState = _expensesState.asStateFlow()

    private val _recentTransactionsState = MutableStateFlow<Resource<List<Expense>>>(Resource.Loading())
    val recentTransactionsState = _recentTransactionsState.asStateFlow()

    init {
        getRecentTransactions()
    }

    fun getExpenses() {
        viewModelScope.launch {
            repository.getExpenses().collect { result ->
                _expensesState.value = result
            }
        }
    }

    fun getRecentTransactions(limit: Int = 4) {
        viewModelScope.launch {
            repository.getRecentTransactions(limit).collect { result ->
                _recentTransactionsState.value = result
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addExpense(expense).collect { /* Handle result if needed */ }
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            repository.deleteExpense(expenseId).collect { /* Handle result if needed */ }
        }
    }
}
