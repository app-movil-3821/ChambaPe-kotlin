package com.example.chambape.data.remote.dto


data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val profile: ProfileDto? = null,
    val createdAt: String = "",
    val updatedAt: String = ""
)

data class ProfileDto(
    val photoUrl: String? = null,
    val skills: List<String> = emptyList(),
    val experience: String = "",
    val district: String = "",
    val phone: String = "",
    val verified: Boolean = false
)
