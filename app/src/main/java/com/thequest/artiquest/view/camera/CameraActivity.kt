package com.thequest.artiquest.view.camera

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.thequest.artiquest.data.remote.api.retrofit.ApiConfig
import com.thequest.artiquest.data.remote.api.retrofit.ApiService
import com.thequest.artiquest.databinding.ActivityCameraBinding
import com.thequest.artiquest.utils.createCustomTempFile
import com.thequest.artiquest.view.detail.DetailActivity
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    private val apiService: ApiService = ApiConfig.getApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (
                    cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                ) CameraSelector.DEFAULT_FRONT_CAMERA
                else
                    CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
        binding.captureImage.setOnClickListener {
            takePhoto()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        if (checkPermissionCameraGranted()) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }
                imageCapture = ImageCapture.Builder()
                    .build()

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (exc: Exception) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal memuat kamera.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "startCamera: ${exc.message}")
                }
            }, ContextCompat.getMainExecutor(this))
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Camera permission is required",
                RC_CAMERA_PERMISSION,
                REQUIRED_PERMISSION_CAMERA
            )
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createCustomTempFile(this)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    sendImageForIdentification(photoFile)
                }

                override fun onError(exc: ImageCaptureException) {
                    showToast("Gagal mengambil gambar : ${exc.message}")
                    Log.e(TAG, "onError: ${exc.message}", exc)
                }
            }
        )
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    private fun sendImageForIdentification(imageFile: File) {
        showLoading(true)

        GlobalScope.launch {
            try {
                // Mengompres gambar sebelum mengirimnya
                val compressedImageFile = Compressor.compress(this@CameraActivity, imageFile)
                val requestFile = compressedImageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val body =
                    MultipartBody.Part.createFormData("file", compressedImageFile.name, requestFile)

                // Melakukan request ke server secara asinkron
                val response = apiService.scanArtifact(body).execute()

                // Kembali ke thread utama untuk menangani hasil
                launch(Dispatchers.Main) {
                    showLoading(false)

                    if (response.isSuccessful) {
                        val identificationResult = response.body()
                        identificationResult?.let {
                            val identifiedItem = it.idClass
                            val intent = Intent(this@CameraActivity, DetailActivity::class.java)
                            intent.putExtra(EXTRA_IDENTIFIED, identifiedItem.toString())
                            startActivity(intent)
                            Toast.makeText(
                                this@CameraActivity,
                                "Object teridentifikasi : $identifiedItem",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@CameraActivity,
                            "Gagal mengidentifikasi object.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                // Tangani kesalahan yang mungkin terjadi selama proses asinkron
                launch(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(
                        this@CameraActivity,
                        "Terjadi kesalahan: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture?.targetRotation = rotation
            }
        }
    }


    private fun checkPermissionCameraGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION_CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "CameraActivity"
        private const val REQUIRED_PERMISSION_CAMERA = android.Manifest.permission.CAMERA
        private const val RC_CAMERA_PERMISSION = 123
        private const val EXTRA_IDENTIFIED = "extra_identified"
    }
}