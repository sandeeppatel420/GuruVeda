package com.example.guruveda.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guruveda.DataModel.PdfModel
import com.example.guruveda.DataModel.TestSeriesDataModal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TestSeriesViewModel : ViewModel() {
    val _testSeriesList = MutableLiveData<List<TestSeriesDataModal>>()
    val  testSeriesList: LiveData<List<TestSeriesDataModal>> get() = _testSeriesList
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchTestSeriesData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {

                val snapshot = withContext(Dispatchers.IO) {
                    db.collection("teacher_tests_schedule")
                        .get()
                        .await()
                }

                val list = snapshot.map { document ->
                    document.toObject(TestSeriesDataModal::class.java)
                }
                _testSeriesList.value = list

            } catch (e: Exception) {
              _errorMessage.value="Failed to load PDFs. Check your internet connection."
            }
                finally {
                    _isLoading.value = false
                }
        }
    }
}
