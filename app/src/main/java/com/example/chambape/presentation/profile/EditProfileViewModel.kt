package com.example.chambape.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val skills: List<String> = emptyList(),
    val experience: String = "",
    val district: String = "",
    val errorMessage: String? = null
)

class EditProfileViewModel : ViewModel() {

    private val authRepository = AppModule.authRepository
    private val tokenManager   = AppModule.tokenManager

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    fun load() {
        val userId = tokenManager.getUserId() ?: run {
            _uiState.value = EditProfileUiState(errorMessage = "No se encontró tu sesión.")
            return
        }
        viewModelScope.launch {
            _uiState.value = EditProfileUiState(isLoading = true)
            authRepository.getUser(userId)
                .onSuccess { user ->
                    _uiState.value = EditProfileUiState(
                        name       = user.name,
                        email      = user.email,
                        phone      = user.phone,
                        skills     = user.skills,
                        experience = user.experience,
                        district   = user.district
                    )
                }
                .onFailure { error ->
                    _uiState.value = EditProfileUiState(
                        errorMessage = error.message ?: "Error al cargar el perfil."
                    )
                }
        }
    }

    fun onNameChange(value: String)       { _uiState.value = _uiState.value.copy(name = value, errorMessage = null) }
    fun onPhoneChange(value: String)      { _uiState.value = _uiState.value.copy(phone = value, errorMessage = null) }
    fun onDistrictChange(value: String)   { _uiState.value = _uiState.value.copy(district = value, errorMessage = null) }
    fun onExperienceChange(value: String) { _uiState.value = _uiState.value.copy(experience = value, errorMessage = null) }

    fun onSkillToggle(skill: String) {
        val current = _uiState.value.skills.toMutableList()
        if (skill in current) current.remove(skill) else current.add(skill)
        _uiState.value = _uiState.value.copy(skills = current, errorMessage = null)
    }

    fun save() {
        val userId = tokenManager.getUserId() ?: return
        val state  = _uiState.value
        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, errorMessage = null)
            authRepository.updateUser(
                userId     = userId,
                name       = state.name.trim(),
                phone      = state.phone.trim(),
                skills     = state.skills,
                experience = state.experience,
                district   = state.district
            )
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isSaving = false, saveSuccess = true)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isSaving     = false,
                        errorMessage = error.message ?: "No se pudo guardar el perfil."
                    )
                }
        }
    }
}