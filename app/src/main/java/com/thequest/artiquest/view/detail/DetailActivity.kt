package com.thequest.artiquest.view.detail

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.thequest.artiquest.R
import com.thequest.artiquest.data.remote.api.retrofit.ApiConfig
import com.thequest.artiquest.data.repository.ArtifactRepository
import com.thequest.artiquest.databinding.ActivityDetailBinding
import com.thequest.artiquest.factory.ArtifactViewModelFactory
import com.thequest.artiquest.view.home.HomeActivity
import java.util.Locale

class DetailActivity : AppCompatActivity(), OnMapReadyCallback, TextToSpeech.OnInitListener {

    private lateinit var mMap: GoogleMap
    private val repository = ArtifactRepository.getInstance(ApiConfig.getApiService())
    private lateinit var binding: ActivityDetailBinding
    private lateinit var vpSlider: ViewPager
    private lateinit var textToSpeech: TextToSpeech
    private val detailViewModel by viewModels<DetailViewModel> {
        ArtifactViewModelFactory.getInstance(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        textToSpeech = TextToSpeech(this, this)
        setupAction()
        getDetailArtifact()


    }

    private fun setupAction() {
        binding.speakDesc.setOnClickListener {
            detailViewModel.detailArtifact.observe(this) { artifact ->
                val textToRead = artifact.description
                if (textToRead != null) {
                    speakText(textToRead)
                }
            }

        }

        binding.imageViewBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getDetailArtifact() {
        val id = intent.getStringExtra(EXTRA_ID)
        if (id != null) {
            detailViewModel.getDetailArtifact(id)
            detailViewModel.detailArtifact.observe(this) { artifact ->
                binding.apply {
                    tvName.text = artifact.displayName
                    tvDescription.text = artifact.description
//                    Glide.with(root.context)
//                        .load(artifact.photoUrl)
//                        .into(imageDetailStory)
                    val handler = Handler(Looper.getMainLooper())
                    vpSlider = binding.viewPager
//        val arrSlider= ArrayList<Int>()
//        arrSlider.add(R.drawable.save)
//        arrSlider.add(R.drawable.baseline_account_circle_24)
//        arrSlider.add(R.drawable.account_default)

                    val imageUrls = listOf(
                        artifact.displayIcon,
                        artifact.bustPortrait,
                        artifact.background
                    )
                    val adapterSlider = AdapterSilder(this.root.context, imageUrls)
                    vpSlider.adapter = adapterSlider

                    val runnable = object : Runnable {
                        override fun run() {
                            val currentItem = vpSlider.currentItem
                            val totalCount = adapterSlider.count

                            if (currentItem < totalCount - 1) {
                                vpSlider.currentItem = currentItem + 1
                            } else {
                                vpSlider.currentItem = 0
                            }

                            handler.postDelayed(this, delay)
                        }
                    }

                    handler.postDelayed(runnable, delay)

                }
            }

            detailViewModel.loading.observe(this) { loading ->
                showLoading(loading)
            }
        }
    }

//    private fun pictureSlider() {
//        val handler = Handler(Looper.getMainLooper())
//        vpSlider = binding.viewPager
////        val arrSlider= ArrayList<Int>()
////        arrSlider.add(R.drawable.save)
////        arrSlider.add(R.drawable.baseline_account_circle_24)
////        arrSlider.add(R.drawable.account_default)
//
//        val imageUrls = listOf(
//
//        )
//        val adapterSlider = AdapterSilder(arrSlider , this)
//        vpSlider.adapter = adapterSlider
//
//        val runnable = object : Runnable {
//            override fun run() {
//                val currentItem = vpSlider.currentItem
//                val totalCount = adapterSlider.count
//
//                if (currentItem < totalCount - 1) {
//                    vpSlider.currentItem = currentItem + 1
//                } else {
//                    vpSlider.currentItem = 0
//                }
//
//                handler.postDelayed(this, delay)
//            }
//        }
//
//        handler.postDelayed(runnable, delay)
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Konfigurasi peta dan tampilkan marker atau informasi lainnya
        mMap = googleMap
        val sydney = LatLng(-34.0, 151.0)

        mMap.addMarker(MarkerOptions().position(sydney).title("This is Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Language is not supported.")
            }
        } else {
            Log.e("TextToSpeech", "Initialization failed.")
        }
    }

    private fun speakText(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }

        super.onDestroy()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val delay = 4000L
        private const val EXTRA_ID = "extra_id"
    }


}