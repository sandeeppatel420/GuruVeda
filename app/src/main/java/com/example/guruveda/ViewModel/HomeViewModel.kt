package com.example.guruveda.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.CourseModel
import com.example.guruveda.DataModel.FreeVideosDataModel
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _recommendedCoursesList = MutableLiveData<List<CourseModel>>()
    val courses: LiveData<List<CourseModel>> get() = _recommendedCoursesList

    private val _freeVideosList = MutableLiveData<List<FreeVideosDataModel>>()
    val freeVideosList: LiveData<List<FreeVideosDataModel>> get() = _freeVideosList

    fun getAllCourses() {
        db.collection("courses").get().addOnSuccessListener {
            val tempList = ArrayList<CourseModel>()
            for (document in it.documents) {
                val course = document.toObject(CourseModel::class.java)
                course?.let { tempList.add(it) }
            }
            _recommendedCoursesList.value = tempList
        }
    }

    fun getSelectedCourses(courseTitle: String) {
        db.collection("courses").whereEqualTo("courseTitle", courseTitle).get()
            .addOnSuccessListener {
                val tempList = ArrayList<CourseModel>()
                for (document in it.documents) {
                    val course = document.toObject(CourseModel::class.java)
                    course?.let { tempList.add(it) }
                }
                _recommendedCoursesList.value = tempList
            }
    }

    fun getAllFreeVideos() {
        db.collection("videos").get().addOnSuccessListener {
            val tempList = ArrayList<FreeVideosDataModel>()
            for (document in it.documents) {
                val video = document.toObject(FreeVideosDataModel::class.java)
                video?.let { tempList.add(it) }
            }
            _freeVideosList.value = tempList
        }
    }

    fun getFreeVideosByType(type: String) {
        db.collection("videos").whereEqualTo("freeVideo", type).get()
            .addOnSuccessListener {
                val tempList = ArrayList<FreeVideosDataModel>()
                for (document in it.documents) {
                    val video = document.toObject(FreeVideosDataModel::class.java)
                    video?.let { tempList.add(it) }
                }
                _freeVideosList.value = tempList
            }
    }
}