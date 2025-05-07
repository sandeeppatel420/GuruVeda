package com.example.guruveda.DoubtActivity

import android.Manifest
import android.R
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.guruveda.ViewModel.DoubtViewModel
import com.example.guruveda.databinding.ActivityDoubtBinding

class DoubtActivity : AppCompatActivity() {
    private val CAMERA_PERMISSION_CODE = 101
    private val IMAGE_PICK_CODE = 102
    private val viewModel: DoubtViewModel by viewModels()

    private var selectedImageUri: Uri? = null
    lateinit var binding: ActivityDoubtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoubtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner()
        setupListeners()

        binding.uploadFileImage.visibility = View.GONE
    }

    private fun spinner() {
        val list = arrayOf("Kotlin", "Dart", "Android", "PHP", "Java", "Python")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, list)
        binding.enquiryTypeSpinner.adapter = adapter
    }

    private fun setupListeners() {
        binding.submitDoubtButton.setOnClickListener {
            val subjectName = binding.enquiryTypeSpinner.selectedItem.toString()
            val doubtMessage = binding.doubtMessage.text.toString()

            if (doubtMessage.isEmpty()) {
                Toast.makeText(this, "Please enter your doubt", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



    viewModel.addDoubt(subjectName, doubtMessage, selectedImageUri) { success ->
        if (success) {
            binding.circleProgressView.visibility = View.VISIBLE
            val animator = ObjectAnimator.ofInt(binding.circleProgressView, "progress", 0, 100)
            animator.duration = 2000
            animator.start()

            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.circleProgressView.visibility = View.GONE
                    Toast.makeText(this@DoubtActivity, "Doubt submitted successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        } else {
            binding.circleProgressView.visibility = View.GONE
            Toast.makeText(this, "Failed to submit doubt.", Toast.LENGTH_SHORT).show()
        }
    }


        }

        binding.uploadFileButton.setOnClickListener {
            checkCameraPermission()
            binding.uploadFileImage.visibility = View.VISIBLE
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            openImageChooser()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImageChooser()
        } else {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImageChooser() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val chooser = Intent.createChooser(galleryIntent, "Select Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        startActivityForResult(chooser, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            data?.let {
                val bitmap = it.extras?.get("data") as? Bitmap
                if (bitmap != null) {
                    binding.uploadFileImage.setImageBitmap(bitmap)
                    val uri = getImageUriFromBitmap(bitmap)
                    selectedImageUri = uri
                } else {
                    val uri = it.data
                    if (uri != null) {
                        binding.uploadFileImage.setImageURI(uri)
                        selectedImageUri = uri
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "TempImage", null)
        return Uri.parse(path)
    }


}