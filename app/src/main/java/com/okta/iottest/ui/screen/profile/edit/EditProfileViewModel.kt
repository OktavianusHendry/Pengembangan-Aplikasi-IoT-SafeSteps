package com.okta.iottest.ui.screen.profile.edit

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class EditProfileViewModel : ViewModel() {
    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> get() = _email

    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> get() = _name

    private val _photoUrl = MutableStateFlow<String>("")
    val photoUrl: StateFlow<String> get() = _photoUrl

    private val _updateStatus = MutableSharedFlow<Boolean>()
    val updateStatus: SharedFlow<Boolean> get() = _updateStatus

    init {
        val user = Firebase.auth.currentUser
        _email.value = user?.email ?: ""
        _name.value = user?.displayName ?: ""
        _photoUrl.value = user?.photoUrl?.toString() ?: ""
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updatePhotoUrl(newPhotoUrl: String) {
        _photoUrl.value = newPhotoUrl
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun saveChanges() {
        val user = Firebase.auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(_name.value)
            .setPhotoUri(Uri.parse(_photoUrl.value))
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Profile updated
                    _updateStatus.tryEmit(true)
                    Log.d(ContentValues.TAG, "User profile updated.")
                } else {
                    // Profile update failed
                    _updateStatus.tryEmit(false)
                    Log.w(ContentValues.TAG, "updateProfile:failure", task.exception)
                }
            }
    }
}