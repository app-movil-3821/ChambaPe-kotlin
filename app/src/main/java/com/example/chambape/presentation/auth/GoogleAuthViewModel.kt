package com.example.chambape.presentation.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val WEB_CLIENT_ID =
    "18412931124-ehfh5ujsq2agndl3kcekmg8u8jsa9emj.apps.googleusercontent.com"

data class GoogleAuthUiState(
    val isLoading    : Boolean = false,
    val success      : Boolean = false,
    val errorMessage : String? = null
)

class GoogleAuthViewModel : ViewModel() {

    private val authRepository = AppModule.authRepository

    private val _uiState = MutableStateFlow(GoogleAuthUiState())
    val uiState: StateFlow<GoogleAuthUiState> = _uiState.asStateFlow()

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _uiState.value = GoogleAuthUiState(isLoading = true)
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(WEB_CLIENT_ID)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val credentialManager = CredentialManager.create(context)
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                // Enviar al backend
                authRepository.googleAuth(idToken)
                    .onSuccess {
                        AppModule.tokenManager.setNewUser(true)
                        _uiState.value = GoogleAuthUiState(success = true)
                    }
                    .onFailure { e ->
                        _uiState.value = GoogleAuthUiState(
                            errorMessage = e.message ?: "Error al autenticar con Google."
                        )
                    }

            } catch (e: Exception) {
                _uiState.value = GoogleAuthUiState(
                    errorMessage = "No se pudo iniciar sesión con Google."
                )
            }
        }
    }

    fun resetState() { _uiState.value = GoogleAuthUiState() }
}