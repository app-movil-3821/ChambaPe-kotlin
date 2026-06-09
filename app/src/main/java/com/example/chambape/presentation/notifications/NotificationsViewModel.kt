package com.example.chambape.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val errorMessage: String? = null
)

class NotificationsViewModel : ViewModel() {

    private val notificationRepository = AppModule.notificationRepository
    private val tokenManager           = AppModule.tokenManager

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    fun load() {
        val userId = tokenManager.getUserId() ?: run {
            _uiState.value = NotificationsUiState(errorMessage = "No se encontró tu sesión.")
            return
        }
        viewModelScope.launch {
            _uiState.value = NotificationsUiState(isLoading = true)
            notificationRepository.getNotifications(userId)
                .onSuccess { notifications ->
                    _uiState.value = NotificationsUiState(
                        notifications = notifications.sortedByDescending { it.createdAt }
                    )
                }
                .onFailure { error ->
                    _uiState.value = NotificationsUiState(
                        errorMessage = error.message ?: "Error al cargar notificaciones."
                    )
                }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(notificationId)
            _uiState.value = _uiState.value.copy(
                notifications = _uiState.value.notifications.map { n ->
                    if (n.id == notificationId) n.copy(read = true) else n
                }
            )
        }
    }

    fun markAllAsRead() {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            notificationRepository.markAllAsRead(userId)
            _uiState.value = _uiState.value.copy(
                notifications = _uiState.value.notifications.map { it.copy(read = true) }
            )
        }
    }
}
