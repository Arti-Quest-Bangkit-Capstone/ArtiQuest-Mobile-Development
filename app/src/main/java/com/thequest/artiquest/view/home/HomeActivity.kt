package com.thequest.artiquest.view.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thequest.artiquest.R
import com.thequest.artiquest.data.remote.api.response.AgentItem
import com.thequest.artiquest.data.remote.api.retrofit.ApiConfig
import com.thequest.artiquest.data.repository.ArtifactRepository
import com.thequest.artiquest.databinding.ActivityHomeBinding
import com.thequest.artiquest.factory.ArtifactViewModelFactory
import com.thequest.artiquest.view.camera.CameraActivity
import com.thequest.artiquest.view.profile.UserActivity

class HomeActivity : AppCompatActivity() {

    private val repository = ArtifactRepository.getInstance(ApiConfig.getApiService())
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>{
        ArtifactViewModelFactory.getInstance(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        showRecyclerView()

        homeViewModel.getArtifacts()

        homeViewModel.listArtifact.observe(this) { listArtifact ->
            setArtifactsData(listArtifact)
        }

        homeViewModel.loading.observe(this) {
            showLoading(it)
        }




    }

    private fun setupAction() {
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(0).isEnabled = false
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                    finish()
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

        binding.rvArtifacts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    // RecyclerView mencapai bawah
                    binding.bottomAppBar.animate().translationY(binding.bottomAppBar.height.toFloat()).setInterpolator(AccelerateInterpolator()).start()
                    binding.fab.animate().translationY(binding.fab.height.toFloat()).setInterpolator(
                        AccelerateInterpolator()
                    ).start()
                } else {
                    // RecyclerView masih dapat di-scroll
                    binding.bottomAppBar.animate().translationY(0f).setInterpolator(
                        DecelerateInterpolator()
                    ).start()
                    binding.fab.animate().translationY(0f).setInterpolator(DecelerateInterpolator()).start()
                }
            }
        })

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

    private fun showRecyclerView() {
        // Set layout manager to create a grid layout with 3 columns
        val layoutManager = GridLayoutManager(this, 3)
        binding.rvArtifacts.layoutManager = layoutManager
    }

    private fun setArtifactsData(list: List<AgentItem>) {
        // Set adapter
        val adapter = ListArtifactAdapter(list)
        binding.rvArtifacts.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}