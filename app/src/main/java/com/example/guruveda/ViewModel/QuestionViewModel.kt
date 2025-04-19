package com.example.guruveda.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.TestQuestionModel
import com.google.firebase.firestore.FirebaseFirestore

class QuestionViewModel : ViewModel() {
    val questionList = MutableLiveData<List<TestQuestionModel>>()
    private val db = FirebaseFirestore.getInstance()

    fun fetchQuestionData() {
        db.collection("teacher_tests_question").get()
            .addOnSuccessListener { result ->
                val tempList = ArrayList<TestQuestionModel>()
                for (document in result) {
                    val question = document.toObject(TestQuestionModel::class.java)
                    tempList.add(question)
                }
                questionList.value = tempList
            }.addOnFailureListener {

            }
    }
}