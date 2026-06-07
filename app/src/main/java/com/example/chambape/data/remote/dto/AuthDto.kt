package com.example.chambape.data.remote.dto



data class LoginRequest(
    val email: String,
    val password: String
)


data class LoginResponse(
    val token: String,
    val userId: String,
    val name: String,
    val email: String,
    val role: String
)


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