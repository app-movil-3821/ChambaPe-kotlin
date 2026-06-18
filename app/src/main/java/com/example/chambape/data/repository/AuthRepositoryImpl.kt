package com.example.chambape.data.repository

import com.example.chambape.data.mapper.toDomain
import com.example.chambape.data.remote.dto.ChangePasswordRequest
import com.example.chambape.data.remote.dto.LoginRequest
import com.example.chambape.data.remote.dto.RegisterRequest
import com.example.chambape.data.remote.dto.UpdateUserRequest
import com.example.chambape.data.remote.service.AuthService
import com.example.chambape.domain.model.User
import com.example.chambape.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authService.login(LoginRequest(email, password))
            val token  = response.token  ?: return Result.failure(Exception("Respuesta inválida del servidor (token ausente)."))
            val userId = response.userId ?: return Result.failure(Exception("Respuesta inválida del servidor (userId ausente)."))
            tokenManager.saveToken(token)
            tokenManager.saveUserId(userId)
            val user = User(
                id         = userId,
                name       = response.name  ?: "",
                email      = response.email ?: email,
                role       = response.role  ?: "",
                phone      = "",
                skills     = emptyList(),
                experience = "",
                district   = "",
                photoUrl   = null,
                verified   = false
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(userId: String): Result<User> {
        return try {
            val dto = authService.getUser(userId)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(
        userId    : String,
        name      : String,
        phone     : String,
        skills    : List<String>,
        experience: String,
        district  : String
    ): Result<User> {
        return try {
            val dto = authService.updateUser(
                userId,
                UpdateUserRequest(
                    phone      = phone,
                    skills     = skills,
                    experience = experience,
                    district   = district,
                    verified   = false
                )
            )
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        role: String,
        skills: List<String>,
        experience: String,
        district: String,
        phone: String
    ): Result<User> {
        return try {
            val dto = authService.register(
                RegisterRequest(
                    name       = name,
                    email      = email,
                    password   = password,
                    role       = role,
                    skills     = skills,
                    experience = experience,
                    district   = district,
                    phone      = phone
                )
            )
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changePassword(
        userId         : String,
        currentPassword: String,
        newPassword    : String
    ): Result<Unit> = try {
        authService.changePassword(userId, ChangePasswordRequest(currentPassword, newPassword))
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}