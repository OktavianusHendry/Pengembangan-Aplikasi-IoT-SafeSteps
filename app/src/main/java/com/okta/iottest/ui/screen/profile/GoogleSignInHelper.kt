package com.okta.iottest.ui.screen.profile

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.okta.iottest.R
import com.okta.iottest.ui.navigation.Screen
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleSignInHelper(
    private val context: Context,
    private val oneTapClient: SignInClient, // Creates the auth request
    private val auth: FirebaseAuth
) {
    fun createIntent(onSuccess: (IntentSenderRequest) -> Unit) {
        val request = GetSignInIntentRequest.builder()
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()

        Identity.getSignInClient(context)
            .getSignInIntent(request)
            .addOnSuccessListener { result: PendingIntent ->
                try {
                    onSuccess(
                        IntentSenderRequest.Builder(
                            result.intentSender
                        ).build()
                    )

                } catch (e: IntentSender.SendIntentException) {
                    Log.e(ContentValues.TAG, "Google Sign-in failed")
                }
            }
            .addOnFailureListener { e: Exception? ->
                Log.e(
                    ContentValues.TAG,
                    "Google Sign-in failed",
                    e
                )
            }
    }

    fun signInWithIntent(intent: Intent): Flow<Result<FirebaseUser>> = callbackFlow {
        // Get the credentials from the intent
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        // Authenticates the user
        try {
            val result = auth.signInWithCredential(googleCredentials).await()
            trySend(Result.success(result.user!!))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            trySend(Result.failure(e))
        }

        // Make sure to close the flow otherwise it will crash
        awaitClose {
            close()
        }
    }
}