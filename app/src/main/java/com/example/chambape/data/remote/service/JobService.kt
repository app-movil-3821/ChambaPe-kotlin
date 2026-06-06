package com.example.chambape.data.remote.service

import com.example.chambape.data.remote.dto.JobDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JobService {
    @GET("jobs")
    suspend fun getJobs(): List<JobDto>

    @GET("jobs/{id}")
    suspend fun getJobById(@Path("id") jobId: String): JobDto

    @GET("jobs/published")
    suspend fun getPublishedJobs(): List<JobDto>

    @GET("jobs/nearby")
    suspend fun getNearbyJobs(
        @Query("latitude")  latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radiusKm")  radiusKm: Double,
        @Query("category")  category: String? = null,
        @Query("district")  district: String? = null,
        @Query("minPayment") minPayment: Double? = null
    ): List<JobDto>
}
