package com.example.chambape.data.remote.dto



data class LoginRequest(
    val email: String,
    val password: String
)


data class LoginResponse(
    val token: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val role: String? = null
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

data class UpdateUserRequest(
    val name: String,
    val phone: String,
    val skills: List<String> = emptyList(),
    val experience: String = "",
    val district: String = ""
)