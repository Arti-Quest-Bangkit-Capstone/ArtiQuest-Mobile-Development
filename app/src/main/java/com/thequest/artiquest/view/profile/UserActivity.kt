package com.thequest.artiquest.view.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.thequest.artiquest.R
import com.thequest.artiquest.data.local.database.UserDatabase
import com.thequest.artiquest.databinding.ActivityUserBinding
import com.thequest.artiquest.view.camera.CameraActivity
import com.thequest.artiquest.view.home.HomeActivity
import com.thequest.artiquest.view.login.LoginActivity
import com.thequest.artiquest.view.login.helper.EmailPassHelper
import com.thequest.artiquest.view.login.helper.GoogleSignInHelper
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    private lateinit var googleSignInHelper: GoogleSignInHelper
    private lateinit var emailPassHelper: EmailPassHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        getData()

        // Inisialisasi GoogleSignInHelper dengan aktivitas saat ini
        googleSignInHelper = GoogleSignInHelper(this)

        // Inisialisasi EmailPassSignInHelper dengan aktivitas saat ini
        emailPassHelper = EmailPassHelper(this)
    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            performLogout()
        }

        binding.imageViewBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnEditprofile.setOnClickListener {
            val intent = Intent(this, DetailProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }


                else -> false
            }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun getData() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val photoUrl = user?.photoUrl
        val displayName = user?.displayName
        val email = user?.email

        // Jika foto profil tersedia, Anda dapat menggunakannya
        if (photoUrl != null) {
            val cornerRadius = 32
            Glide.with(this)
                .load(photoUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(cornerRadius)))
                .placeholder(R.drawable.baseline_account_circle_24) // Gambar placeholder jika gambar tidak dapat dimuat
                .error(R.drawable.baseline_account_circle_24) // Gambar error jika terjadi kesalahan
                .into(binding.profilePicture)
            Log.d("Profile", "Photo URL: $photoUrl")
        } else {
            // Foto profil tidak tersedia

            Log.d("Profile", "Photo URL not available")
        }

        displayName?.let {
            binding.tvName.text = it
        }

        email?.let {
            binding.tvEmail.text = it
        }

        getNewData()

    }

    private fun getNewData() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            lifecycleScope.launch {
                val userDao = UserDatabase.getDatabase(this@UserActivity).userDao()

                // Ambil data user dari database lokal
                val existingUser = userDao.getUser(user.uid)

                if (existingUser != null) {
                    // Data user ditemukan, Anda dapat mengakses bidang-bidangnya
                    val displayName = existingUser.displayName
                    val profilePictureUrl = existingUser.profilePictureUrl ?: "DEFAULT_URL"

                    // Lakukan sesuatu dengan data tersebut
                    binding.tvName.text = displayName

                    val cornerRadius = 32
                    Glide.with(this@UserActivity)
                        .load(Uri.parse(profilePictureUrl))
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(cornerRadius)))
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .error(R.drawable.baseline_account_circle_24)
                        .into(binding.profilePicture)
                } else {
                    // Data user tidak ditemukan di database lokal
                    // Mungkin inisialisasi data atau tanggapan lainnya
                }
            }

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