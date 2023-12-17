package com.thequest.artiquest.view.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thequest.artiquest.R
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

        // Sample data
        val data = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6")

        val recyclerView: RecyclerView = findViewById(R.id.rvArtifacts)

        // Set layout manager to create a grid layout with 4 columns
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager

        // Set adapter
        val adapter = ListArtifactAdapter(data)
        recyclerView.adapter = adapter

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false


    }

    private fun setupAction() {
        // Setel listener untuk tombol logout
//        binding.btnSignout.setOnClickListener {
//            performLogout()
//        }

        // Setel untuk Search

        with(binding) {
            searchView
//            searchView
//                .editText
//                .setOnEditorActionListener { textView, actionId, event ->
//                    searchBar.text = searchView.text
//                    searchView.hide()
//                    mainViewModel.searchGithubUser(searchView.text.toString())
//                    false
//                }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}