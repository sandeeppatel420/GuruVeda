package com.example.guruveda.allAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guruveda.DataModel.TestQuestionModel
import com.example.guruveda.R

class QuestionAdapter(
    private val questionList: ArrayList<TestQuestionModel>,
    private var questionAnswerListener: QuestionAnswerListener
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    private var lastCheckedButton: RadioButton? = null
    private var selectedAnswers: MutableList<Pair<String, String>> = mutableListOf()

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val rbOptionA: RadioButton = itemView.findViewById(R.id.rbOptionA)
        val rbOptionB: RadioButton = itemView.findViewById(R.id.rbOptionB)
        val rbOptionC: RadioButton = itemView.findViewById(R.id.rbOptionC)
        val rbOptionD: RadioButton = itemView.findViewById(R.id.rbOptionD)
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
                    selectedAnswers.removeAll { it.first == "Q$position" }
                    questionAnswerListener.onAnswerSelected(question.questionId ?: "", "", position) // Send empty answer
                } else {
                    radioButton.forEach { it.isChecked = false }
                    rb.isChecked = true
                    lastCheckedButton = rb
                    selectedAnswers.removeAll { it.first == "Q$position" }
                    selectedAnswers.add(Pair("Q$position", rb.text.toString()))
                    questionAnswerListener.onAnswerSelected(question.questionId ?: "", rb.text.toString(), position)
                }
            }
        }
    }

}
interface QuestionAnswerListener {
    fun onAnswerSelected(questionId: String, answer: String, index: Int)
}
