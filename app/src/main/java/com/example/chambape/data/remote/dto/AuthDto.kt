package com.example.chambape.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val userId: String,
    val name: String,
    val email: String,
    val role: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String = "CHAMBEADOR",
    val skills: List<String> = emptyList(),
    val experience: String = "",
    val district: String = "",
    val phone: String
)