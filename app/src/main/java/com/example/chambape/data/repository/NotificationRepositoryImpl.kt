package com.example.chambape.data.repository

import com.example.chambape.data.mapper.toDomain
import com.example.chambape.data.remote.service.NotificationService
import com.example.chambape.domain.model.Notification
import com.example.chambape.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val notificationService: NotificationService
) : NotificationRepository {

    override suspend fun getNotifications(userId: String): Result<List<Notification>> {
        return try {
            val notifications = notificationService.getNotifications(userId).map { it.toDomain() }
            Result.success(notifications)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUnreadNotifications(userId: String): Result<List<Notification>> {
        return try {
            val notifications = notificationService.getUnreadNotifications(userId).map { it.toDomain() }
            Result.success(notifications)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAsRead(notificationId: String): Result<Boolean> {
        return try {
            notificationService.markAsRead(notificationId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAllAsRead(userId: String): Result<Boolean> {
        return try {
            notificationService.markAllAsRead(userId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
