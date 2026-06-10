package com.example.chambape.domain.repository

import com.example.chambape.data.remote.dto.CreateJobRequest
import com.example.chambape.domain.model.Job

interface JobRepository {
    suspend fun getJobs(): Result<List<Job>>
    suspend fun getJobById(jobId: String): Result<Job>
    suspend fun getPublishedJobs(): Result<List<Job>>
    suspend fun getNearbyJobs(
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): Result<List<Job>>

    suspend fun createJob(request: CreateJobRequest): Job
}