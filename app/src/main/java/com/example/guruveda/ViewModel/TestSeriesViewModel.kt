package com.example.guruveda.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.TestSeriesDataModal
import com.google.firebase.firestore.FirebaseFirestore

class TestSeriesViewModel : ViewModel() {
    val testSeriesList = MutableLiveData<List<TestSeriesDataModal>>()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun fetchTestSeriesData() {
        db.collection("teacher_tests_schedule").get().addOnSuccessListener { result ->
            val tempList = ArrayList<TestSeriesDataModal>()
            for (document in result) {
                val testSeries = document.toObject(TestSeriesDataModal::class.java)
                tempList.add(testSeries)
            }
            testSeriesList.value = tempList
        }.addOnFailureListener {
            // Optional: Add error handling here
        }
    }
}
