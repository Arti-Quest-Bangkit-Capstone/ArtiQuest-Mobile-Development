package com.thequest.artiquest.view.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thequest.artiquest.R
import com.thequest.artiquest.data.local.database.Feature
import com.thequest.artiquest.databinding.ActivityWelcomeBinding
import com.thequest.artiquest.view.home.HomeActivity
import com.thequest.artiquest.view.login.helper.EmailPassHelper
import com.thequest.artiquest.view.login.helper.GoogleSignInHelper
import com.thequest.artiquest.view.welcome.feature.FeatureAdapter

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var googleSignInHelper: GoogleSignInHelper
    private lateinit var emailPassHelper: EmailPassHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate((layoutInflater))
        setContentView(binding.root)
        sliderWelcome()

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

//    private fun setupAction() {
//        binding.signinButton.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        binding.signupButton.setOnClickListener {
//            val intent = Intent(this, SignupActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        binding.previousButton.setOnClickListener {
//            val intent = Intent(this, ListenActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }

    private fun sliderWelcome() {
        val features = listOf(
            Feature("", resources.getString(R.string.explaination_apk), false),
            Feature(resources.getString(R.string.feature1), resources.getString(R.string.explain_feature1),false),
            Feature(resources.getString(R.string.feature2), resources.getString(R.string.explain_feature2), false),
            Feature(resources.getString(R.string.feature3), resources.getString(R.string.explain_feature3), false),
            Feature(resources.getString(R.string.welcome), resources.getString(R.string.quote_welcome), true)
        )

        val adapter = FeatureAdapter(features)
        binding.viewPager.adapter = adapter

        binding.dotsIndicator.attachTo(binding.viewPager)
    }
}