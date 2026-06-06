package com.example.chambape.domain.repository

import com.example.chambape.domain.model.Shift

interface ShiftRepository {
    suspend fun applyToJob(jobId: String, workerId: String, contractorId: String): Result<Shift>
    suspend fun getShiftsByWorker(workerId: String): Result<List<Shift>>
    suspend fun acceptShift(enrollmentId: String): Result<Shift>
    suspend fun rejectShift(enrollmentId: String): Result<Shift>
}