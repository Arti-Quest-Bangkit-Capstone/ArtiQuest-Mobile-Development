package com.thequest.artiquest.view.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.thequest.artiquest.R
import com.thequest.artiquest.databinding.ActivityUserBinding
import com.thequest.artiquest.view.home.HomeActivity
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

    }

    private fun getData() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val photoUrl = user?.photoUrl
        val displayName = user?.displayName
        val email = user?.email

        Log.d("Profile", "PDISP: $displayName")

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
        val uid = user?.uid
        val sharedPreferences = getSharedPreferences("$USER_PREFERENCE-$uid", Context.MODE_PRIVATE)

        val profilePictureUrl = sharedPreferences.getString(PROFILE_PICTURE_URL, "")
        val newDisplayName = sharedPreferences.getString(NEWDISPLAYNAMENAME, "")
        if (!newDisplayName.isNullOrEmpty()) {
            binding.tvName.text = newDisplayName
        }

        if (profilePictureUrl != null) {
            if (profilePictureUrl.isNotEmpty()) {
                val cornerRadius = 32
                Glide.with(this)
                    .load(Uri.parse(profilePictureUrl))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(cornerRadius)))
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .error(R.drawable.baseline_account_circle_24)
                    .into(binding.profilePicture)
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

    companion object {
        private const val PROFILE_PICTURE_URL = "profile_picture_url"
        private const val NEWDISPLAYNAMENAME = "new_display_name"
        private const val USER_PREFERENCE = "user_preference"
    }
}