package com.thequest.artiquest.view.login.helper

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class EmailPassHelper(private val activity: Activity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String, onComplete: (Boolean?) -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Email and Password must not be empty", Toast.LENGTH_SHORT)
                .show()
            onComplete(null)
            return  // Return early if email or password is empty
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        if (user.isEmailVerified) {
                            // Email sudah diverifikasi, izinkan masuk
                            onComplete(true)
                        } else {
                            // Email belum diverifikasi, beri tahu pengguna dan tidak izinkan masuk
                            Toast.makeText(
                                activity,
                                "Email not verified. Please check your email.",
                                Toast.LENGTH_SHORT
                            ).show()
                            onComplete(false)
                            // Anda dapat menambahkan tindakan tambahan di sini jika diperlukan
                        }
                    }
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Email or Password incorrect", Toast.LENGTH_SHORT)
                        .show()
                    onComplete(false)
                }
            }
    }

    fun register(
        username: String,
        email: String,
        password: String,
        onComplete: (Boolean?) -> Unit
    ) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                activity,
                "Username, Email and Password must not be empty",
                Toast.LENGTH_SHORT
            ).show()
            onComplete(false)
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    sendVerificationEmail(onComplete)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Registration failed.", Toast.LENGTH_SHORT).show()
                    onComplete(false)
                }
            }
    }

    private fun sendVerificationEmail(onComplete: (Boolean) -> Unit) {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Verification email sent.", Toast.LENGTH_SHORT).show()
                    onComplete(true)
                } else {
                    Toast.makeText(
                        activity,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                    onComplete(false)
                }
            }
    }

    fun resetPassword(email: String, onComplete: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Berhasil mengirim email reset kata sandi
                    onComplete(true)
                } else {
                    // Gagal mengirim email reset kata sandi
                    onComplete(false)
                }
            }
    }

    fun signOut(onComplete: () -> Unit) {
        auth.signOut()
        onComplete()
    }

    fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    companion object {
        const val TAG = "AuthManager"
    }
}