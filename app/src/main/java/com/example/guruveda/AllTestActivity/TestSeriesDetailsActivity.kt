package com.example.guruveda.AllTestActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruveda.DataModel.TestQuestionModel
import com.example.guruveda.R
import com.example.guruveda.ViewModel.QuestionViewModel
import com.example.guruveda.allAdapter.QuestionAdapter
import com.example.guruveda.allAdapter.QuestionAnswerListener
import com.google.android.material.appbar.MaterialToolbar

class TestSeriesDetailsActivity: AppCompatActivity(), QuestionAnswerListener {

    private lateinit var imageView: ImageView
    private lateinit var subjectTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var questionRecyclerView: RecyclerView
    private lateinit var submitButton: AppCompatButton
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var questionList: ArrayList<TestQuestionModel>
    private val questionViewModel: QuestionViewModel by viewModels()
    private var imageUrl1: String? = null
    private var selectedAnswers: MutableList<Pair<String, String>> = mutableListOf()
    private lateinit var arrowBack: MaterialToolbar

    @SuppressLint("CheckResult", "MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_series_detail)

        imageView = findViewById(R.id.details_image)
        subjectTextView = findViewById(R.id.details_subject)
        titleTextView = findViewById(R.id.details_title)
        descriptionTextView = findViewById(R.id.details_description)
        durationTextView = findViewById(R.id.details_duration)
        submitButton = findViewById(R.id.submit_button)
        arrowBack = findViewById(R.id.topAppBar)
        arrowBack.setNavigationOnClickListener {
            onBackPressed()
        }

        val imageUrl = intent.getStringExtra("imageUrl")
        imageUrl1 = imageUrl

        submitButton.setOnClickListener {
            if (selectedAnswers.isNotEmpty()) {
                val subject = subjectTextView.text.toString()
                val title = titleTextView.text.toString()
                val description = descriptionTextView.text.toString()
                val duration = durationTextView.text.toString()

                val intent = Intent(this, AnswerDetailsActivity::class.java)
                intent.putExtra("subject", subject)
                intent.putExtra("title", title)
                intent.putExtra("description", description)
                intent.putExtra("duration", duration)
                intent.putExtra("imageUrl", imageUrl1)

                // Pass the selected answers as a formatted string
                intent.putExtra("selectedAnswer", selectedAnswers.joinToString("\n\n") { "Question: ${it.first}\nAnswer: ${it.second}"})
                startActivity(intent)
            } else {

                Toast.makeText(this, "Please select an answer for each question.", Toast.LENGTH_SHORT).show()
            }
        }

        val subject = intent.getStringExtra("subject")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val duration = intent.getStringExtra("duration")

        subjectTextView.text = subject
        titleTextView.text = title
        descriptionTextView.text = description
        durationTextView.text = duration
        Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.edit_24).into(imageView)

        setStatusBar()

        questionList = ArrayList()
        questionRecyclerView = findViewById(R.id.questionOptions_recyclerView)
        questionRecyclerView.layoutManager = LinearLayoutManager(this)
        questionAdapter = QuestionAdapter(questionList, this)
        questionRecyclerView.adapter = questionAdapter
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onAnswerSelected(questionId: String, answer: String, index: Int, isSelected:Boolean) {


        if (selectedAnswers.any { it.first == "Q$index" }) {
            selectedAnswers.removeAll { it.first == "Q$index" }
            questionAdapter.notifyDataSetChanged()
            questionViewModel.updateSelectedAnswer(questionId, selectedAnswers.toString())
        } else {
            selectedAnswers.add(Pair("Q$index", answer))
            questionAdapter.notifyDataSetChanged()
            questionViewModel.updateSelectedAnswer(questionId, selectedAnswers.toString())

        }

        if (!isSelected){
            selectedAnswers.removeAll { it.first == "Q$index" }
            questionAdapter.notifyDataSetChanged()
            questionViewModel.updateSelectedAnswer(questionId, selectedAnswers.toString())
            return
        }
    }


}
