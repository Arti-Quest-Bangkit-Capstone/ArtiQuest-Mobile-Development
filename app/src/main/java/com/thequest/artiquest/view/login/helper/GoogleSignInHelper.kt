package com.thequest.artiquest.view.login.helper

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.thequest.artiquest.R

class GoogleSignInHelper(private val activity: Activity) {

    private val googleSignInClient: GoogleSignInClient

    init {
        // Konfigurasi GoogleSignInOptions
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.web_client_id))
            .requestEmail()
            .build()

        // Membuat GoogleSignInClient dengan opsi yang dikonfigurasi
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun performSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun handleSignInResult(data: Intent?, onComplete: (GoogleSignInAccount?) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            onComplete(account)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            onComplete(null)
        }
    }

    fun signOut(onComplete: () -> Unit) {
        googleSignInClient.signOut().addOnCompleteListener(activity) {
            onComplete()
        }
    }

    fun isUserLoggedIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        return account != null
    }

    companion object {
        const val RC_SIGN_IN = 9001
        const val TAG = "GoogleSignIn"
    }
}