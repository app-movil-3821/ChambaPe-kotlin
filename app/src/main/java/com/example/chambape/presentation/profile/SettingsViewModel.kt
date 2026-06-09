package com.example.chambape.presentation.profile

import androidx.lifecycle.ViewModel
import com.example.chambape.data.repository.SettingsPreferences
import com.example.chambape.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    val pushNotifications:  Boolean = true,
    val emailNotifications: Boolean = false,
    val nearbyShifts:       Boolean = true,
    val darkMode:           Boolean = false,
    val language:           String  = "Español"
)

class SettingsViewModel : ViewModel() {

    private val prefs: SettingsPreferences = AppModule.settingsPreferences

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            pushNotifications  = prefs.pushNotifications,
            emailNotifications = prefs.emailNotifications,
            nearbyShifts       = prefs.nearbyShifts,
            darkMode           = prefs.darkMode,
            language           = prefs.language
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setPushNotifications(value: Boolean)  { prefs.pushNotifications  = value; update { it.copy(pushNotifications  = value) } }
    fun setEmailNotifications(value: Boolean) { prefs.emailNotifications = value; update { it.copy(emailNotifications = value) } }
    fun setNearbyShifts(value: Boolean)       { prefs.nearbyShifts       = value; update { it.copy(nearbyShifts       = value) } }
    fun setDarkMode(value: Boolean)           { prefs.darkMode           = value; update { it.copy(darkMode           = value) } }
    fun setLanguage(value: String)            { prefs.language           = value; update { it.copy(language           = value) } }

    private fun update(transform: (SettingsUiState) -> SettingsUiState) {
        _uiState.value = transform(_uiState.value)
    }
}
