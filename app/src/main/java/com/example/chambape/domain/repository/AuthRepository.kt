package com.example.chambape.domain.repository

import com.example.chambape.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(
        name: String,
        email: String,
        password: String,
        role: String,
        skills: List<String>,
        experience: String,
        district: String,
        phone: String
    ): Result<User>
}