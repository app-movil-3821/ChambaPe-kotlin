package com.example.chambape.data.remote.service

import com.example.chambape.data.remote.dto.ConversationDto
import com.example.chambape.data.remote.dto.MessageDto
import com.example.chambape.data.remote.dto.SendMessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageService {
    @GET("communications/user/{userId}")
    suspend fun getConversations(@Path("userId") userId: String): List<ConversationDto>

    @GET("communications/{conversationId}/messages")
    suspend fun getMessages(@Path("conversationId") conversationId: String): List<MessageDto>

    @POST("communications/{conversationId}/messages")
    suspend fun sendMessage(
        @Path("conversationId") conversationId: String,
        @Body body: SendMessageRequest
    ): MessageDto
}