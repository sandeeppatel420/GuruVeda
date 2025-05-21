package com.example.guruveda.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.guruveda.DataModel.DoubtModel
import com.example.guruveda.Notification.NotificationModel
import com.example.guruveda.Notification.MessageContent
import com.example.guruveda.Notification.NotificationContent
import com.example.guruveda.Notification.ServerKey
import com.example.guruveda.Notification.SeverObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class DoubtViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val doubtList = mutableListOf<DoubtModel>()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun addDoubt(
        subjectName: String,
        doubtMessage: String,
        imageUri: Uri?,
        context: Context,
        onResult: (Boolean) -> Unit
    ) {
        // Notification bhejne ka function
        fun sendTeacherNotification(serverKey: String) {
            val notificationMessage = NotificationModel(
                message = MessageContent(
                    topic = "teachers", // Teachers topic pe bhejo
                    notification = NotificationContent(
                        title = "Doubt in $subjectName", // Subject as title
                        body = doubtMessage, // Doubt message as body
                        timestamp = System.currentTimeMillis()
                    )
                )
            )

            SeverObject.retrofit.sendNotification("Bearer $serverKey", notificationMessage)
                .enqueue(object : Callback<NotificationModel> {
                    override fun onResponse(
                        call: Call<NotificationModel>,
                        response: Response<NotificationModel>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("SendNotification", "Teacher ko doubt ka notification chala gaya")
                        } else {
                            Log.e("SendNotification", "Notification nahi gaya: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<NotificationModel>, t: Throwable) {
                        Log.e("SendNotification", "Error aaya: ${t.message}")
                    }
                })
        }

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

                            // Server key lo aur notification bhejo
                            ServerKey(context).getAccessToken { token ->
                                if (token != null) {
                                    sendTeacherNotification(token)
                                } else {
                                    Log.e("AccessToken", "Server key nahi mila")
                                }
                            }
                        }
                        .addOnFailureListener { onResult(false) }
                }
            }.addOnFailureListener {
                onResult(false)
            }
        } else {
            val doubt = DoubtModel(
                subjectName = subjectName,
                doubtMessage = doubtMessage,
                imageUrl = "",
                userId = auth.currentUser?.uid
            )
            db.collection("doubts").add(doubt)
                .addOnSuccessListener {
                    doubtList.add(doubt)
                    onResult(true)

                    // Server key lo aur notification bhejo
                    ServerKey(context).getAccessToken { token ->
                        if (token != null) {
                            sendTeacherNotification(token)
                        } else {
                            Log.e("AccessToken", "Server key nahi mila")
                        }
                    }
                }
                .addOnFailureListener { onResult(false) }
        }
    }
}