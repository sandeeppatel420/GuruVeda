package com.example.guruveda

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class AnswerDetailsActivity : AppCompatActivity() {
    private lateinit var answerImageView: ImageView
    private lateinit var answerSubjectTextView: TextView
    private lateinit var answerTitleTextView: TextView
    private lateinit var answerDescriptionTextView: TextView
    private lateinit var answerDurationTextView: TextView
    private lateinit var answerAnswersTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)
        answerImageView = findViewById(R.id.answerDetails_ImageView)
        answerSubjectTextView = findViewById(R.id.answerSubjectDetail_TextView)
        answerTitleTextView = findViewById(R.id.answerTitleDetail_TextView)
        answerDescriptionTextView = findViewById(R.id.answerDescriptionDetail_TextView)
        answerDurationTextView = findViewById(R.id.answerDurationDetail_TextView)
        answerAnswersTextView = findViewById(R.id.answersTextView)

        val subject = intent.getStringExtra("subject")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val duration = intent.getStringExtra("duration")
        val imageUrl = intent.getStringExtra("imageUrl")
        val selectedAnswer = intent.getStringExtra("selectedAnswer")



        answerSubjectTextView.text = subject
        answerTitleTextView.text = title
        answerDescriptionTextView.text = description
        answerDurationTextView.text = duration
        answerAnswersTextView.text = selectedAnswer
        Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.edit_24).into(answerImageView)


    }
}