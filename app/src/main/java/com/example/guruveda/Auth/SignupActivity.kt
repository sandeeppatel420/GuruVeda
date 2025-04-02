package com.example.guruveda.Auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.guruveda.MainActivity
import com.example.guruveda.ViewModel.AuthViewModel
import com.example.guruveda.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding

    lateinit var userViewModel: AuthViewModel
    private lateinit var progressDialog: ProgressDialog

    private lateinit var userViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing Up...")
        progressDialog.setCancelable(false)
        userViewModel= ViewModelProvider(this)[AuthViewModel::class.java]
        binding.signupBtn.setOnClickListener {
            val userName=binding.signUpName.text.toString()
            val userEmail=binding.signUpEmail.text.toString()
            val userPhone=binding.signUpPhone.text.toString()
            val userPassword=binding.signUpConPassword.text.toString()

            if (userName==""){
                Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show()
            }
            else if (userEmail==""){
                Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show()
            }
            else if (userPhone==""){
                Toast.makeText(this, "Please Enter PhoneNumber", Toast.LENGTH_SHORT).show()
            }
            else if (userPassword==""){
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
            }
            else{
                progressDialog.show()
             userViewModel.register(userName,userEmail,userPhone,userPassword)
            }
        }

        userViewModel.users.observe(this) {
            progressDialog.dismiss()
            if (it!=null) {
                Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}