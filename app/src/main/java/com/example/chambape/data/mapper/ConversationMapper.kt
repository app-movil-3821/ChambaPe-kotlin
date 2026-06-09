package com.example.chambape.data.mapper

import com.example.chambape.data.remote.dto.ConversationDto
import com.example.chambape.domain.model.Conversation

fun ConversationDto.toDomain() = Conversation(
    id           = id,
    jobId        = jobId,
    enrollmentId = enrollmentId,
    contractorId = contractorId,
    workerId     = workerId,
    status       = status,
    createdAt    = createdAt
)
