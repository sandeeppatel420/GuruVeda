package com.example.guruveda.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.AuthModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileUpdateViewModel:ViewModel() {
    val updateProfile= MutableLiveData<AuthModel>()
    val db= FirebaseDatabase.getInstance()
    val auth=FirebaseAuth.getInstance()

    fun profileUpdateData(name:String,email:String,image:String,userId: String){
       val updates=HashMap<String,Any>()
          updates["name"]=name
          updates["email"]=email
          updates["userId"]=userId
         if (image!=""){
             updates["imageProfile"]=image
         }

        db.getReference("Users1").child(userId).updateChildren(updates)
            .addOnSuccessListener {
               updateProfile.value=AuthModel(name,email)
            }
            .addOnFailureListener {
                Log.e("AuthViewModel", "Database Error: ${it.message}")
            }
    }
}