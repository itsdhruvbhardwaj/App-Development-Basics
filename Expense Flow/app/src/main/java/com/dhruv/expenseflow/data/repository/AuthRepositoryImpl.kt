package com.dhruv.expenseflow.data.repository

import android.app.Activity
import com.dhruv.expenseflow.domain.repository.AuthRepository
import com.dhruv.expenseflow.util.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override fun sendOtp(
        phoneNumber: String,
        activity: Activity
    ): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto-verification or instant validation (rarely happens on all devices)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(Resource.Error(e.localizedMessage ?: "Verification Failed"))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                trySend(Resource.Success(verificationId))
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose { }
    }

    override fun verifyOtp(
        verificationId: String,
        otpCode: String
    ): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading())

        val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Resource.Success(true))
                } else {
                    trySend(Resource.Error(task.exception?.localizedMessage ?: "Invalid OTP"))
                }
            }
        awaitClose { }
    }

    override fun signInWithGoogle(credential: AuthCredential): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading())
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Resource.Success(true))
                } else {
                    trySend(Resource.Error(task.exception?.localizedMessage ?: "Google Sign In Failed"))
                }
            }
        awaitClose { }
    }

    override fun signInAnonymously(): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading())
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Resource.Success(true))
                } else {
                    trySend(Resource.Error(task.exception?.localizedMessage ?: "Anonymous Sign In Failed"))
                }
            }
        awaitClose { }
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserName(): String? {
        return auth.currentUser?.displayName ?: if (auth.currentUser?.isAnonymous == true) "Guest" else null
    }

    override fun logout() {
        auth.signOut()
    }
}
