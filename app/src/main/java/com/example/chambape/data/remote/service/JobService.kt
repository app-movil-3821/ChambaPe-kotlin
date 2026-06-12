package com.example.chambape.data.remote.service

import com.example.chambape.data.remote.dto.CreateJobRequest
import com.example.chambape.data.remote.dto.JobDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface JobService {
    @GET("jobs")
    suspend fun getJobs(): List<JobDto>

    @GET("jobs/{id}")
    suspend fun getJobById(@Path("id") jobId: String): JobDto

    @GET("jobs/published")
    suspend fun getPublishedJobs(): List<JobDto>

    @GET("jobs/contractor/{contractorId}")
    suspend fun getJobsByContractor(@Path("contractorId") contractorId: String): List<JobDto>

    @GET("jobs/nearby")
    suspend fun getNearbyJobs(
        @Query("latitude")  latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radiusKm")  radiusKm: Double,
        @Query("category")  category: String? = null,
        @Query("district")  district: String? = null,
        @Query("minPayment") minPayment: Double? = null
    ): List<JobDto>

    @POST("jobs")
    suspend fun createJob(@Body body: CreateJobRequest): JobDto

    // ─── Transiciones de estado (gestión del contratante) ───
    @PUT("jobs/{id}/publish")
    suspend fun publishJob(@Path("id") id: String): JobDto

    @PUT("jobs/{id}/start")
    suspend fun startJob(@Path("id") id: String): JobDto

    @PUT("jobs/{id}/complete")
    suspend fun completeJob(@Path("id") id: String): JobDto

    @PUT("jobs/{id}/cancel")
    suspend fun cancelJob(@Path("id") id: String): JobDto

    @PUT("jobs/{id}/close")
    suspend fun closeJob(@Path("id") id: String): JobDto

    @PUT("jobs/{id}/reopen")
    suspend fun reopenJob(@Path("id") id: String): JobDto
}