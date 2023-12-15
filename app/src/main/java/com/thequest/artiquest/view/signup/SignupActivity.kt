package com.thequest.artiquest.view.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.thequest.artiquest.R
import com.thequest.artiquest.databinding.ActivitySignupBinding
import com.thequest.artiquest.view.custom.MyEditText
import com.thequest.artiquest.view.home.HomeActivity
import com.thequest.artiquest.view.login.LoginActivity
import com.thequest.artiquest.view.login.helper.EmailPassHelper
import com.thequest.artiquest.view.welcome.WelcomeActivity


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var emailPassHelper: EmailPassHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHint()
        setupAction()

        // Inisialisasi EmailPassSignInHelper dengan aktivitas saat ini
        emailPassHelper = EmailPassHelper(this)

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI()
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI()
                }
            }
    }
    // [END auth_with_google]

    private fun setHint() {
        val customEditTextUsername = findViewById<MyEditText>(R.id.usernameEditText)
        val customEditTextEmail = findViewById<MyEditText>(R.id.emailEditText)
        val customEditTextPassword = findViewById<MyEditText>(R.id.passwordEditText)
        customEditTextUsername.setUsernameHint()
        customEditTextEmail.setEmailHint()
        customEditTextPassword.setPasswordHint()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            emailPassHelper.register(username, email, password) { success ->
                if (success == true) {
                    // Registrasi berhasil, lakukan tindakan setelah registrasi
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }
        }

        binding.imageViewBack.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    private fun updateUI() {
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}