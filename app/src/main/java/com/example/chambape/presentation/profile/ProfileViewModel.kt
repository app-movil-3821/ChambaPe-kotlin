package com.example.chambape.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null
)

class ProfileViewModel : ViewModel() {

    private val authRepository = AppModule.authRepository
    private val tokenManager   = AppModule.tokenManager

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun load() {
        val userId = tokenManager.getUserId() ?: run {
            _uiState.value = ProfileUiState(errorMessage = "No se encontró tu sesión.")
            return
        }
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)
            authRepository.getUser(userId)
                .onSuccess { user ->
                    _uiState.value = ProfileUiState(user = user)
                }
                .onFailure { error ->
                    _uiState.value = ProfileUiState(
                        errorMessage = error.message ?: "Error al cargar el perfil."
                    )
                }
        }
    }
}
