package com.example.chambape.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val phone: String,
    val skills: List<String>,
    val experience: String,
    val district: String,
    val photoUrl: String?,
    val verified: Boolean
)