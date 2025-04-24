package com.example.guruveda.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.VideoModel
import com.google.firebase.firestore.FirebaseFirestore

class FreeVideoGet(): ViewModel() {
    val videoLiveDataFree = MutableLiveData<List<VideoModel>?>()
    val db = FirebaseFirestore.getInstance()

    fun getVideos() {
        db.collection("videos").whereEqualTo("type", "Free")
            .get()
            .addOnSuccessListener {
                val videoList = ArrayList<VideoModel>()
                for (document in it) {
                    val video = document.toObject(VideoModel::class.java)
                    videoList.add(video)
                }
                videoLiveDataFree.value = videoList
            }
            .addOnFailureListener {exception ->
                videoLiveDataFree.value = null
            }
    }
}