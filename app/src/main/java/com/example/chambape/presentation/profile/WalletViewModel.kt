package com.example.chambape.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WalletTransaction(
    val title   : String,
    val date    : String,
    val amount  : Double,
    val isIncome: Boolean
)

data class WalletUiState(
    val isLoading   : Boolean              = false,
    val balance     : Double               = 0.0,
    val transactions: List<WalletTransaction> = emptyList(),
    val errorMessage: String?              = null
)

class WalletViewModel : ViewModel() {

    private val shiftRepository = AppModule.shiftRepository
    private val jobRepository   = AppModule.jobRepository
    private val tokenManager    = AppModule.tokenManager

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    fun load() {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            _uiState.value = WalletUiState(isLoading = true)

            // Obtener postulaciones del chambeador
            val shiftsResult = shiftRepository.getShiftsByWorker(userId)
            if (shiftsResult.isFailure) {
                _uiState.value = WalletUiState(errorMessage = "No se pudo cargar la billetera.")
                return@launch
            }

            val shifts = shiftsResult.getOrNull() ?: emptyList()

            // Para cada postulación aceptada, obtener el job
            val transactions = mutableListOf<WalletTransaction>()
            var totalBalance = 0.0

            for (shift in shifts) {
                val jobResult = jobRepository.getJobById(shift.jobId)
                val job = jobResult.getOrNull() ?: continue

                if (job.status == "COMPLETED") {
                    totalBalance += job.paymentAmount
                    transactions.add(
                        WalletTransaction(
                            title    = "Pago — ${job.title}",
                            date     = formatDate(job.scheduledEnd),
                            amount   = job.paymentAmount,
                            isIncome = true
                        )
                    )
                }
            }

            _uiState.value = WalletUiState(
                balance      = totalBalance,
                transactions = transactions.sortedByDescending { it.date }
            )
        }
    }

    private fun formatDate(dateTime: String): String = try {
        // "2024-10-15T14:00:00" → "15 Oct, 2024"
        val months = listOf("","Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic")
        val parts = dateTime.substring(0, 10).split("-")
        val day   = parts[2].toInt()
        val month = months[parts[1].toInt()]
        val year  = parts[0]
        "$day $month, $year"
    } catch (e: Exception) { dateTime }
}