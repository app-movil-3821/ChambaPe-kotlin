package com.example.chambape.domain.model

data class Shift(
    val id: String,
    val jobId: String,
    val workerId: String,
    val contractorId: String,
    val status: String,
    val appliedAt: String
)