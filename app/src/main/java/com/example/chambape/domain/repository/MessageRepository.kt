package com.example.chambape.domain.repository

import com.example.chambape.domain.model.Message

interface MessageRepository {
    suspend fun getConversations(userId: String): Result<List<Any>>
    suspend fun getMessages(conversationId: String): Result<List<Message>>
    suspend fun sendMessage(conversationId: String, senderId: String, content: String): Result<Message>
}