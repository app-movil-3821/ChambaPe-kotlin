package com.example.chambape.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.presentation.home.di.RepositoryModule.provideJobRepository
import com.example.chambape.presentation.home.domain.model.Job
import com.example.chambape.presentation.home.domain.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFeedViewModel(
    private val jobRepository: JobRepository = provideJobRepository()
): ViewModel() {

    private val _jobs = MutableStateFlow<List<Job>>(value = emptyList())
    val jobs: StateFlow<List<Job>> get() = _jobs

    fun getJobs() {
        viewModelScope.launch {
            _jobs.value = jobRepository.getJobs()
        }
    }

    init {
        getJobs()
    }
}