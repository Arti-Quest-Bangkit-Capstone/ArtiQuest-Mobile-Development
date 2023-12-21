package com.thequest.artiquest.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.thequest.artiquest.R
import com.thequest.artiquest.data.remote.api.response.ArtifactItem
import com.thequest.artiquest.data.remote.api.retrofit.ApiConfig
import com.thequest.artiquest.data.repository.ArtifactRepository
import com.thequest.artiquest.databinding.ActivityHomeBinding
import com.thequest.artiquest.factory.ArtifactViewModelFactory
import com.thequest.artiquest.view.camera.CameraActivity
import com.thequest.artiquest.view.profile.UserActivity

class HomeActivity : AppCompatActivity() {

    private val repository = ArtifactRepository.getInstance(ApiConfig.getApiService())
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel> {
        ArtifactViewModelFactory.getInstance(repository)
    }
    private lateinit var artifactAdapter: ListArtifactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        showRecyclerView()

        homeViewModel.getArtifacts()

        homeViewModel.listArtifact.observe(this) { listArtifact ->
            Log.d("TAG", "List Artifact Updated: $listArtifact")
            setArtifactsData(listArtifact)
        }

        homeViewModel.loading.observe(this) {
            showLoading(it)
        }

    }

    private fun setupAction() {
        with(binding) {
            bottomNavigationView.background = null
            bottomNavigationView.menu.getItem(0).isEnabled = false
            bottomNavigationView.menu.getItem(1).isEnabled = false
            bottomNavigationView.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.profile -> {
                        val intent = Intent(this@HomeActivity, UserActivity::class.java)
                        startActivity(intent)
                        finish()
                        true
                    }

                    else -> false
                }
            }

            fab.setOnClickListener {
                val intent = Intent(this@HomeActivity, CameraActivity::class.java)
                startActivity(intent)
                finish()
            }

            // Mengasumsikan Anda memiliki SearchView dalam layout dengan id 'searchView'
            searchView.setOnQueryTextListener(object : OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // Handle penyerahan query jika diperlukan
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // Handle perubahan teks query (pencarian real-time)
                    if (newText.isNullOrEmpty()) {
                        // Teks pencarian kosong, kembalikan ke keseluruhan data
                        homeViewModel.getArtifacts()
                    } else {
                        // Handle perubahan teks query (pencarian real-time)
                        homeViewModel.searchArtifacts(newText)
                    }

                    return true
                }
            })

            // Opsional, Anda juga dapat menangani aksi 'submit'
            searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    // SearchView kehilangan fokus, Anda dapat melakukan tindakan di sini jika diperlukan
                }
            }
        }

//        binding.rvArtifacts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (!recyclerView.canScrollVertically(1)) {
//                    // RecyclerView mencapai bawah
//                    binding.bottomAppBar.animate().translationY(binding.bottomAppBar.height.toFloat()).setInterpolator(AccelerateInterpolator()).start()
//                    binding.fab.animate().translationY(binding.fab.height.toFloat()).setInterpolator(
//                        AccelerateInterpolator()
//                    ).start()
//                } else {
//                    // RecyclerView masih dapat di-scroll
//                    binding.bottomAppBar.animate().translationY(0f).setInterpolator(
//                        DecelerateInterpolator()
//                    ).start()
//                    binding.fab.animate().translationY(0f).setInterpolator(DecelerateInterpolator()).start()
//                }
//            }
//        })

        // Setel untuk Search


    }

    private fun showRecyclerView() {
        // Set layout manager to create a grid layout with 3 columns
        val layoutManager = GridLayoutManager(this, 3)
        binding.rvArtifacts.layoutManager = layoutManager

        artifactAdapter = ListArtifactAdapter(emptyList())
        binding.rvArtifacts.adapter = artifactAdapter
    }

    private fun setArtifactsData(list: List<ArtifactItem>) {
        artifactAdapter.setData(list)


    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}