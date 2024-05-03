package com.okta.iottest.ui.screen.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel(){
    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> get() = _email

    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> get() = _name

    private val _photoUrl = MutableStateFlow<String>("")
    val photoUrl: StateFlow<String> get() = _photoUrl

    init {
        val user = Firebase.auth.currentUser
        _email.value = user?.email ?: ""
        _name.value = user?.displayName ?: ""
        _photoUrl.value = user?.photoUrl?.toString() ?: ""
    }
}