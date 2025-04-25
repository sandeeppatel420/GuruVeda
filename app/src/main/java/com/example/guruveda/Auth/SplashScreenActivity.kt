package com.example.guruveda.Auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.guruveda.MainActivity
import com.example.guruveda.R
import com.example.guruveda.databinding.ActivitySplashSreenBinding
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashSreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashSreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth= FirebaseAuth.getInstance()
        val currentUser=auth.currentUser
        Handler(Looper.getMainLooper()).postDelayed({

            if (currentUser!=null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }

        }, 2000)
     setStatusBar()
    }
    private fun setStatusBar() {
        val statusBarColor = ContextCompat.getColor(this, R.color.black2)
        window.statusBarColor = statusBarColor
    }
}