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
    suspend fun getUser(userId: String): Result<User>
    suspend fun updateUser(
        userId    : String,
        name      : String,
        phone     : String,
        skills    : List<String>,
        experience: String,
        district  : String
    ): Result<User>

    suspend fun changePassword(
        userId         : String,
        currentPassword: String,
        newPassword    : String
    ): Result<Unit>
}