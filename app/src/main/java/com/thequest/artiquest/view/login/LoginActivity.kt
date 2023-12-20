package com.thequest.artiquest.view.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.thequest.artiquest.databinding.ActivityLoginBinding
import com.thequest.artiquest.view.home.HomeActivity
import com.thequest.artiquest.view.login.helper.EmailPassHelper
import com.thequest.artiquest.view.login.helper.GoogleSignInHelper
import com.thequest.artiquest.view.signup.SignupActivity
import com.thequest.artiquest.view.welcome.WelcomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInHelper: GoogleSignInHelper
    private lateinit var emailPassHelper: EmailPassHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi EmailPassSignInHelper dengan aktivitas saat ini
        emailPassHelper = EmailPassHelper(this)

        // Inisialisasi GoogleSignInHelper dengan aktivitas saat ini
        googleSignInHelper = GoogleSignInHelper(this)


        setHint()
        setupAction()

    }

    // Panggil fungsi ini dari onActivityResult
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
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finish()
                    Log.d(TAG, "Login berhasil: ${account.displayName}")
                } else {
                    // Login gagal
                    Log.d(TAG, "Login gagal")
                }
            }
        }
    }


    private fun setHint() {
        binding.apply {
            emailEditText.setEmailHint()
            passwordEditText.setPasswordHint()
        }
    }

    private fun setupAction() {
        binding.signinButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            showLoading(true)

            emailPassHelper.login(email, password) { user ->
                // Sembunyikan ProgressBar saat proses login selesai
                showLoading(false)

                if (user != null) {
                    // Pastikan email sudah diverifikasi sebelum pindah ke HomeActivity
                    if (user == true) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        binding.imageViewBack.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Setel listener untuk tombol login Google
        binding.signinGoogleButton.setOnClickListener {
            googleSignInHelper.performSignIn()
        }

        binding.forgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}