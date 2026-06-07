package com.example.chambape.data.remote.dto



data class ConversationDto(
    val id: String,
    val jobId: String,
    val enrollmentId: String,
    val contractorId: String,
    val workerId: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)


data class MessageDto(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val content: String,
    val sentAt: String,
    val read: Boolean = false,
    val readAt: String? = null
)


data class SendMessageRequest(
    val senderId: String,
    val content: String
)