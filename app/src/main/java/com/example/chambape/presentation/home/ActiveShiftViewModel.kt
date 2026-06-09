package com.example.chambape.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ActiveShiftUiState(
    val isLoading: Boolean = false,
    val job: Job? = null,
    val errorMessage: String? = null
)

class ActiveShiftViewModel : ViewModel() {

    private val jobRepository = AppModule.jobRepository

    private val _uiState = MutableStateFlow(ActiveShiftUiState())
    val uiState: StateFlow<ActiveShiftUiState> = _uiState.asStateFlow()

    fun loadJob(jobId: String) {
        if (jobId.isBlank()) {
            _uiState.value = ActiveShiftUiState(
                isLoading = false,
                errorMessage = "No se encontró el ID del trabajo."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = ActiveShiftUiState(isLoading = true)

            jobRepository.getJobById(jobId)
                .onSuccess { job ->
                    _uiState.value = ActiveShiftUiState(
                        isLoading = false,
                        job = job
                    )
                }
                .onFailure { error ->
                    _uiState.value = ActiveShiftUiState(
                        isLoading = false,
                        errorMessage = error.message ?: "Error al cargar el turno."
                    )
                }
        }
    }
}
