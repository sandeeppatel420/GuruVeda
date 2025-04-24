package com.example.guruveda.allAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.guruveda.DataModel.TestQuestionModel
import com.example.guruveda.MainActivity
import com.example.guruveda.R
import com.example.guruveda.ViewModel.QuestionViewModel

class QuestionAdapter(private val questionList: ArrayList<TestQuestionModel>, var questionViewModel: QuestionViewModel) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
    private var lastCheckedButton: RadioButton? = null

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val rbOptionA: RadioButton = itemView.findViewById(R.id.rbOptionA)
        val rbOptionB: RadioButton = itemView.findViewById(R.id.rbOptionB)
        val rbOptionC: RadioButton = itemView.findViewById(R.id.rbOptionC)
        val rbOptionD: RadioButton = itemView.findViewById(R.id.rbOptionD)
        val radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroupOptions)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_test_question, parent, false)
        return QuestionViewHolder(view)

    }

    override fun getItemCount(): Int {
        return questionList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]
        holder.tvQuestion.text = "Q${position + 1}. ${question.question}"
        holder.rbOptionA.text = question.optionA
        holder.rbOptionB.text = question.optionB
        holder.rbOptionC.text = question.optionC
        holder.rbOptionD.text = question.optionD



        val radioButton = arrayOf(
            holder.rbOptionA,
            holder.rbOptionB,
            holder.rbOptionC,
            holder.rbOptionD

        )
        for (rb in radioButton) {
            rb.setOnClickListener {
                if (rb == lastCheckedButton) {
                    rb.isChecked = false
                    lastCheckedButton = null
                } else {
                    radioButton.forEach { it.isChecked = false }
                    rb.isChecked = true
                    lastCheckedButton = rb
                }

            }
        }
//        questionViewModel=ViewModelProvider(holder.itemView.context as MainActivity).get(QuestionViewModel::class.java)
//        questionViewModel.updateSelectedAnswer(question.questionId ?: "", lastCheckedButton?.text.toString())

    }


}