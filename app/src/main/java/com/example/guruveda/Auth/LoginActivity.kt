package com.example.guruveda.Auth

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.guruveda.MainActivity
import com.example.guruveda.ViewModel.AuthViewModel
import com.example.guruveda.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var userViewModel: AuthViewModel
    private lateinit var progressDialog: ProgressDialog
    val auth=FirebaseAuth.getInstance()
    private var userEmail:String=""
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signUp.setOnClickListener {
         startActivity(Intent(this,SignupActivity::class.java))
            finish()
        }

        progressDialog = ProgressDialog(this).apply {
            setMessage("Logging in...")
            setCancelable(false)
        }

        userViewModel= ViewModelProvider(this)[AuthViewModel::class.java]
        binding.loginBtn.setOnClickListener {
              userEmail = binding.emailLogin.text.toString()
            val userPassword = binding.passwordLogin.text.toString()

            when {
                userEmail.isEmpty() -> showToast("Enter an email")
                !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches() -> showToast("Enter a valid email")
                userPassword.isEmpty() -> showToast("Enter a password")
                else -> {
                    progressDialog.show()
                    userViewModel.login(userEmail, userPassword)
                }
            }
        }


        userViewModel.loginStatus.observe(this) { result ->
            progressDialog.dismiss()
            when (result) {
                "success" -> {
                    showToast("Login Successful")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                "Email not registered" -> showToast("Email not registered")
                "Password is incorrect" -> showToast("Incorrect password")
                "Invalid email format" -> showToast("Invalid email format")
                "User account is disabled" -> showToast("User account is disabled")
                else -> showToast(result ?: "Login Failed")
            }
        }


    }
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


}