package com.example.chambape.data.remote.dto


data class ShiftDto(
    val id: String,
    val jobId: String,
    val workerId: String,
    val contractorId: String,
    val status: String,
    val appliedAt: String,
    val respondedAt: String? = null,
    val updatedAt: String = ""
)


data class EnrollmentRequest(
    val jobId: String,
    val workerId: String,
    val contractorId: String
)
