package com.example.chambape.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chambape.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterUiState {
    object Idle    : RegisterUiState()
    object Loading : RegisterUiState()
    // Lleva el rol para que la pantalla decida la ruta de onboarding
    data class Success(val role: String) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(
        name    : String,
        email   : String,
        phone   : String,
        password: String,
        role    : String
    ) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading

            // 1. Registrar en el backend
            val registerResult = authRepository.register(
                name       = name.trim(),
                email      = email.trim(),
                password   = password,
                role       = role,
                phone      = phone.trim(),
                skills     = emptyList(),
                experience = "",
                district   = ""
            )

            if (registerResult.isFailure) {
                _uiState.value = RegisterUiState.Error(
                    registerResult.exceptionOrNull()?.message ?: "Error al registrarse."
                )
                return@launch
            }

            // 2. Auto-login para obtener y guardar el token
            val loginResult = authRepository.login(email.trim(), password)

            _uiState.value = if (loginResult.isSuccess) {
                RegisterUiState.Success(role)
            } else {
                // Registro OK pero login falló — pedir que inicie sesión manualmente
                RegisterUiState.Error("Registro exitoso. Por favor inicia sesión.")
            }
        }
    }

    fun resetState() { _uiState.value = RegisterUiState.Idle }
}

class RegisterViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        RegisterViewModel(authRepository) as T
}