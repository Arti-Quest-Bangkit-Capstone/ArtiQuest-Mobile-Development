package com.thequest.artiquest.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thequest.artiquest.R
import com.thequest.artiquest.databinding.ActivityHomeBinding
import com.thequest.artiquest.view.profile.UserActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()


        // Sample data
        val data = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6")

        // Set layout manager to create a grid layout with 3 columns
        val layoutManager = GridLayoutManager(this, 3)
        binding.rvArtifacts.layoutManager = layoutManager

        // Set adapter
        val adapter = ListArtifactAdapter(data)
        binding.rvArtifacts.adapter = adapter





    }

    private fun setupAction() {
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            Log.d("Navigation", "Item clicked: ${menuItem}")
            when (menuItem.itemId) {
                R.id.home -> {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.profile -> {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                else -> false
            }
        }

        // Setel untuk Search

//        with(binding) {
//            searchView
//            searchView
//                .editText
//                .setOnEditorActionListener { textView, actionId, event ->
//                    searchBar.text = searchView.text
//                    searchView.hide()
//                    mainViewModel.searchGithubUser(searchView.text.toString())
//                    false
//                }
//        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}