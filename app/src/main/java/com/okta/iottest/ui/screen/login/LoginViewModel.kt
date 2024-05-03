package com.okta.iottest.ui.screen.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.getString
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.okta.iottest.R
import com.okta.iottest.ui.screen.profile.GoogleSignInHelper
import kotlinx.coroutines.flow.Flow

class LoginViewModel() : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData(false)
    val loginResponse: LiveData<Boolean> = _loginResponse

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun signInWithEmailAndPassword(auth: FirebaseAuth, email: String, password: String) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    // User is successfully signed in
                    _loginResponse.value = true
                    _errorMessage.value = null
                } else {
                    // Sign in failed
                    _loginResponse.value = false
                    _errorMessage.value = task.exception?.message
                }
            }
    }
}
