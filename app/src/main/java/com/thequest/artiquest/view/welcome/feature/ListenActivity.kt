package com.thequest.artiquest.view.welcome.feature

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thequest.artiquest.databinding.ActivityListenBinding
import com.thequest.artiquest.view.welcome.WelcomeActivity

class ListenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListenBinding.inflate((layoutInflater))
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.previousButton.setOnClickListener {
            val intent = Intent(this, ArtixploreActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.nextButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}