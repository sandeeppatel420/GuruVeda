package com.example.guruveda.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.DoubtModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class DoubtViewModel: ViewModel() {
    val auth = FirebaseAuth.getInstance()
    val doubtList = mutableListOf<DoubtModel>()
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    fun addDoubt(subjectName: String, doubtMessage: String, imageUri: Uri?, onResult: (Boolean) -> Unit) {
        if (imageUri != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val storageRef = storage.reference.child("doubts/$fileName")

            storageRef.putFile(imageUri).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val doubt = DoubtModel(
                        subjectName = subjectName,
                        doubtMessage = doubtMessage,
                        imageUrl = uri.toString(),
                        userId = auth.currentUser?.uid
                    )

                    db.collection("doubts").add(doubt)
                        .addOnSuccessListener {

                            doubtList.add(doubt)
                            onResult(true)
                        }
                        .addOnFailureListener { onResult(false) }
                }
            }.addOnFailureListener {
                onResult(false)
            }
        } else {
            // Agar image nahi hai
            val doubt = DoubtModel(
                subjectName = subjectName,
                doubtMessage = doubtMessage,
                imageUrl = ""
            )
            db.collection("=doubts").add(doubt)
                .addOnSuccessListener {
                    doubtList.add(doubt)
                    onResult(true)
                }
                .addOnFailureListener { onResult(false) }
        }
    }
}

