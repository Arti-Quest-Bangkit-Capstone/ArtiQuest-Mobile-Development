package com.thequest.artiquest.view.profile

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.thequest.artiquest.R
import com.thequest.artiquest.databinding.ActivityDetailProfileBinding
import com.thequest.artiquest.view.camera.getImageUri
import pub.devrel.easypermissions.EasyPermissions

class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding
    private var currentImageUri: Uri? = null
    private var isCaptureCompleted = false

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { result: Boolean? ->
        result?.let {
            isCaptureCompleted = it
            if (isCaptureCompleted) {
                showImage()
            } else {
                Log.d("Camera", "Capture canceled or failed")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
        setupAction()
    }

    private fun setupAction() {
        binding.cancelSave.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.save.setOnClickListener {
            val newUsername = binding.editTextUsername.text.toString()
            val newDisplayName = binding.editTextNama.text.toString()
            val newNumber = binding.editTextNumber.text.toString()

            if (newUsername.isEmpty() || newDisplayName.isEmpty() || newNumber.isEmpty()) {
                Toast.makeText(
                    this,
                    "Username, Display Name and Phone Number must not be empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val auth = FirebaseAuth.getInstance()
                val user = auth.currentUser

                val uid = user?.uid

                // Mendapatkan referensi ke SharedPreferences
                val sharedPreferences =
                    getSharedPreferences("${USER_PREFERENCE}-$uid", Context.MODE_PRIVATE)

                // Mendapatkan editor SharedPreferences
                val editor = sharedPreferences.edit()

                // Menyimpan data pengguna ke SharedPreferences
                editor.putString(NEWUSERNAME, newUsername)
                editor.putString(NEWDISPLAYNAMENAME, newDisplayName)
                editor.putString(NEWNUMBER, newNumber)
                editor.putString(PROFILE_PICTURE_URL, currentImageUri.toString())
                editor.apply()

                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.profilPicture.setOnClickListener {
            showImagePickerDialog()
        }


    }

    private fun getData() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val photoUrl = user?.photoUrl
        val displayName = user?.displayName
        val email = user?.email
        val uid = user?.uid
        val sharedPreferences =
            getSharedPreferences("$USER_PREFERENCE-$uid", Context.MODE_PRIVATE)

        if (photoUrl != null) {
            val cornerRadius = 32
            Glide.with(this)
                .load(photoUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(cornerRadius)))
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(binding.profilPicture)
            Log.d("Profile", "Photo URL: $photoUrl")
        } else {
            // Foto profil tidak tersedia

            Log.d("Profile", "Photo URL not available")
        }

        if (user != null) {
            binding.editTextNama.setText(displayName)
            binding.editTextEmail.setText(email)
        }

        val username = sharedPreferences.getString(USERNAME, "")
        binding.editTextUsername.setText(username)

        getNewData()

    }

    private fun getNewData() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val uid = user?.uid
        val sharedPreferences =
            getSharedPreferences("$USER_PREFERENCE-$uid", Context.MODE_PRIVATE)

        val newUsername = sharedPreferences.getString(NEWUSERNAME, "")
        val newDisplayName = sharedPreferences.getString(NEWDISPLAYNAMENAME, "")
        val newNumber = sharedPreferences.getString(NEWNUMBER, "")
        val profilePictureUrl = sharedPreferences.getString(PROFILE_PICTURE_URL, "")
        if (!newUsername.isNullOrEmpty() || !newDisplayName.isNullOrEmpty()) {
            binding.editTextUsername.setText(newUsername)
            binding.editTextNama.setText(newDisplayName)
        }

        binding.editTextNumber.setText(newNumber)

        if (profilePictureUrl != null) {
            if (profilePictureUrl.isNotEmpty()) {
                val cornerRadius = 32
                Glide.with(this)
                    .load(Uri.parse(profilePictureUrl))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(cornerRadius)))
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .error(R.drawable.baseline_account_circle_24)
                    .into(binding.profilPicture)
            }
        }


    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Camera", "Gallery")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Image Source")
        builder.setItems(options) { dialog: DialogInterface?, which: Int ->
            when (which) {
                0 -> startCamera()
                1 -> startGallery()
            }
            dialog?.dismiss()
        }
        builder.show()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        if (checkPermissionCameraGranted()) {
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentImageUri)
        } else {
            // Request camera permission if not granted
            EasyPermissions.requestPermissions(
                this,
                "Camera permission is required",
                RC_CAMERA_PERMISSION,
                REQUIRED_PERMISSION_CAMERA
            )
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.profilPicture.setImageURI(it)
        }
    }

    private fun checkPermissionCameraGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION_CAMERA
        ) == PackageManager.PERMISSION_GRANTED


    companion object {
        private const val PROFILE_PICTURE_URL = "profile_picture_url"
        private const val REQUIRED_PERMISSION_CAMERA = Manifest.permission.CAMERA
        private const val RC_CAMERA_PERMISSION = 123
        private const val USERNAME = "username"
        private const val NEWUSERNAME = "new_username"
        private const val NEWNUMBER = "new_number"
        private const val NEWDISPLAYNAMENAME = "new_display_name"
        private const val USER_PREFERENCE = "user_preference"

    }
}