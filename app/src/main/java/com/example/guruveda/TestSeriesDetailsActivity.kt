package com.example.guruveda

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruveda.DataModel.TestQuestionModel
import com.example.guruveda.ViewModel.QuestionViewModel
import com.example.guruveda.allAdapter.QuestionAdapter

class TestSeriesDetailsActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var subjectTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var questionRecyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var questionList: ArrayList<TestQuestionModel>
    private lateinit var questionViewModel: QuestionViewModel

    @SuppressLint("CheckResult", "MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texst_series_detail)

        imageView = findViewById(R.id.details_image)
        subjectTextView = findViewById(R.id.details_subject)
        titleTextView = findViewById(R.id.details_title)
        descriptionTextView = findViewById(R.id.details_description)
        durationTextView = findViewById(R.id.details_duration)


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

        questionList = ArrayList()
        questionRecyclerView = findViewById(R.id.questionOptions_recyclerView)
        questionRecyclerView.layoutManager = LinearLayoutManager(this)
        questionAdapter = QuestionAdapter(questionList)
        questionRecyclerView.adapter = questionAdapter
        questionViewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)
        questionViewModel.fetchQuestionData()
        questionViewModel.questionList.observe(this) {
            questionList.clear()
            questionList.addAll(it)
            questionAdapter.notifyDataSetChanged()
        }
    }

    private fun setStatusBar() {
        val statusBarColor = ContextCompat.getColor(this, R.color.black2)
        window.statusBarColor = statusBarColor
    }

}