package com.example.chambape.data.remote.service

import com.example.chambape.data.remote.dto.CreateReviewRequest
import com.example.chambape.data.remote.dto.ReviewDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {

    @POST("reviews")
    suspend fun createReview(@Body body: CreateReviewRequest): ReviewDto

    /** Reviews que ha recibido un usuario (para mostrar su reputación). */
    @GET("reviews/user/{userId}")
    suspend fun getReviewsByUser(@Path("userId") userId: String): List<ReviewDto>

    /** Reviews de un job específico (para saber si ya se calificó). */
    @GET("reviews/job/{jobId}")
    suspend fun getReviewsByJob(@Path("jobId") jobId: String): List<ReviewDto>
}