package com.example.guruveda.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.VideoModel
import com.google.firebase.firestore.FirebaseFirestore

class VideoGetViewModel(): ViewModel() {
    val videoLiveData = MutableLiveData<List<VideoModel>?>()
    val db = FirebaseFirestore.getInstance()
    fun getVideos(courseId: String) {
        db.collection("courses").document(courseId).collection("videos").get()
            .addOnSuccessListener {
                val videoList = ArrayList<VideoModel>()
                for (document in it) {
                    val video = document.toObject(VideoModel::class.java)
                    videoList.add(video)
                }
                videoLiveData.value = videoList
            }
            .addOnFailureListener {
                videoLiveData.value = null
            }

    }
}