package com.thequest.artiquest.view.welcome.feature

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thequest.artiquest.databinding.ActivityFeatureBinding
import com.thequest.artiquest.view.home.HomeActivity
import com.thequest.artiquest.view.login.helper.EmailPassHelper
import com.thequest.artiquest.view.login.helper.GoogleSignInHelper

class FeatureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeatureBinding

    private lateinit var googleSignInHelper: GoogleSignInHelper
    private lateinit var emailPassHelper: EmailPassHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeatureBinding.inflate((layoutInflater))
        setContentView(binding.root)

        setupAction()

        // Inisialisasi EmailPassSignInHelper dengan aktivitas saat ini
        emailPassHelper = EmailPassHelper(this)

        // Inisialisasi GoogleSignInHelper dengan aktivitas saat ini
        googleSignInHelper = GoogleSignInHelper(this)

        // Cek apakah pengguna sudah login, jika ya, langsung navigasikan ke HomeActivity
        if (googleSignInHelper.isUserLoggedIn()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
            return
        }

        if (emailPassHelper.isUserLoggedIn()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
            return
        }
    }

    private fun setupAction() {
        binding.nextButton.setOnClickListener {
            val intent = Intent(this, ArtiScanActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}