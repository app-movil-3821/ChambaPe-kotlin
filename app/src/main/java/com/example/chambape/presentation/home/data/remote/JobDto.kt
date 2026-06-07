package com.example.chambape.presentation.home.data.remote

// Refleja exactamente la estructura del JSON
data class LocationDto(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val district: String
)

data class JobDto(
    val id: String,
    val contractorId: String,
    val title: String,
    val description: String,
    val category: String,
    val requiredSkills: List<String>,
    val paymentAmount: Double,
    val location: LocationDto,
    val scheduleStart: String,
    val scheduleEnd: String,
    val status: String,
    val creatAt: String,
    val updatedAt: String
)