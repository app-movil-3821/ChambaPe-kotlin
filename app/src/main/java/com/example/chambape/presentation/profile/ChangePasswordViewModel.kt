package com.example.chambape.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChangePasswordUiState(
    val isSaving     : Boolean = false,
    val saveSuccess  : Boolean = false,
    val errorMessage : String? = null
)

class ChangePasswordViewModel : ViewModel() {

    private val authRepository = AppModule.authRepository
    private val tokenManager   = AppModule.tokenManager

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun changePassword(currentPassword: String, newPassword: String) {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            _uiState.value = ChangePasswordUiState(isSaving = true)
            authRepository.changePassword(userId, currentPassword, newPassword)
                .onSuccess { _uiState.value = ChangePasswordUiState(saveSuccess = true) }
                .onFailure { _uiState.value = ChangePasswordUiState(
                    errorMessage = it.message ?: "No se pudo cambiar la contraseña."
                )}
        }
    }
}