package com.example.chambape.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class JobDetailsUiState(
    val isLoading: Boolean = false,
    val job: Job? = null,
    val errorMessage: String? = null
)

class JobDetailsViewModel : ViewModel() {

    private val jobRepository = AppModule.jobRepository

    private val _uiState = MutableStateFlow(JobDetailsUiState())
    val uiState: StateFlow<JobDetailsUiState> = _uiState.asStateFlow()

    fun loadJob(jobId: String) {
        if (jobId.isBlank()) {
            _uiState.value = JobDetailsUiState(errorMessage = "No se encontró el ID del trabajo.")
            return
        }
        viewModelScope.launch {
            _uiState.value = JobDetailsUiState(isLoading = true)
            jobRepository.getJobById(jobId)
                .onSuccess { job ->
                    _uiState.value = JobDetailsUiState(job = job)
                }
                .onFailure { error ->
                    _uiState.value = JobDetailsUiState(
                        errorMessage = error.message ?: "Error al cargar el trabajo."
                    )
                }
        }
    }
}
