package com.example.guruveda.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.CourseModel
import com.google.firebase.firestore.FirebaseFirestore

class AllCourseViewModel: ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _listLiveData = MutableLiveData<List<CourseModel>>()
    val listLiveData: LiveData<List<CourseModel>> get() = _listLiveData
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    fun getCourse() {
        _isLoading.value = true
        try {
            firestore.collection("courses").get()
                .addOnSuccessListener { result ->
                    val courseList = ArrayList<CourseModel>()
                    for (document in result.documents) {
                        val course = document.toObject(CourseModel::class.java)
                        course?.let { courseList.add(it) }
                    }
                    _listLiveData.value = courseList
                    _isLoading.value = false
                }
                .addOnFailureListener { exception ->
                    _errorMessage.value = "Failed to load courses: ${exception.message}"
                    Log.e("FirestoreViewModel", "Error getting document", exception)
                }
        } catch (e: Exception) {
            _errorMessage.value = "Unexpected error: ${e.message}"
            Log.e("FirestoreViewModel", "Unexpected error", e)
        }
    }


    private val _myCoursesLiveData = MutableLiveData<List<CourseModel>>()
    val myCoursesLiveData: LiveData<List<CourseModel>> get() = _myCoursesLiveData

    fun getMyCourses(userId: String) {
        _isLoading.value = true

        firestore.collection("users")
            .document(userId)
            .collection("myCourses")
            .get()
            .addOnSuccessListener { documents ->
                val courseIds = documents.mapNotNull { it.getString("courseId") }
                firestore.collection("courses")
                    .whereIn("courseId", courseIds)
                    .get()
                    .addOnSuccessListener { courseDocs ->
                        val courseList = ArrayList<CourseModel>()
                        for (doc in courseDocs.documents) {
                            val course = doc.toObject(CourseModel::class.java)
                            course?.let { courseList.add(it) }
                        }
                        _myCoursesLiveData.value = courseList
                        _isLoading.value = false
                    }
                    .addOnFailureListener {
                        _errorMessage.value = "Failed to load course details: ${it.message}"
                        _isLoading.value = false
                    }
            }
            .addOnFailureListener {
                _errorMessage.value = "Failed to load my courses: ${it.message}"
                _isLoading.value = false
            }
    }
}
