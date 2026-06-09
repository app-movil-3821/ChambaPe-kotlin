package com.example.chambape.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<Message> = emptyList(),
    val jobTitle: String = "",
    val sendError: String? = null
)

class ChatViewModel : ViewModel() {

    private val messageRepository = AppModule.messageRepository
    private val jobRepository     = AppModule.jobRepository
    private val tokenManager      = AppModule.tokenManager

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    val currentUserId: String get() = tokenManager.getUserId() ?: ""

    fun load(conversationId: String, jobId: String) {
        viewModelScope.launch {
            _uiState.value = ChatUiState(isLoading = true)
            val title = jobRepository.getJobById(jobId).getOrNull()?.title ?: ""
            messageRepository.getMessages(conversationId)
                .onSuccess { messages ->
                    _uiState.value = ChatUiState(messages = messages, jobTitle = title)
                }
                .onFailure { error ->
                    _uiState.value = ChatUiState(
                        jobTitle     = title,
                        sendError    = error.message ?: "Error al cargar mensajes."
                    )
                }
        }
    }

    fun send(conversationId: String, content: String) {
        val senderId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            messageRepository.sendMessage(conversationId, senderId, content)
                .onSuccess { newMessage ->
                    _uiState.value = _uiState.value.copy(
                        messages  = _uiState.value.messages + newMessage,
                        sendError = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        sendError = error.message ?: "No se pudo enviar el mensaje."
                    )
                }
        }
    }
}
