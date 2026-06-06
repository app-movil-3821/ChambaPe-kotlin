package com.example.chambape.domain.model

data class Job(
    val id: String,
    val contractorId: String,
    val title: String,
    val description: String,
    val category: String,
    val requiredSkills: List<String>,
    val paymentAmount: Double,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val district: String,
    val scheduledStart: String,
    val scheduledEnd: String,
    val status: String
)