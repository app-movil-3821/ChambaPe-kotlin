package com.example.chambape.presentation.home.di

import com.example.chambape.presentation.home.data.repository.JobRepositoryImpl
import com.example.chambape.presentation.home.data.remote.JobService
import com.example.chambape.presentation.home.di.RemoteModule.provideJobService
import com.example.chambape.presentation.home.domain.repository.JobRepository

object RepositoryModule {
    fun provideJobRepository(jobService: JobService = provideJobService()): JobRepository {
        return JobRepositoryImpl(jobService)
    }
}