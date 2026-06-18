package com.example.chambape.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SkillsUiState(
    val isSaving     : Boolean = false,
    val saveSuccess  : Boolean = false,
    val errorMessage : String? = null
)

class SkillsViewModel : ViewModel() {

    private val authRepository = AppModule.authRepository
    private val tokenManager   = AppModule.tokenManager

    private val _uiState = MutableStateFlow(SkillsUiState())
    val uiState: StateFlow<SkillsUiState> = _uiState.asStateFlow()

    /**
     * Carga el perfil actual y luego guarda las skills seleccionadas
     * preservando los demás campos (phone, district, experience).
     */
    fun saveSkills(selectedSkills: List<String>) {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            _uiState.value = SkillsUiState(isSaving = true)

            // 1. Leer perfil actual para no sobreescribir campos existentes
            val userResult = authRepository.getUser(userId)
            if (userResult.isFailure) {
                _uiState.value = SkillsUiState(
                    errorMessage = "No se pudo cargar el perfil. Intenta de nuevo."
                )
                return@launch
            }
            val user = userResult.getOrNull()!!

            // 2. Guardar con los mismos valores actuales + skills nuevas
            authRepository.updateUser(
                userId     = userId,
                name       = user.name,
                phone      = user.phone,
                skills     = selectedSkills,
                experience = user.experience,
                district   = user.district
            )
                .onSuccess { _uiState.value = SkillsUiState(saveSuccess = true) }
                .onFailure { _uiState.value = SkillsUiState(
                    errorMessage = it.message ?: "No se pudieron guardar las habilidades."
                )}
        }
    }
}