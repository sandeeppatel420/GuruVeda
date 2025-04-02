package com.example.guruveda.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.AuthModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {
    val users = MutableLiveData<AuthModel>()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()

    fun register(name: String, email: String, phoneNumber: String, confirmPassword: String) {
        auth.createUserWithEmailAndPassword(email, confirmPassword)
            .addOnSuccessListener {
                val userId = auth.currentUser
                userId?.let {
                    val data = mapOf(
                        "name" to name,
                        "email" to email,
                        "phoneNumber" to phoneNumber,
                        "userId" to it.uid,
                        "imageProfile" to "https://firebasestorage.googleapis.com/v0/b/onlyrealtimedata.appspot.com/o/image%2Fprofile1.png?alt=media&token=183f368e-8d5a-4ab6-bb16-8e6ad657ec57"
                    )
                    db.getReference("Users1").child(it.uid).setValue(data)
                        .addOnSuccessListener {
                            users.value = AuthModel(name, email, phoneNumber, confirmPassword)
                        }
                        .addOnFailureListener { error ->
                            Log.e("AuthViewModel", "Database Error: ${error.message}")
                        }
                }
            }
            .addOnFailureListener { error ->
                Log.e("AuthViewModel", "Auth Error: ${error.message}")
            }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                users.value = AuthModel(user?.displayName ?: "", user?.email ?: "")
            }
            .addOnFailureListener { error ->
                Log.e("AuthViewModel", "Login Error: ${error.message}")
            }
    }
}

