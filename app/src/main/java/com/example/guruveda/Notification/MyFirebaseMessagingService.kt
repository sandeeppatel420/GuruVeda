package com.example.guruveda.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.guruveda.Notification.NotificationActivity
import com.example.guruveda.Notification.NotificationContent
import com.example.guruveda.Notification.NotificationRepository
import com.example.guruveda.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val CHANNEL_ID = "FCM_CHANNEL"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: ""
        val body = remoteMessage.notification?.body ?: ""

        // Store the notification
        val notificationItem = NotificationContent(
            title = title,
            body = body,
            timestamp = System.currentTimeMillis()
        )
        NotificationRepository.addNotification(notificationItem)

        showNotification(title, body)
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Firebase Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Firebase push notifications"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String, message: String) {
        // Intent to open NotificationActivity
        val intent = Intent(this, NotificationActivity::class.java).apply {
            putExtra("notification_title", title)
            putExtra("notification_body", message)
            putExtra("notification_timestamp", System.currentTimeMillis())
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(NotificationManager::class.java)
        createNotificationChannelIfNeeded()
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}