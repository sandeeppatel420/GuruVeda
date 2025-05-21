package com.example.guruveda.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guruveda.DataModel.VideoModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FreeVideoGet : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _videoLiveDataFree = MutableLiveData<List<VideoModel>?>()
    val videoLiveDataFree: LiveData<List<VideoModel>?> get() = _videoLiveDataFree

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getVideos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = withContext(Dispatchers.IO) {
                    db.collection("videos").whereEqualTo("type", "Free").get().await()

                }
                val videoList = ArrayList<VideoModel>()
                for (document in snapshot.documents) {
                    val video = document.toObject(VideoModel::class.java)
                    video?.let {
                        videoList.add(it)
                    }
                    _videoLiveDataFree.value = videoList
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load videos: ${e.message}"
            } finally {
                _isLoading.value = false



            }


        }
    }
}