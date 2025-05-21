package com.example.guruveda.Notification

import java.util.UUID

data class NotificationModel(
    val message: MessageContent
)

data class MessageContent(
    val token: String? = null,
    val topic: String? = null,
    val notification: NotificationContent
)

data class NotificationContent(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val body: String,
    val timestamp: Long
)