package com.example.chambape.data.repository

import com.example.chambape.data.mapper.toDomain
import com.example.chambape.data.remote.service.JobService
import com.example.chambape.domain.model.Job
import com.example.chambape.domain.repository.JobAction
import com.example.chambape.domain.repository.JobRepository

class JobRepositoryImpl(
    private val jobService: JobService
) : JobRepository {

    override suspend fun getJobs(): Result<List<Job>> {
        return try {
            val jobs = jobService.getJobs().map { it.toDomain() }
            Result.success(jobs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getJobById(jobId: String): Result<Job> {
        return try {
            val job = jobService.getJobById(jobId).toDomain()
            Result.success(job)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPublishedJobs(): Result<List<Job>> {
        return try {
            val jobs = jobService.getPublishedJobs().map { it.toDomain() }
            Result.success(jobs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getJobsByContractor(contractorId: String): Result<List<Job>> {
        return try {
            val jobs = jobService.getJobsByContractor(contractorId).map { it.toDomain() }
            Result.success(jobs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changeJobStatus(jobId: String, action: JobAction): Result<Job> {
        return try {
            val dto = when (action) {
                JobAction.PUBLISH  -> jobService.publishJob(jobId)
                JobAction.START    -> jobService.startJob(jobId)
                JobAction.COMPLETE -> jobService.completeJob(jobId)
                JobAction.CANCEL   -> jobService.cancelJob(jobId)
                JobAction.CLOSE    -> jobService.closeJob(jobId)
                JobAction.REOPEN   -> jobService.reopenJob(jobId)
            }
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNearbyJobs(
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): Result<List<Job>> {
        return try {
            val jobs = jobService.getNearbyJobs(latitude, longitude, radiusKm).map { it.toDomain() }
            Result.success(jobs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun createJob(request: com.example.chambape.data.remote.dto.CreateJobRequest): com.example.chambape.domain.model.Job {
        // Llama a tu Retrofit (jobService) y usa tu mapper para transformarlo al modelo de dominio
        return jobService.createJob(request).toDomain()
    }
}