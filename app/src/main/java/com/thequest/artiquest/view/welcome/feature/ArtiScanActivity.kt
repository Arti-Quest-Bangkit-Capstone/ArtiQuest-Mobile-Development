package com.thequest.artiquest.view.welcome.feature

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thequest.artiquest.databinding.ActivityArtiScanBinding

class ArtiScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtiScanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtiScanBinding.inflate((layoutInflater))
        setContentView(binding.root)

        setupAction()

    }

    private fun setupAction() {
        binding.previousButton.setOnClickListener {
            val intent = Intent(this, FeatureActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.nextButton.setOnClickListener {
            val intent = Intent(this, ArtixploreActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}