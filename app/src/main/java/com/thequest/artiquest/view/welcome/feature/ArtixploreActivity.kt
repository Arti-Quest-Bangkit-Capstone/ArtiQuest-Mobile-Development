package com.thequest.artiquest.view.welcome.feature

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thequest.artiquest.databinding.ActivityArtixploreBinding

class ArtixploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtixploreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtixploreBinding.inflate((layoutInflater))
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.previousButton.setOnClickListener {
            val intent = Intent(this, ArtiScanActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.nextButton.setOnClickListener {
            val intent = Intent(this, ListenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}