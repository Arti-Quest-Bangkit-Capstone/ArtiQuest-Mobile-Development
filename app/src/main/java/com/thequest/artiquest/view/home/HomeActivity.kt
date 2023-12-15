package com.thequest.artiquest.view.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thequest.artiquest.databinding.ActivityHomeBinding
import com.thequest.artiquest.view.login.LoginActivity
import com.thequest.artiquest.view.login.helper.EmailPassHelper
import com.thequest.artiquest.view.login.helper.GoogleSignInHelper

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var googleSignInHelper: GoogleSignInHelper
    private lateinit var emailPassHelper: EmailPassHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

        // Inisialisasi GoogleSignInHelper dengan aktivitas saat ini
        googleSignInHelper = GoogleSignInHelper(this)

        // Inisialisasi EmailPassSignInHelper dengan aktivitas saat ini
        emailPassHelper = EmailPassHelper(this)


    }

    private fun setupAction() {
        // Setel listener untuk tombol logout
        binding.btnSignout.setOnClickListener {
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