package com.dhruv.expenseflow.di

import com.dhruv.expenseflow.data.repository.AuthRepositoryImpl
import com.dhruv.expenseflow.data.repository.ExpenseRepositoryImpl
import com.dhruv.expenseflow.domain.repository.AuthRepository
import com.dhruv.expenseflow.domain.repository.ExpenseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(firestore, auth)
    }
}
