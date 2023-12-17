package com.thequest.artiquest.view.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thequest.artiquest.R
import com.thequest.artiquest.databinding.ActivityHomeBinding
import com.thequest.artiquest.databinding.ActivityUserBinding
import com.thequest.artiquest.view.login.LoginActivity
import com.thequest.artiquest.view.login.helper.EmailPassHelper
import com.thequest.artiquest.view.login.helper.GoogleSignInHelper

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    private lateinit var googleSignInHelper: GoogleSignInHelper
    private lateinit var emailPassHelper: EmailPassHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

        // Inisialisasi GoogleSignInHelper dengan aktivitas saat ini
        googleSignInHelper = GoogleSignInHelper(this)

        // Inisialisasi EmailPassSignInHelper dengan aktivitas saat ini
        emailPassHelper = EmailPassHelper(this)
    }

    private fun setupAction() {
        // Setel listener untuk tombol logout
        binding.btnLogout.setOnClickListener {
            performLogout()
        }


    }

    private fun performLogout() {
        // Logout menggunakan GoogleSignInHelper
        googleSignInHelper.signOut {
            // Redirect atau lakukan tindakan lain setelah logout
            // Contoh: Kembali ke halaman login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Logout menggunakan EmailPasswordHelper
        emailPassHelper.signOut {
            // Redirect atau lakukan tindakan lain setelah logout
            // Contoh: Kembali ke halaman login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}