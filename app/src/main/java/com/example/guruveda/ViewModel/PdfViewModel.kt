package com.example.guruveda.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guruveda.DataModel.PdfModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PdfViewModel:ViewModel() {

    private val _pdfList = MutableLiveData<List<PdfModel>>()
    val pdfList: LiveData<List<PdfModel>> get() = _pdfList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchPdfData() {
      viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = withContext(Dispatchers.IO) {
                    FirebaseFirestore.getInstance()
                        .collection("pdfs")
                        .get()
                        .await()
                }
                val list = snapshot.map { it.toObject(PdfModel::class.java) }
                _pdfList.value = list
            }
            catch (e: Exception) {
               _errorMessage.value="Failed to load PDFs. Check your internet connection."
            }
            finally {
                _isLoading.value = false
            }
        }
    }

}

