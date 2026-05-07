package com.dhruv.expenseflow.data.repository

import com.dhruv.expenseflow.domain.Expense
import com.dhruv.expenseflow.domain.repository.ExpenseRepository
import com.dhruv.expenseflow.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ExpenseRepository {

    private val userId: String
        get() = auth.currentUser?.uid ?: ""

    private val expenseCollection
        get() = firestore.collection("users").document(userId).collection("expenses")

    override fun addExpense(expense: Expense): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading())
        
        if (userId.isEmpty()) {
            trySend(Resource.Error("User not logged in"))
            close()
            return@callbackFlow
        }

        val docRef = if (expense.id.isEmpty()) expenseCollection.document() else expenseCollection.document(expense.id)
        val finalExpense = expense.copy(id = docRef.id)

        docRef.set(finalExpense)
            .addOnSuccessListener {
                trySend(Resource.Success(true))
            }
            .addOnFailureListener { e ->
                trySend(Resource.Error(e.localizedMessage ?: "Failed to add expense"))
            }
        
        awaitClose { }
    }

    override fun getExpenses(): Flow<Resource<List<Expense>>> = callbackFlow {
        trySend(Resource.Loading())

        if (userId.isEmpty()) {
            trySend(Resource.Error("User not logged in"))
            close()
            return@callbackFlow
        }

        val subscription = expenseCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.localizedMessage ?: "Error fetching expenses"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val expenses = snapshot.toObjects(Expense::class.java)
                    trySend(Resource.Success(expenses))
                }
            }

        awaitClose { subscription.remove() }
    }

    override fun deleteExpense(expenseId: String): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading())
        
        expenseCollection.document(expenseId).delete()
            .addOnSuccessListener {
                trySend(Resource.Success(true))
            }
            .addOnFailureListener { e ->
                trySend(Resource.Error(e.localizedMessage ?: "Failed to delete expense"))
            }
        
        awaitClose { }
    }

    override fun getRecentTransactions(limit: Int): Flow<Resource<List<Expense>>> = callbackFlow {
        trySend(Resource.Loading())

        if (userId.isEmpty()) {
            trySend(Resource.Error("User not logged in"))
            close()
            return@callbackFlow
        }

        val subscription = expenseCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.localizedMessage ?: "Error fetching recent transactions"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val expenses = snapshot.toObjects(Expense::class.java)
                    trySend(Resource.Success(expenses))
                }
            }

        awaitClose { subscription.remove() }
    }
}
