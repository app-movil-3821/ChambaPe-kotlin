package com.example.chambape.presentation.home.domain.repository

import com.example.chambape.presentation.home.domain.model.Job

interface JobRepository {
    suspend fun getJobs(): List<Job>
}