package com.example.chambape.data.repository

import com.example.chambape.data.mapper.toDomain
import com.example.chambape.data.remote.dto.SendMessageRequest
import com.example.chambape.data.remote.service.MessageService
import com.example.chambape.domain.model.Conversation
import com.example.chambape.domain.model.Message
import com.example.chambape.domain.repository.MessageRepository

class MessageRepositoryImpl(
    private val messageService: MessageService
) : MessageRepository {

    override suspend fun getConversations(userId: String): Result<List<Conversation>> {
        return try {
            val conversations = messageService.getConversations(userId).map { it.toDomain() }
            Result.success(conversations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessages(conversationId: String): Result<List<Message>> {
        return try {
            val messages = messageService.getMessages(conversationId).map { it.toDomain() }
            Result.success(messages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendMessage(
        conversationId: String,
        senderId: String,
        content: String
    ): Result<Message> {
        return try {
            val message = messageService.sendMessage(
                conversationId,
                SendMessageRequest(senderId, content)
            ).toDomain()
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
