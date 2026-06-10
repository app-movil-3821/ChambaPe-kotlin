package com.example.chambape.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chambape.data.remote.dto.CreateJobRequest
import com.example.chambape.data.repository.TokenManager
import com.example.chambape.domain.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed class CreateJobUiState {
    object Idle : CreateJobUiState()
    object Loading : CreateJobUiState()
    object Success : CreateJobUiState()
    data class Error(val message: String) : CreateJobUiState()
}

class CreateJobViewModel(
    private val jobRepository: JobRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateJobUiState>(CreateJobUiState.Idle)
    val uiState: StateFlow<CreateJobUiState> = _uiState.asStateFlow()

    fun publishJob(
        title: String,
        description: String,
        category: String,
        payment: String,
        district: String
    ) {
        val contractorId = tokenManager.getUserId() ?: return

        viewModelScope.launch {
            _uiState.value = CreateJobUiState.Loading

            // Formateador clásico y seguro para las fechas
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())
            sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")

            val ahora = java.util.Calendar.getInstance()
            val start = sdf.format(ahora.time)

            ahora.add(java.util.Calendar.DAY_OF_MONTH, 1) // Mañana
            val end = sdf.format(ahora.time)

            val request = CreateJobRequest(
                contractorId = contractorId,
                title = title,
                description = description,
                category = category,
                paymentAmount = payment.toDoubleOrNull() ?: 0.0,
                latitude = -12.046374,
                longitude = -77.042793,
                address = "Dirección por definir",
                district = district,
                scheduledStart = start,
                scheduledEnd = end
            )

            try {
                val job = jobRepository.createJob(request)

                _uiState.value = CreateJobUiState.Success
            } catch (e: Exception) {
                println("🚨 ERROR REAL AL CREAR TRABAJO: ${e.localizedMessage}")
                _uiState.value = CreateJobUiState.Error(e.localizedMessage ?: "Error desconocido al publicar")
            }
        }
    }

    fun resetState() { _uiState.value = CreateJobUiState.Idle }
}

// Factory para la inyección de dependencias manual
class CreateJobViewModelFactory(
    private val jobRepository: JobRepository,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateJobViewModel(jobRepository, tokenManager) as T
    }
}