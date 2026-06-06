package com.example.chambape.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class JobDto(
    val id: String,
    val contractorId: String,
    val title: String,
    val description: String,
    val category: String,
    val requiredSkills: List<String> = emptyList(),
    val paymentAmount: Double,
    val location: LocationDto,
    val scheduledStart: String,
    val scheduledEnd: String,
    val status: String,
    val createdAt: String = "",
    val updatedAt: String = ""
)

@Serializable
data class LocationDto(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val district: String
)