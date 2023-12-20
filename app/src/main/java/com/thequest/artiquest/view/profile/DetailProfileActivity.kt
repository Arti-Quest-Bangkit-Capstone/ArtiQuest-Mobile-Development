package com.thequest.artiquest.view.profile

import android.Manifest
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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.thequest.artiquest.R
import com.thequest.artiquest.data.local.database.User
import com.thequest.artiquest.data.local.database.UserDatabase
import com.thequest.artiquest.databinding.ActivityDetailProfileBinding
import com.thequest.artiquest.utils.getImageUri
import kotlinx.coroutines.launch
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

                if (uid != null) {
                    lifecycleScope.launch {
                        val userDao = UserDatabase.getDatabase(this@DetailProfileActivity).userDao()

                        // Ambil data user dari database lokal
                        val existingUser = userDao.getUser(uid)

                        if (existingUser == null) {
                            // Jika data user belum ada, buat objek User baru dan simpan ke database
                            val newUser = User(
                                uid = uid,
                                username = newUsername,
                                displayName = newDisplayName,
                                phoneNumber = newNumber,
                                profilePictureUrl = currentImageUri?.toString() ?: "DEFAULT_URL"
                            )
                            userDao.insertUser(newUser)
                        } else {
                            // Jika data user sudah ada, perbarui data dengan informasi yang baru
                            existingUser.username = newUsername
                            existingUser.displayName = newDisplayName
                            existingUser.phoneNumber = newNumber
                            if (currentImageUri != null) {
                                existingUser.profilePictureUrl = currentImageUri.toString()
                            }
                            userDao.updateUser(existingUser)
                        }
                    }

                }


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


        if (photoUrl != null) {
            val cornerRadius = 32
            Glide.with(this)
                .load(photoUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(cornerRadius)))
                .placeholder(R.drawable.account_default)
                .error(R.drawable.account_default)
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

        getNewData()

    }

    private fun getNewData() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            lifecycleScope.launch {
                val userDao = UserDatabase.getDatabase(this@DetailProfileActivity).userDao()

                val existingUser = userDao.getUser(user.uid)

                if (existingUser != null) {

                    val username = existingUser.username
                    val displayName = existingUser.displayName
                    val phoneNumber = existingUser.phoneNumber
                    val profilePictureUrl = existingUser.profilePictureUrl ?: "DEFAULT_URL"

                    // Lakukan sesuatu dengan data tersebut
                    binding.editTextUsername.setText(username)
                    binding.editTextNama.setText(displayName)
                    binding.editTextNumber.setText(phoneNumber)


                    val cornerRadius = 32
                    Glide.with(this@DetailProfileActivity)
                        .load(Uri.parse(profilePictureUrl))
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(cornerRadius)))
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .error(R.drawable.baseline_account_circle_24)
                        .into(binding.profilPicture)
                } else {
                    // Data user tidak ditemukan di database lokal
                    // Mungkin inisialisasi data atau tanggapan lainnya
                }
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
        private const val REQUIRED_PERMISSION_CAMERA = Manifest.permission.CAMERA
        private const val RC_CAMERA_PERMISSION = 123
    }
}