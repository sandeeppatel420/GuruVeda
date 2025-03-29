package com.example.guruveda.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.AuthModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel:ViewModel() {

    val users= MutableLiveData<AuthModel>()
    val auth=FirebaseAuth.getInstance()
    val db= FirebaseDatabase.getInstance()

    fun register(name: String, email: String,phoneNumber:String, conformPassword: String) {
        auth.createUserWithEmailAndPassword(email, conformPassword)
            .addOnSuccessListener {
                val userId = auth.currentUser
                if (userId != null) {
                    users.value = AuthModel(name, email)
                    val data = HashMap<String, Any>()
                    data["name"] = name
                    data["email"] = email
                    data["phoneNumber"] = phoneNumber
                    data["conformPassword"] = email
                    data["userId"] = userId
                    data["imageProfile"] = "https://firebasestorage.googleapis.com/v0/b/onlyrealtimedata.appspot.com/o/image%2Fprofile1.png?alt=media&token=183f368e-8d5a-4ab6-bb16-8e6ad657ec57"
                    db.getReference("Users1").child(userId.uid).setValue(data)
                }
            }
            .addOnFailureListener {

            }
    }
}