package com.example.chambape.data.remote.service

import com.example.chambape.data.remote.dto.LoginRequest
import com.example.chambape.data.remote.dto.LoginResponse
import com.example.chambape.data.remote.dto.RegisterRequest
import com.example.chambape.data.remote.dto.UpdateUserRequest
import com.example.chambape.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("users")
    suspend fun register(@Body body: RegisterRequest): UserDto

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: String): UserDto

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") userId: String, @Body body: UpdateUserRequest): UserDto
}