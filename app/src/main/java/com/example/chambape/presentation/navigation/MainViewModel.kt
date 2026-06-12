package com.example.chambape.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chambape.data.repository.TokenManager
import com.example.chambape.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Expone el rol del usuario actual ("CONTRATANTE" / "CHAMBEADOR") para que la
 * barra inferior pueda mostrar "Jobs" o "Shifts" y la pestaña cargue la pantalla
 * correcta según el rol.
 *
 * NOTA: por ahora lee el rol vía getUser (igual que HomeFeed). Cuando se acuerde
 * cachear el rol en TokenManager, basta cambiar el cuerpo de loadRole().
 */
class MainViewModel(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _userRole = MutableStateFlow("")
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    init {
        loadRole()
    }

    private fun loadRole() {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            authRepository.getUser(userId)
                .onSuccess { _userRole.value = it.role.uppercase() }
        }
    }
}

class MainViewModelFactory(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(authRepository, tokenManager) as T
    }
}