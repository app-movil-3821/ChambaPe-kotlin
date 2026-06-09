package com.example.chambape.domain.model

data class Conversation(
    val id: String,
    val jobId: String,
    val enrollmentId: String,
    val contractorId: String,
    val workerId: String,
    val status: String,
    val createdAt: String
)
