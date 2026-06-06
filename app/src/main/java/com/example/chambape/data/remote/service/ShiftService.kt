package com.example.chambape.data.remote.service

import com.example.chambape.data.remote.dto.EnrollmentRequest
import com.example.chambape.data.remote.dto.ShiftDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShiftService {
    @POST("enrollments")
    suspend fun applyToJob(@Body body: EnrollmentRequest): ShiftDto

    @GET("enrollments/worker/{workerId}")
    suspend fun getShiftsByWorker(@Path("workerId") workerId: String): List<ShiftDto>

    @GET("enrollments/{id}")
    suspend fun getShiftById(@Path("id") enrollmentId: String): ShiftDto

    @PUT("enrollments/{id}/accept")
    suspend fun acceptShift(@Path("id") enrollmentId: String): ShiftDto

    @PUT("enrollments/{id}/reject")
    suspend fun rejectShift(@Path("id") enrollmentId: String): ShiftDto

    @PUT("enrollments/{id}/cancel")
    suspend fun cancelShift(@Path("id") enrollmentId: String): ShiftDto
}
