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
    val isLoading    : Boolean = false,
    val job          : Job?    = null,
    val errorMessage : String? = null,
    val reviewSent   : Boolean = false,
    val isSending    : Boolean = false
)

class ShiftSummaryViewModel : ViewModel() {

    private val jobRepository    = AppModule.jobRepository
    private val reviewRepository = AppModule.reviewRepository
    private val tokenManager     = AppModule.tokenManager

    private val _uiState = MutableStateFlow(ShiftSummaryUiState())
    val uiState: StateFlow<ShiftSummaryUiState> = _uiState.asStateFlow()

    fun loadJob(jobId: String) {
        if (jobId.isBlank()) {
            _uiState.value = ShiftSummaryUiState(errorMessage = "No se encontró el ID del trabajo.")
            return
        }
        viewModelScope.launch {
            _uiState.value = ShiftSummaryUiState(isLoading = true)
            jobRepository.getJobById(jobId)
                .onSuccess { job ->
                    _uiState.value = ShiftSummaryUiState(job = job)
                }
                .onFailure { error ->
                    _uiState.value = ShiftSummaryUiState(
                        errorMessage = error.message ?: "Error al cargar el resumen del turno."
                    )
                }
        }
    }

    /**
     * Envía la calificación del chambeador al contratante.
     * reviewedUserId = contractorId del job (a quién se califica).
     */
    fun submitReview(rating: Int, comment: String) {
        val job        = _uiState.value.job ?: return
        val reviewerId = tokenManager.getUserId() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true, errorMessage = null)
            reviewRepository.createReview(
                jobId          = job.id,
                reviewerId     = reviewerId,
                reviewedUserId = job.contractorId,
                rating         = rating,
                comment        = comment.ifBlank { null }
            )
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isSending = false, reviewSent = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSending    = false,
                        errorMessage = e.message ?: "No se pudo enviar la calificación."
                    )
                }
        }
    }
}