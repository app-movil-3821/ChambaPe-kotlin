package com.example.chambape.data.repository

import com.example.chambape.data.mapper.toDomain
import com.example.chambape.data.remote.dto.LoginRequest
import com.example.chambape.data.remote.dto.RegisterRequest
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
            // Guardar token para usarlo en requests protegidas
            tokenManager.saveToken(response.token)
            tokenManager.saveUserId(response.userId)
            // Retornar usuario básico del login response
            val user = User(
                id         = response.userId,
                name       = response.name,
                email      = response.email,
                role       = response.role,
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
}