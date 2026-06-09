package com.example.chambape.presentation.shifts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ShiftSummaryUiState(
    val  isLoading: Boolean = false,
    val job: Job? = null,
    val errorMessage: String? = null
)

class ShiftSummaryViewModel : ViewModel(){
    private val jobRepository = AppModule.jobRepository

    private val _uiState = MutableStateFlow(ShiftSummaryUiState())
    val uiState: StateFlow<ShiftSummaryUiState> = _uiState.asStateFlow()

    fun loadJob(jobId: String) {
        if (jobId.isBlank()) {
            _uiState.value = ShiftSummaryUiState(
                isLoading = false,
                errorMessage = "No se encontró el ID del trabajo."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = ShiftSummaryUiState(isLoading = true)

            jobRepository.getJobById(jobId)
                .onSuccess { job ->
                    _uiState.value = ShiftSummaryUiState(
                        isLoading = false,
                        job = job
                    )
                }
                .onFailure { error ->
                    _uiState.value = ShiftSummaryUiState(
                        isLoading = false,
                        errorMessage = error.message ?: "Error al cargar el resumen del turno."
                    )
                }
        }
    }
}
