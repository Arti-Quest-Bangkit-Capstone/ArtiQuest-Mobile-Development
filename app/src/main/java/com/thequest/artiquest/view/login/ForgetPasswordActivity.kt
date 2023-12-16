package com.thequest.artiquest.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.thequest.artiquest.R
import com.thequest.artiquest.databinding.ActivityForgetPasswordBinding
import com.thequest.artiquest.view.login.helper.EmailPassHelper

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var emailPassHelper: EmailPassHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        emailPassHelper = EmailPassHelper(this)

    }

    private fun setupAction() {
        binding.editTextEmailReset.addTextChangedListener {
            validateEmail(it.toString())
        }

        binding.sendForgotButton.setOnClickListener {
            val email = binding.editTextEmailReset.text.toString()

            // Tampilkan ProgressBar saat proses login dimulai
            showLoading(true)

            emailPassHelper.resetPassword(email) { success ->

                // Sembunyikan ProgressBar saat proses login selesai
                showLoading(false)

                if (success) {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this, "Password reset email has been sent.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Failed to send password reset email.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }

        binding.imageViewBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateEmail(email: String) {
        // Format ekspresi reguler untuk validasi email
        val emailPattern = Patterns.EMAIL_ADDRESS

        if (email.isEmpty()) {
            // Jika email kosong, tampilkan pesan error
            binding.editTextEmailReset.error = resources.getString(R.string.email_warning_empty)
        } else if (!emailPattern.matcher(email).matches()) {
            // Jika email tidak sesuai format, tampilkan pesan error
            binding.editTextEmailReset.error = resources.getString(R.string.email_warning_invalid)
        } else {
            // Jika email sesuai format, hapus pesan error
            binding.editTextEmailReset.error = null
        }
    }

    private fun showLoading(isLoading: Boolean) { binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE }
}