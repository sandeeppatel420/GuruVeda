package com.example.guruveda.AllActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.guruveda.databinding.ActivitySplashSreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashSreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashSreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}