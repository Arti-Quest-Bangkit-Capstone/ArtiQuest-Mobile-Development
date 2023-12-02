package com.thequest.artiquest.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thequest.artiquest.R
import com.thequest.artiquest.databinding.ActivityLoginBinding
import com.thequest.artiquest.view.custom.MyEditText
import com.thequest.artiquest.view.home.HomeActivity
import com.thequest.artiquest.view.signup.SignupActivity
import com.thequest.artiquest.view.welcome.WelcomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHint()
        setupAction()

    }

    private fun setHint() {
        val customEditTextEmail = findViewById<MyEditText>(R.id.emailEditText)
        val customEditTextPassword = findViewById<MyEditText>(R.id.passwordEditText)
        customEditTextEmail.setEmailHint()
        customEditTextPassword.setPasswordHint()
    }

    private fun setupAction() {
        binding.signinButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.imageViewBack.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}