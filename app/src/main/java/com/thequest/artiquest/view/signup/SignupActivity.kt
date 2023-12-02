package com.thequest.artiquest.view.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thequest.artiquest.R
import com.thequest.artiquest.databinding.ActivitySignupBinding
import com.thequest.artiquest.view.custom.MyEditText
import com.thequest.artiquest.view.login.LoginActivity
import com.thequest.artiquest.view.welcome.WelcomeActivity


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHint()
        setupAction()

    }

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
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.imageViewBack.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

    }
}