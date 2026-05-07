package com.dhruv.expenseflow.domain.repository

import android.app.Activity
import com.dhruv.expenseflow.util.Resource
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun sendOtp(
        phoneNumber: String,
        activity: Activity
    ): Flow<Resource<String>>

    fun verifyOtp(
        verificationId: String,
        otpCode: String
    ): Flow<Resource<Boolean>>

    fun signInWithGoogle(credential: AuthCredential): Flow<Resource<Boolean>>

    fun signInAnonymously(): Flow<Resource<Boolean>>

    fun isUserLoggedIn(): Boolean

    fun getCurrentUserName(): String?
    
    fun logout()
}
