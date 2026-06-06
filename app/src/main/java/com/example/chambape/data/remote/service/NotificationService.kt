package com.example.chambape.data.remote.service

import com.example.chambape.data.remote.dto.NotificationDto
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationService {
    @GET("notifications/user/{userId}")
    suspend fun getNotifications(@Path("userId") userId: String): List<NotificationDto>

    @GET("notifications/user/{userId}/unread")
    suspend fun getUnreadNotifications(@Path("userId") userId: String): List<NotificationDto>

    @PUT("notifications/{id}/read")
    suspend fun markAsRead(@Path("id") notificationId: String)

    @PUT("notifications/user/{userId}/read-all")
    suspend fun markAllAsRead(@Path("userId") userId: String)
}