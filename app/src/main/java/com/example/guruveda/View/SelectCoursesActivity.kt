package com.example.guruveda.View

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guruveda.MainActivity
import com.example.guruveda.R

class SelectCoursesActivity : AppCompatActivity() {
    lateinit var dartCardView: CardView
    lateinit var mySqlCardView: CardView
    lateinit var kotlinCardView: CardView
    lateinit var javaCardView: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select_courses)

        dartCardView = findViewById(R.id.dart_selected)
        mySqlCardView = findViewById(R.id.sql_selected)
        kotlinCardView = findViewById(R.id.kotlin_selected)
        javaCardView = findViewById(R.id.java_selected)

        dartCardView.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("courses", "Flutter")
            intent.putExtra("freeVideo", "Free")
            startActivity(intent)
            finish()

        }

    }
}