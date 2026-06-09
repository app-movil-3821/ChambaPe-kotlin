package com.example.chambape.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ConversationItem(
    val conversationId: String,
    val jobId: String,
    val jobTitle: String,
    val createdAt: String
)

data class MessagesUiState(
    val isLoading: Boolean = false,
    val conversations: List<ConversationItem> = emptyList(),
    val errorMessage: String? = null
)

class MessagesViewModel : ViewModel() {

    private val messageRepository = AppModule.messageRepository
    private val jobRepository     = AppModule.jobRepository
    private val tokenManager      = AppModule.tokenManager

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    fun load() {
        val userId = tokenManager.getUserId() ?: run {
            _uiState.value = MessagesUiState(errorMessage = "No se encontró tu sesión.")
            return
        }
        viewModelScope.launch {
            _uiState.value = MessagesUiState(isLoading = true)
            messageRepository.getConversations(userId)
                .onSuccess { conversations ->
                    val items = conversations.map { conv ->
                        async {
                            val title = jobRepository.getJobById(conv.jobId)
                                .getOrNull()?.title ?: "Conversación"
                            ConversationItem(
                                conversationId = conv.id,
                                jobId          = conv.jobId,
                                jobTitle       = title,
                                createdAt      = conv.createdAt
                            )
                        }
                    }.awaitAll()
                    _uiState.value = MessagesUiState(conversations = items)
                }
                .onFailure { error ->
                    _uiState.value = MessagesUiState(
                        errorMessage = error.message ?: "Error al cargar mensajes."
                    )
                }
        }
    }
}
