package com.example.guruveda

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class TestSeriesDetailsActivity : AppCompatActivity() {
    @SuppressLint("CheckResult", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texst_series_detail)

        val imageView = findViewById<ImageView>(R.id.details_image)
        val subjectTextView = findViewById<TextView>(R.id.details_subject)
        val titleTextView = findViewById<TextView>(R.id.details_title)
        val descriptionTextView = findViewById<TextView>(R.id.details_description)
        val durationTextView = findViewById<TextView>(R.id.details_duration)


        val subject = intent.getStringExtra("subject")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val duration = intent.getStringExtra("duration")
        val imageUrl = intent.getStringExtra("imageUrl")

        subjectTextView.text = subject
        titleTextView.text = title
        descriptionTextView.text = description
        durationTextView.text = duration
        Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.edit_24).into(imageView)

        setStatusBar()

    }
    private fun setStatusBar() {
        val statusBarColor = ContextCompat.getColor(this, R.color.black2)
        window.statusBarColor = statusBarColor
    }

}