package com.example.guruveda.Auth

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.guruveda.R
import com.example.guruveda.ViewModel.ProfileUpdateViewModel
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class ProfileEditActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var viewModel: ProfileUpdateViewModel
    private var imageUri: Uri?=null
    private lateinit var editImage:CardView
    private var updateImage:Boolean=false
    private lateinit var progressDialog: ProgressDialog

    private var currentId:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)



        nameEditText=findViewById(R.id.profileName_EditText)
        emailEditText=findViewById(R.id.profileEmail_EditText)
        profileImageView=findViewById(R.id.ProfileImage_ImageView)

        val nameData=intent.getStringExtra("name")
        val emailData=intent.getStringExtra("email")
        val imageData=intent.getStringExtra("imageProfile")

        currentId = intent.getStringExtra("id") ?: ""
       nameEditText.setText(nameData)
        emailEditText.setText(emailData)
        Glide.with(this).load(imageData).into(profileImageView)
        viewModel= ViewModelProvider(this)[ProfileUpdateViewModel::class.java]

        viewModel.updateProfile.observe(this){
            progressDialog.dismiss()
            if (it!=null){
                finish()
            }
        }
        val updateBtn=findViewById<AppCompatButton>(R.id.update_Button)


        updateBtn.setOnClickListener {
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Updating...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            val nameUpdate=nameEditText.text.toString()
            val emailUpdate=emailEditText.text.toString()
          if (updateImage){
              uploadImage(nameUpdate,emailUpdate)
          }
            else{
              viewModel.profileUpdateData(nameUpdate,emailUpdate,"",currentId)
          }

        }

        editImage=findViewById(R.id.editImage_CardView)
        editImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    200
                )
            } else {
                showImageSelectedOptions()
            }
        }

    }


    private fun showImageSelectedOptions() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(galleryIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

        startActivityForResult(chooserIntent, 200)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            showImageSelectedOptions()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }

}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            if (data?.data != null) {
                imageUri = data.data
                profileImageView.setImageURI(imageUri)
                updateImage=true
            } else if (data?.extras?.get("data") != null) {

                val bitmap = data.extras?.get("data") as Bitmap
                profileImageView.setImageBitmap(bitmap)
                imageUri = getImageUri(bitmap)
                updateImage=true
            }
        }

    }
    private fun getImageUri(inImage: Bitmap): Uri {
        val tempFile = File.createTempFile("temprentpk", ".png")
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }


    private fun uploadImage(name: String, email: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageref = storageRef.child("images/${System.currentTimeMillis()}.jpg")

        if (imageUri != null) {
            imageref.putFile(imageUri!!).addOnSuccessListener {
                imageref.downloadUrl.addOnSuccessListener { uri ->
                    viewModel.profileUpdateData(name, email, uri.toString(), currentId)
                    Toast.makeText(this, "Image update success", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}