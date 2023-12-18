package com.thequest.artiquest.view.signup

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.thequest.artiquest.databinding.ActivitySignupBinding
import com.thequest.artiquest.view.home.HomeActivity
import com.thequest.artiquest.view.login.LoginActivity
import com.thequest.artiquest.view.login.helper.EmailPassHelper
import com.thequest.artiquest.view.login.helper.GoogleSignInHelper
import com.thequest.artiquest.view.welcome.WelcomeActivity


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private lateinit var emailPassHelper: EmailPassHelper
    private lateinit var googleSignInHelper: GoogleSignInHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHint()
        setupAction()

        // Inisialisasi EmailPassSignInHelper dengan aktivitas saat ini
        emailPassHelper = EmailPassHelper(this)

        // Inisialisasi GoogleSignInHelper dengan aktivitas saat ini
        googleSignInHelper = GoogleSignInHelper(this)

    }

    // [START onactivityresult]
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            // Handle hasil login Google
            googleSignInHelper.handleSignInResult(data) { account ->
                if (account != null) {
                    // Login berhasil, lakukan tindakan setelah login
                    // Misalnya, masuk ke halaman utama aplikasi
                    startActivity(Intent(this, HomeActivity::class.java))
                    Log.d(ContentValues.TAG, "Login berhasil: ${account.displayName}")
                } else {
                    // Login gagal
                    Log.d(ContentValues.TAG, "Login gagal")
                }
            }
        }
    }
    // [END onactivityresult]


    private fun setHint() {
        binding.apply {
            usernameEditText.setUsernameHint()
            emailEditText.setEmailHint()
            passwordEditText.setPasswordHint()
        }
    }

    private fun setupAction() {
        binding.signinGoogleButton.setOnClickListener {
            googleSignInHelper.performSignIn()
        }

        binding.signupButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            showLoading(true)

            emailPassHelper.register(username, email, password) { success ->
                showLoading(false)

                if (success == true) {
                    // Registrasi berhasil, lakukan tindakan setelah registrasi
                    val auth = FirebaseAuth.getInstance()
                    val user = auth.currentUser
                    val uid = user?.uid


                    // Mendapatkan referensi ke SharedPreferences
                    val sharedPreferences =
                        getSharedPreferences("$USER_PREFERENCE-$uid", Context.MODE_PRIVATE)

                    // Mendapatkan editor SharedPreferences
                    val editor = sharedPreferences.edit()

                    // Menyimpan data pengguna ke SharedPreferences menggunakan kunci USERNAME
                    editor.putString(USERNAME, username)
                    editor.apply()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.imageViewBack.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val USERNAME = "username"
        private const val USER_PREFERENCE = "user_preference"
        private const val RC_SIGN_IN = 9001

    }
}