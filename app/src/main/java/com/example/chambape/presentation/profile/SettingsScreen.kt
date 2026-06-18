package com.example.chambape.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Colors ───────────────────────────────────────────────────────────────────
private val ChambaBlue     = Color(0xFF1A3FD8)
private val BackgroundGray  = Color(0xFFF7F8FC)
private val TextPrimary     = Color(0xFF0D0D0D)
private val TextSecondary   = Color(0xFF6B6B6B)

// ─── Screen ───────────────────────────────────────────────────────────────────
@Composable
fun SettingsScreen(
    onBack           : () -> Unit = {},
    onLogout         : () -> Unit = {},
    onGoToEditProfile: () -> Unit = {}
) {
    val viewModel: SettingsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showLogoutDialog   by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = BackgroundGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector        = Icons.Outlined.ArrowBack,
                        contentDescription = "Volver",
                        tint               = ChambaBlue
                    )
                }
                Text(
                    text       = "Configuración",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = TextPrimary
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Cuenta ───────────────────────────────────────────────────────
            SectionTitle("Cuenta")
            SettingsCard {
                NavRow(Icons.Outlined.Person, "Editar perfil") { onGoToEditProfile() }
                ThinDivider()
                NavRow(Icons.Outlined.Lock, "Cambiar contraseña") {}
            }

            Spacer(Modifier.height(20.dp))

            // ── Notificaciones ───────────────────────────────────────────────
            SectionTitle("Notificaciones")
            SettingsCard {
                ToggleRow(
                    icon    = Icons.Outlined.Notifications,
                    label   = "Notificaciones push",
                    checked = uiState.pushNotifications,
                    onCheckedChange = { viewModel.setPushNotifications(it) }
                )
                ThinDivider()
                ToggleRow(
                    icon    = Icons.Outlined.Email,
                    label   = "Avisos por correo",
                    checked = uiState.emailNotifications,
                    onCheckedChange = { viewModel.setEmailNotifications(it) }
                )
                ThinDivider()
                ToggleRow(
                    icon    = Icons.Outlined.WorkOutline,
                    label   = "Turnos cercanos a mí",
                    checked = uiState.nearbyShifts,
                    onCheckedChange = { viewModel.setNearbyShifts(it) }
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Preferencias ─────────────────────────────────────────────────
            SectionTitle("Preferencias")
            SettingsCard {
                ToggleRow(
                    icon    = Icons.Outlined.DarkMode,
                    label   = "Modo oscuro",
                    checked = uiState.darkMode,
                    onCheckedChange = { viewModel.setDarkMode(it) }
                )
                ThinDivider()
                NavRow(
                    icon     = Icons.Outlined.Language,
                    label    = "Idioma",
                    trailing = uiState.language,
                    onClick  = { showLanguageDialog = true }
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Soporte ──────────────────────────────────────────────────────
            SectionTitle("Soporte")
            SettingsCard {
                NavRow(Icons.Outlined.HelpOutline, "Centro de ayuda") {}
                ThinDivider()
                NavRow(Icons.Outlined.PrivacyTip, "Privacidad y términos") {}
            }

            Spacer(Modifier.height(20.dp))

            // ── Cerrar sesión ────────────────────────────────────────────────
            SettingsCard {
                NavRow(
                    icon       = Icons.Outlined.ExitToApp,
                    label      = "Cerrar sesión",
                    iconTint   = Color(0xFFE53935),
                    labelColor = Color(0xFFE53935),
                    showChevron = false,
                    onClick    = { showLogoutDialog = true }
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text     = "ChambaYa v1.0.0",
                fontSize = 12.sp,
                color    = Color(0xFFAAAAAA),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }

    // Diálogo de idioma (funcional: cambia el valor mostrado).
    if (showLanguageDialog) {
        val options = listOf("Español", "English", "Português")
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Idioma") },
            text = {
                Column {
                    options.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setLanguage(option)
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text       = option,
                                fontSize   = 15.sp,
                                color      = if (option == uiState.language) ChambaBlue else TextPrimary,
                                fontWeight = if (option == uiState.language) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) { Text("Cerrar") }
            }
        )
    }

    // Diálogo de cierre de sesión.
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text  = { Text("¿Seguro que quieres cerrar tu sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Cerrar sesión", color = Color(0xFFE53935), fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

// ─── Reusable pieces ──────────────────────────────────────────────────────────
@Composable
private fun SectionTitle(text: String) {
    Text(
        text       = text,
        fontSize   = 14.sp,
        fontWeight = FontWeight.Bold,
        color      = TextSecondary,
        modifier   = Modifier.padding(start = 20.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column { content() }
    }
}

@Composable
private fun ThinDivider() {
    Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
private fun ToggleRow(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(icon)
            Spacer(Modifier.width(14.dp))
            Text(label, fontSize = 15.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
        }
        Switch(
            checked         = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ChambaBlue
            )
        )
    }
}

@Composable
private fun NavRow(
    icon: ImageVector,
    label: String,
    trailing: String? = null,
    iconTint: Color = ChambaBlue,
    labelColor: Color = TextPrimary,
    showChevron: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(icon, tint = iconTint)
            Spacer(Modifier.width(14.dp))
            Text(label, fontSize = 15.sp, color = labelColor, fontWeight = FontWeight.Medium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (trailing != null) {
                Text(trailing, fontSize = 14.sp, color = TextSecondary)
                Spacer(Modifier.width(6.dp))
            }
            if (showChevron) {
                Icon(
                    imageVector        = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint               = Color(0xFFBBBBBB),
                    modifier           = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun IconBadge(icon: ImageVector, tint: Color = ChambaBlue) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (tint == Color(0xFFE53935)) Color(0xFFFFEEEE) else Color(0xFFE8EEFF)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = tint,
            modifier           = Modifier.size(19.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen()
}