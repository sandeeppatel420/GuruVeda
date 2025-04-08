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
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    lateinit var userViewModel: AuthViewModel
    private lateinit var progressDialog: ProgressDialog
    val auth=FirebaseAuth.getInstance()
    val db=FirebaseDatabase.getInstance()
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
            val userEmail=binding.emailLogin.text.toString()
            val userPassword=binding.passwordLogin.text.toString()


            if (userEmail=="") {
                showToast("Enter a  email")

            }
            else if (userPassword==""){
                showToast("Enter a  password")
            }
            else{
                progressDialog.show()
                userViewModel.login(userEmail, userPassword)
            }
            }


        userViewModel.users.observe(this) {
            progressDialog.dismiss()
            if (it!=null) {
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

//    private fun isValidPassword(password: String): Boolean {
//        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
//        return password.matches(Regex(passwordPattern))
//    }

}