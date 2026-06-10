package com.example.chambape.data.remote.dto


data class JobDto(
    val id: String,
    val contractorId: String,
    val title: String,
    val description: String,
    val category: String,
    val requiredSkills: List<String> = emptyList(),
    val paymentAmount: Double,
    val location: LocationDto? = null,
    val scheduledStart: String? = null,
    val scheduledEnd: String? = null,
    val status: String,
    val createdAt: String = "",
    val updatedAt: String = ""
)

data class LocationDto(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val district: String
)

data class CreateJobRequest(
    val contractorId: String,
    val title: String,
    val description: String,
    val category: String,
    val requiredSkills: List<String> = emptyList(),
    val paymentAmount: Double,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val district: String,
    val scheduledStart: String,
    val scheduledEnd: String
)