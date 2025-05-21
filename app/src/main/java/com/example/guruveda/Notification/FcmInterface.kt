package com.example.guruveda.Notification

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmInterface {
    @POST("v1/projects/training-278dd/messages:send")
    fun sendNotification(
        @Header("Authorization") authorization: String,
        @Body message: NotificationModel
    ): Call<NotificationModel>
}

object SeverObject {
    val baseUri = "https://fcm.googleapis.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUri)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FcmInterface::class.java)
}
object NotificationRepository {
    private val notificationList = mutableListOf<NotificationContent>()

    fun getNotifications(): List<NotificationContent> = notificationList

    fun addNotification(notification: NotificationContent) {
        notificationList.add(0, notification)
    }

    fun removeNotification(id: String) {
        notificationList.removeAll { it.id == id }
    }

    fun clear() {
        notificationList.clear()
    }
}
