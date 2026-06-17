package com.example.chambape.domain.repository

import com.example.chambape.data.remote.dto.ReviewDto

interface ReviewRepository {
    suspend fun createReview(
        jobId          : String,
        reviewerId     : String,
        reviewedUserId : String,
        rating         : Int,
        comment        : String?
    ): Result<ReviewDto>

    suspend fun getReviewsByJob(jobId: String): Result<List<ReviewDto>>
}