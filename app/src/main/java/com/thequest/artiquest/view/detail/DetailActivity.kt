package com.thequest.artiquest.view.detail

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.thequest.artiquest.R
import com.thequest.artiquest.databinding.ActivityDetailBinding
import com.thequest.artiquest.view.home.HomeActivity
import java.util.Locale

class DetailActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var vpSlider: ViewPager
    private lateinit var textToSpeech: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textToSpeech = TextToSpeech(this, this)
        setupAction()
        pictureSlider()


    }

    private fun setupAction() {
        binding.speakDesc.setOnClickListener {
            val textToRead = "Teks yang ingin dibacakan"  // Gantilah dengan teks yang ingin dibacakan
            speakText(textToRead)
        }

        binding.imageViewBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun pictureSlider() {
        val handler = Handler(Looper.getMainLooper())
        vpSlider = binding.viewPager
        val arrSlider= ArrayList<Int>()
        arrSlider.add(R.drawable.save)
        arrSlider.add(R.drawable.baseline_account_circle_24)
        arrSlider.add(R.drawable.account_default)

        val adapterSlider = AdapterSilder(arrSlider , this)
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

    companion object {
        private const val delay = 4000L
    }


}