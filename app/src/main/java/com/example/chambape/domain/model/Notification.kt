package com.example.chambape.domain.model

data class Notification(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: String,
    val read: Boolean,
    val createdAt: String
)
