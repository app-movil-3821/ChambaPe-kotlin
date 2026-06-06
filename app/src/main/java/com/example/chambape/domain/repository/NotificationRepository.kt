package com.example.chambape.domain.repository

import com.example.chambape.domain.model.Notification

interface NotificationRepository {
    suspend fun getNotifications(userId: String): Result<List<Notification>>
    suspend fun getUnreadNotifications(userId: String): Result<List<Notification>>
    suspend fun markAsRead(notificationId: String): Result<Boolean>
    suspend fun markAllAsRead(userId: String): Result<Boolean>
}
