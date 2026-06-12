package com.example.chambape.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chambape.data.repository.TokenManager
import com.example.chambape.domain.model.Job
import com.example.chambape.domain.repository.JobAction
import com.example.chambape.domain.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Carga los trabajos que el contratante (usuario actual) ha publicado,
 * con sus estados respectivos. Alimenta la pestaña "Jobs".
 */
class MyJobsViewModel(
    private val jobRepository: JobRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Id de la chamba cuya transición se está procesando (para deshabilitar sus botones).
    private val _actioningId = MutableStateFlow<String?>(null)
    val actioningId: StateFlow<String?> = _actioningId.asStateFlow()

    init {
        // La carga la dispara la pantalla en cada ON_RESUME (también al volver de publicar).
    }

    fun loadMyJobs() {
        val contractorId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            // Solo mostramos el spinner a pantalla completa en la primera carga;
            // los refrescos posteriores son silenciosos para no parpadear.
            if (_jobs.value.isEmpty()) _isLoading.value = true
            _errorMessage.value = null
            jobRepository.getJobsByContractor(contractorId)
                .onSuccess { _jobs.value = it }
                .onFailure { _errorMessage.value = it.message ?: "Error al cargar tus publicaciones." }
            _isLoading.value = false
        }
    }

    /** Ejecuta una transición de estado y refresca la lista al terminar. */
    fun onJobAction(jobId: String, action: JobAction) {
        if (_actioningId.value != null) return // evita doble toque
        viewModelScope.launch {
            _actioningId.value = jobId
            _errorMessage.value = null
            jobRepository.changeJobStatus(jobId, action)
                .onSuccess { loadMyJobs() }
                .onFailure { _errorMessage.value = it.message ?: "No se pudo actualizar el estado." }
            _actioningId.value = null
        }
    }
}

class MyJobsViewModelFactory(
    private val jobRepository: JobRepository,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyJobsViewModel(jobRepository, tokenManager) as T
    }
}