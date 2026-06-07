package com.example.chambape.presentation.home.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface JobService {
    @GET(value = "jobs")
    suspend fun getJobs(): Response<List<JobDto>>
}