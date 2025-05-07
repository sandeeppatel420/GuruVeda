package com.example.guruveda.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guruveda.DataModel.CourseModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AllCourseViewModel: ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _courses = MutableLiveData<List<CourseModel>>()
    val courses: LiveData<List<CourseModel>> = _courses
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    fun getCourse() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = withContext(Dispatchers.IO) {
                    firestore.collection("courses").get().await()
                }

                val courseList = ArrayList<CourseModel>()
                for (document in snapshot.documents) {
                    val course = document.toObject(CourseModel::class.java)
                    course?.let {
                        courseList.add(it)
                    }
                }

                _courses.value = courseList  // ✅ LiveData update
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load courses: ${e.message}"
            } finally {
                _isLoading.value = false
            }
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
