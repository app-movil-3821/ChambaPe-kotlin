package com.example.chambape.data.mapper

import com.example.chambape.data.remote.dto.MessageDto
import com.example.chambape.domain.model.Message

fun MessageDto.toDomain() = Message(
    id             = id,
    conversationId = conversationId,
    senderId       = senderId,
    content        = content,
    sentAt         = sentAt,
    read           = read
)