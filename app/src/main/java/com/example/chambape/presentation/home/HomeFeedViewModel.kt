package com.example.chambape.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFeedViewModel : ViewModel() {

    private val jobRepository = AppModule.jobRepository

    private val _jobs      = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> get() = _jobs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        loadJobs()
    }

    private fun loadJobs() {
        viewModelScope.launch {
            _isLoading.value = true
            jobRepository.getJobs()
                .onSuccess { _jobs.value = it }
                .onFailure { println("ERROR: ${it.message}") }
            _isLoading.value = false
        }
    }
}