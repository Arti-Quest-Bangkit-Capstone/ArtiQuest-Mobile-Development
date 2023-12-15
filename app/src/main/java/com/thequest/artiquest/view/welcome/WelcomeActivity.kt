package com.thequest.artiquest.view.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thequest.artiquest.databinding.ActivityWelcomeBinding
import com.thequest.artiquest.view.login.LoginActivity
import com.thequest.artiquest.view.signup.SignupActivity
import com.thequest.artiquest.view.welcome.feature.ListenActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate((layoutInflater))
        setContentView(binding.root)
        setupAction()
    }

    private fun setupAction() {
        binding.signinButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.previousButton.setOnClickListener {
            val intent = Intent(this, ListenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}