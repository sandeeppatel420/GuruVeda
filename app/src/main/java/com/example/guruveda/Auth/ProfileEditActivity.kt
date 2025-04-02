package com.example.guruveda.Auth

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.guruveda.R

class ProfileEditActivity : AppCompatActivity() {
    lateinit var nameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var profileImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)



        nameEditText=findViewById(R.id.profileName_EditText)
        emailEditText=findViewById(R.id.profileEmail_EditText)
        profileImageView=findViewById(R.id.ProfileImage_ImageView)

        val nameData=intent.getStringExtra("name")
        val emailData=intent.getStringExtra("email")
        val imageData=intent.getStringExtra("imageProfile")

       nameEditText.setText(nameData)
        emailEditText.setText(emailData)
        Glide.with(this).load(imageData).into(profileImageView)

    }
}