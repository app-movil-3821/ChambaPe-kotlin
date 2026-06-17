package com.example.chambape.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chambape.data.repository.TokenManager
import com.example.chambape.domain.model.Job
import com.example.chambape.domain.model.Shift
import com.example.chambape.domain.repository.AuthRepository
import com.example.chambape.domain.repository.JobAction
import com.example.chambape.domain.repository.JobRepository
import com.example.chambape.domain.repository.ShiftRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Postulante enriquecido con el nombre del worker (para mostrarlo en la UI). */
data class ApplicantUi(
    val enrollmentId: String,
    val workerId: String,
    val workerName: String,
    val status: String
)

/**
 * Carga los trabajos que el contratante (usuario actual) ha publicado,
 * con sus estados y postulantes. Alimenta la pestaña "Jobs".
 */
class MyJobsViewModel(
    private val jobRepository: JobRepository,
    private val shiftRepository: ShiftRepository,
    private val authRepository: AuthRepository,
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

    // Postulantes por jobId: Map<jobId, List<ApplicantUi>>
    private val _applicantsMap = MutableStateFlow<Map<String, List<ApplicantUi>>>(emptyMap())
    val applicantsMap: StateFlow<Map<String, List<ApplicantUi>>> = _applicantsMap.asStateFlow()

    // Jobs con desplegable de postulantes abierto
    private val _expandedJobIds = MutableStateFlow<Set<String>>(emptySet())
    val expandedJobIds: StateFlow<Set<String>> = _expandedJobIds.asStateFlow()

    // Id de la postulación que se está procesando (aceptar/rechazar)
    private val _actioningEnrollmentId = MutableStateFlow<String?>(null)
    val actioningEnrollmentId: StateFlow<String?> = _actioningEnrollmentId.asStateFlow()

    init {
        // La carga la dispara la pantalla en cada ON_RESUME (también al volver de publicar).
    }

    fun loadMyJobs() {
        val contractorId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            if (_jobs.value.isEmpty()) _isLoading.value = true
            _errorMessage.value = null
            jobRepository.getJobsByContractor(contractorId)
                .onSuccess { _jobs.value = it }
                .onFailure { _errorMessage.value = it.message ?: "Error al cargar tus publicaciones." }
            _isLoading.value = false
        }
    }

    /** Abre o cierra el desplegable de postulantes de una chamba. */
    fun toggleApplicants(jobId: String) {
        val expanded = _expandedJobIds.value
        if (jobId in expanded) {
            // Cerrar
            _expandedJobIds.value = expanded - jobId
        } else {
            // Abrir: cargar postulantes si no los tenemos aún
            _expandedJobIds.value = expanded + jobId
            if (!_applicantsMap.value.containsKey(jobId)) {
                loadApplicants(jobId)
            }
        }
    }

    /** Carga los postulantes de un job y resuelve sus nombres. */
    private fun loadApplicants(jobId: String) {
        viewModelScope.launch {
            // Marcamos como "cargando" con lista vacía temporal
            _applicantsMap.value = _applicantsMap.value + (jobId to emptyList())

            shiftRepository.getEnrollmentsByJob(jobId)
                .onSuccess { shifts ->
                    // Filtramos solo PENDING para que el contratante solo vea pendientes de decisión
                    val pending = shifts.filter { it.status == "PENDING" }

                    // Resolvemos los nombres en paralelo
                    val applicants = pending.map { shift ->
                        async {
                            val name = authRepository.getUser(shift.workerId)
                                .getOrNull()?.name ?: "Chambeador"
                            ApplicantUi(
                                enrollmentId = shift.id,
                                workerId     = shift.workerId,
                                workerName   = name,
                                status       = shift.status
                            )
                        }
                    }.awaitAll()

                    _applicantsMap.value = _applicantsMap.value + (jobId to applicants)
                }
                .onFailure {
                    // Si falla la carga, quitamos la entrada para que el usuario pueda reintentar
                    _applicantsMap.value = _applicantsMap.value - jobId
                    _expandedJobIds.value = _expandedJobIds.value - jobId
                    _errorMessage.value = "No se pudieron cargar los postulantes."
                }
        }
    }

    /** Acepta o rechaza una postulación y refresca postulantes + lista de jobs. */
    fun onEnrollmentAction(jobId: String, enrollmentId: String, accept: Boolean) {
        if (_actioningEnrollmentId.value != null) return
        viewModelScope.launch {
            _actioningEnrollmentId.value = enrollmentId
            val result = if (accept) {
                shiftRepository.acceptShift(enrollmentId)
            } else {
                shiftRepository.rejectShift(enrollmentId)
            }
            result
                .onSuccess {
                    // Refrescamos postulantes del job y la lista completa
                    _applicantsMap.value = _applicantsMap.value - jobId
                    loadApplicants(jobId)
                    loadMyJobs()
                }
                .onFailure {
                    _errorMessage.value = it.message ?: "No se pudo procesar la acción."
                }
            _actioningEnrollmentId.value = null
        }
    }

    /** Ejecuta una transición de estado y refresca la lista al terminar. */
    fun onJobAction(jobId: String, action: JobAction) {
        if (_actioningId.value != null) return
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
    private val shiftRepository: ShiftRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyJobsViewModel(jobRepository, shiftRepository, authRepository, tokenManager) as T
    }
}