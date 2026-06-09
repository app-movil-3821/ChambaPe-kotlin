package com.example.chambape.presentation.shifts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Job
import com.example.chambape.domain.model.Shift
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ShiftWithJob(
    val shift: Shift,
    val job: Job?
)

data class MyShiftsUiState(
    val isLoading: Boolean = false,
    val shifts: List<ShiftWithJob> = emptyList(),
    val errorMessage: String? = null
)

class MyShiftsViewModel : ViewModel() {

    private val shiftRepository = AppModule.shiftRepository
    private val jobRepository   = AppModule.jobRepository
    private val tokenManager    = AppModule.tokenManager

    private val _uiState = MutableStateFlow(MyShiftsUiState())
    val uiState: StateFlow<MyShiftsUiState> = _uiState.asStateFlow()

    fun load() {
        val workerId = tokenManager.getUserId() ?: run {
            _uiState.value = MyShiftsUiState(errorMessage = "No se encontró tu sesión.")
            return
        }
        viewModelScope.launch {
            _uiState.value = MyShiftsUiState(isLoading = true)
            shiftRepository.getShiftsByWorker(workerId)
                .onSuccess { shifts ->
                    val shiftsWithJobs = shifts.map { shift ->
                        async {
                            val job = jobRepository.getJobById(shift.jobId).getOrNull()
                            ShiftWithJob(shift, job)
                        }
                    }.awaitAll()
                    _uiState.value = MyShiftsUiState(shifts = shiftsWithJobs)
                }
                .onFailure { error ->
                    _uiState.value = MyShiftsUiState(
                        errorMessage = error.message ?: "Error al cargar tus turnos."
                    )
                }
        }
    }
}
