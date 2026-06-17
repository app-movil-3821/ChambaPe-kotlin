package com.example.chambape.data.repository

import com.example.chambape.data.remote.dto.CreateReviewRequest
import com.example.chambape.data.remote.dto.ReviewDto
import com.example.chambape.data.remote.service.ReviewService
import com.example.chambape.domain.repository.ReviewRepository

class ReviewRepositoryImpl(
    private val reviewService: ReviewService
) : ReviewRepository {

    override suspend fun createReview(
        jobId          : String,
        reviewerId     : String,
        reviewedUserId : String,
        rating         : Int,
        comment        : String?
    ): Result<ReviewDto> = try {
        val dto = reviewService.createReview(
            CreateReviewRequest(jobId, reviewerId, reviewedUserId, rating, comment)
        )
        Result.success(dto)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getReviewsByJob(jobId: String): Result<List<ReviewDto>> = try {
        Result.success(reviewService.getReviewsByJob(jobId))
    } catch (e: Exception) {
        Result.failure(e)
    }
}