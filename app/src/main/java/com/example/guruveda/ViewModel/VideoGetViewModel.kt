package com.example.guruveda.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.VideoModel
import com.google.firebase.firestore.FirebaseFirestore

class VideoGetViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _videoLiveData = MutableLiveData<List<VideoModel>?>()
    val videoLiveData: LiveData<List<VideoModel>?> get() = _videoLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getVideos(courseId: String) {
        _isLoading.value = true // Show progress bar before data is loaded

        try {
            db.collection("courses")
                .document(courseId)
                .collection("videos")
                .get()
                .addOnSuccessListener { result ->
                    val videoList = ArrayList<VideoModel>()
                    for (document in result) {
                        val video = document.toObject(VideoModel::class.java)
                        videoList.add(video)
                    }
                    _videoLiveData.value = videoList // Set the videos to LiveData
                    _isLoading.value = false // Hide progress bar after loading is complete
                }
                .addOnFailureListener { e ->
                    _videoLiveData.value = null
                    _errorMessage.value = "Failed to load videos: ${e.message}"
                }
        } catch (e: Exception) {
            _errorMessage.value = "Unexpected error: ${e.message}"
        }
    }
}
