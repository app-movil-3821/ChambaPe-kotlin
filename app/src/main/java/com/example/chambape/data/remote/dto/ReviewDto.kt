package com.example.chambape.data.remote.dto

data class CreateReviewRequest(
    val jobId          : String,
    val reviewerId     : String,
    val reviewedUserId : String,
    val rating         : Int,
    val comment        : String?
)

data class ReviewDto(
    val id             : String  = "",
    val jobId          : String  = "",
    val reviewerId     : String  = "",
    val reviewedUserId : String  = "",
    val rating         : Int     = 0,
    val comment        : String? = null,
    val createdAt      : String  = ""
)