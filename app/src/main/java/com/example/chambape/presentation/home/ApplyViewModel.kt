package com.example.chambape.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ApplyUiState(
    val isLoading: Boolean = true,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ApplyViewModel : ViewModel() {

    private val shiftRepository = AppModule.shiftRepository
    private val tokenManager    = AppModule.tokenManager

    private val _uiState = MutableStateFlow(ApplyUiState())
    val uiState: StateFlow<ApplyUiState> = _uiState.asStateFlow()

    fun apply(jobId: String, contractorId: String) {
        val workerId = tokenManager.getUserId() ?: run {
            _uiState.value = ApplyUiState(isLoading = false, errorMessage = "No se encontró tu sesión.")
            return
        }
        viewModelScope.launch {
            _uiState.value = ApplyUiState(isLoading = true)
            shiftRepository.applyToJob(jobId, workerId, contractorId)
                .onSuccess {
                    _uiState.value = ApplyUiState(isLoading = false, isSuccess = true)
                }
                .onFailure { error ->
                    _uiState.value = ApplyUiState(
                        isLoading = false,
                        errorMessage = error.message ?: "Error al aplicar al turno."
                    )
                }
        }
    }
}
