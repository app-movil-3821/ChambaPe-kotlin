package com.example.chambape.data.remote.service

import com.example.chambape.data.remote.dto.LoginRequest
import com.example.chambape.data.remote.dto.LoginResponse
import com.example.chambape.data.remote.dto.RegisterRequest
import com.example.chambape.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("users")
    suspend fun register(@Body body: RegisterRequest): UserDto
}