package com.example.guruveda.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.CourseModel
import com.google.firebase.firestore.FirebaseFirestore

class AllCouresGet:ViewModel(){
    val listLiveData = MutableLiveData<List<CourseModel>>()
    val firestore = FirebaseFirestore.getInstance()

    fun getCoures(){
   firestore.collection("courses").get()
       .addOnSuccessListener {
           val courseList = ArrayList<CourseModel>()
           for (document in it.documents) {
               val course = document.toObject(CourseModel::class.java)
               course?.let { courseList.add(it) }
           }
           listLiveData.value = courseList
       }
       .addOnFailureListener {
           Log.e("FirestoreViewModel", "Error getting document", it)
       }
   }
    val myCoursesLiveData = MutableLiveData<List<CourseModel>>()
    fun getMyCourses(userId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("myCourses")
            .get()
            .addOnSuccessListener { documents ->
                val courseList = ArrayList<CourseModel>()
                val courseIds = documents.mapNotNull { it.getString("courseId") }

                firestore.collection("courses")
                    .whereIn("courseId", courseIds)
                    .get()
                    .addOnSuccessListener { courseDocs ->
                        for (doc in courseDocs.documents) {
                            val course = doc.toObject(CourseModel::class.java)
                            course?.let { courseList.add(it) }
                        }
                        myCoursesLiveData.value = courseList
                    }
            }
            .addOnFailureListener {
                Log.e("MyCourses", "Failed to load my courses", it)
            }
    }

}