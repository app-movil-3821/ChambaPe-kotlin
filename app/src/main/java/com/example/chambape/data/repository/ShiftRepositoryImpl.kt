package com.example.chambape.data.repository

import com.example.chambape.data.mapper.toDomain
import com.example.chambape.data.remote.dto.EnrollmentRequest
import com.example.chambape.data.remote.service.ShiftService
import com.example.chambape.domain.model.Shift
import com.example.chambape.domain.repository.ShiftRepository

class ShiftRepositoryImpl(
    private val shiftService: ShiftService
) : ShiftRepository {

    override suspend fun applyToJob(
        jobId: String,
        workerId: String,
        contractorId: String
    ): Result<Shift> {
        return try {
            val shift = shiftService.applyToJob(
                EnrollmentRequest(jobId, workerId, contractorId)
            ).toDomain()
            Result.success(shift)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getShiftsByWorker(workerId: String): Result<List<Shift>> {
        return try {
            val shifts = shiftService.getShiftsByWorker(workerId).map { it.toDomain() }
            Result.success(shifts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptShift(enrollmentId: String): Result<Shift> {
        return try {
            val shift = shiftService.acceptShift(enrollmentId).toDomain()
            Result.success(shift)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun rejectShift(enrollmentId: String): Result<Shift> {
        return try {
            val shift = shiftService.rejectShift(enrollmentId).toDomain()
            Result.success(shift)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}