package com.dhruv.expenseflow.ui.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.expenseflow.domain.repository.AuthRepository
import com.dhruv.expenseflow.util.Resource
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _otpSentState = MutableStateFlow<Resource<String>?>(null)
    val otpSentState = _otpSentState.asStateFlow()

    private val _otpVerifyState = MutableStateFlow<Resource<Boolean>?>(null)
    val otpVerifyState = _otpVerifyState.asStateFlow()

    private val _googleSignInState = MutableStateFlow<Resource<Boolean>?>(null)
    val googleSignInState = _googleSignInState.asStateFlow()

    private val _anonymousSignInState = MutableStateFlow<Resource<Boolean>?>(null)
    val anonymousSignInState = _anonymousSignInState.asStateFlow()

    private val _verificationId = MutableStateFlow("")
    val verificationId = _verificationId.asStateFlow()

    fun sendOtp(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            repository.sendOtp(phoneNumber, activity).collect { result ->
                _otpSentState.value = result
                if (result is Resource.Success) {
                    _verificationId.value = result.data ?: ""
                }
            }
        }
    }

    fun verifyOtp(otpCode: String) {
        viewModelScope.launch {
            if (_verificationId.value.isEmpty()) {
                _otpVerifyState.value = Resource.Error("Verification ID is missing")
                return@launch
            }

            repository.verifyOtp(_verificationId.value, otpCode).collect { result ->
                _otpVerifyState.value = result
            }
        }
    }

    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            repository.signInWithGoogle(credential).collect { result ->
                _googleSignInState.value = result
            }
        }
    }

    fun signInAnonymously() {
        viewModelScope.launch {
            repository.signInAnonymously().collect { result ->
                _anonymousSignInState.value = result
            }
        }
    }

    fun isUserLoggedIn() = repository.isUserLoggedIn()

    fun getCurrentUserName(): String {
        return repository.getCurrentUserName() ?: "Guest"
    }
    
    fun logout() {
        repository.logout()
    }
    
    fun resetStates() {
        _otpSentState.value = null
        _otpVerifyState.value = null
        _googleSignInState.value = null
        _anonymousSignInState.value = null
    }
}
