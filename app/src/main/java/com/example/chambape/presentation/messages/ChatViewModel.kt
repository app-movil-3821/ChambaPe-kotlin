package com.example.chambape.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Message
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val POLL_INTERVAL_MS = 5_000L  // cada 5 segundos

data class ChatUiState(
    val isLoading : Boolean       = false,
    val messages  : List<Message> = emptyList(),
    val jobTitle  : String        = "",
    val sendError : String?       = null
)

class ChatViewModel : ViewModel() {

    private val messageRepository = AppModule.messageRepository
    private val jobRepository     = AppModule.jobRepository
    private val tokenManager      = AppModule.tokenManager

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    val currentUserId: String get() = tokenManager.getUserId() ?: ""

    private var pollingJob: Job? = null

    // ── Carga inicial + arranca polling ──────────────────────────────────────

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
                        jobTitle  = title,
                        sendError = error.message ?: "Error al cargar mensajes."
                    )
                }
        }
        startPolling(conversationId)
    }

    // ── Polling: refresca silenciosamente cada 5 segundos ────────────────────

    private fun startPolling(conversationId: String) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                delay(POLL_INTERVAL_MS)
                refreshSilently(conversationId)
            }
        }
    }

    private suspend fun refreshSilently(conversationId: String) {
        messageRepository.getMessages(conversationId)
            .onSuccess { freshMessages ->
                // Solo actualiza si llegaron mensajes nuevos para no redibujar innecesariamente
                if (freshMessages.size != _uiState.value.messages.size) {
                    _uiState.value = _uiState.value.copy(messages = freshMessages)
                }
            }
        // Si falla el refresh silencioso, no muestra error — espera el siguiente ciclo
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    // ── Enviar mensaje ────────────────────────────────────────────────────────

    fun send(conversationId: String, content: String) {
        val senderId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            messageRepository.sendMessage(conversationId, senderId, content)
                .onSuccess { newMessage ->
                    // Agrega el mensaje al estado local inmediatamente sin esperar el poll
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

    // ── Limpieza al salir ─────────────────────────────────────────────────────

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}