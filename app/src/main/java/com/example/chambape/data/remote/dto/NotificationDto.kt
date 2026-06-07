package com.example.chambape.data.remote.dto


data class NotificationDto(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: String,
    val read: Boolean = false,
    val createdAt: String,
    val readAt: String? = null
)