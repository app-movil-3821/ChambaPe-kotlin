package com.example.chambape.data.mapper

import com.example.chambape.data.remote.dto.NotificationDto
import com.example.chambape.domain.model.Notification

fun NotificationDto.toDomain() = Notification(
    id        = id,
    userId    = userId,
    title     = title,
    message   = message,
    type      = type,
    read      = read,
    createdAt = createdAt
)
