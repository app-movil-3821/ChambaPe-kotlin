package com.example.chambape.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Colors ───────────────────────────────────────────────────────────────────
private val ChambaBlue    = Color(0xFF1A3FD8)
private val BackgroundGray = Color(0xFFF7F8FC)
private val SkillSelected  = Color(0xFF1A3FD8)
private val SkillUnselected = Color(0xFFEEEFF5)

// ─── Data ─────────────────────────────────────────────────────────────────────
data class Skill(val label: String, val emoji: String, val selected: Boolean)

// ─── Screen ───────────────────────────────────────────────────────────────────
@Composable
fun ProfileScreen(
    onMyShifts: () -> Unit = {},
    onWallet:   () -> Unit = {},
    onSettings: () -> Unit = {},
    onLogout:   () -> Unit = {}
) {
    // Habilidades como estado: tocar una alterna su selección.
    val skills = remember {
        mutableStateListOf(
            Skill("Mesero", "🍽️", selected = true),
            Skill("Cajero", "💰", selected = false),
        )
    }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = BackgroundGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ProfileTopBar()

            Spacer(Modifier.height(28.dp))

            // Avatar + name + rating
            Column(
                modifier            = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar with verified badge
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFCCCCCC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("😊", fontSize = 44.sp)
                    }
                    // Verified badge
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1DA1F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✓", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text       = "Diego",
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = Color(0xFF0D0D0D)
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Outlined.Star,
                        contentDescription = null,
                        tint               = Color(0xFFFFC107),
                        modifier           = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text     = "4.8 ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color    = Color(0xFF0D0D0D)
                    )
                    Text(
                        text     = "(124 reseñas)",
                        fontSize = 14.sp,
                        color    = Color(0xFF9E9E9E)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Habilidades section
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text       = "Habilidades",
                    fontSize   = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF0D0D0D)
                )
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    skills.forEachIndexed { index, skill ->
                        SkillChip(
                            skill    = skill,
                            modifier = Modifier.weight(1f),
                            onClick  = {
                                skills[index] = skill.copy(selected = !skill.selected)
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // Gestión section
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text       = "Gestión",
                    fontSize   = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF0D0D0D)
                )
                Spacer(Modifier.height(12.dp))

                Card(
                    shape     = RoundedCornerShape(16.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        ManagementRow(
                            icon    = Icons.Outlined.Work,
                            iconBg  = Color(0xFFE8EEFF),
                            iconTint = ChambaBlue,
                            label   = "Mis Turnos",
                            onClick = onMyShifts
                        )
                        Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
                        ManagementRow(
                            icon    = Icons.Outlined.AccountBalanceWallet,
                            iconBg  = Color(0xFFE8EEFF),
                            iconTint = ChambaBlue,
                            label   = "Billetera",
                            onClick = onWallet
                        )
                        Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
                        ManagementRow(
                            icon    = Icons.Outlined.Settings,
                            iconBg  = Color(0xFFE8EEFF),
                            iconTint = ChambaBlue,
                            label   = "Configuración",
                            onClick = onSettings
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Cerrar sesión — separate card, red tint
                Card(
                    shape     = RoundedCornerShape(16.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    ManagementRow(
                        icon     = Icons.Outlined.ExitToApp,
                        iconBg   = Color(0xFFFFEEEE),
                        iconTint = Color(0xFFE53935),
                        label    = "Cerrar sesión",
                        labelColor = Color(0xFFE53935),
                        showChevron = false,
                        onClick  = { showLogoutDialog = true }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    // Diálogo de confirmación de cierre de sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title   = { Text("Cerrar sesión") },
            text    = { Text("¿Seguro que quieres cerrar tu sesión?") },
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
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// ─── Components ───────────────────────────────────────────────────────────────
@Composable
private fun ProfileTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Outlined.Menu, contentDescription = "Menú", tint = Color(0xFF0D0D0D))
        }
        Text(
            text       = "ChambaYa",
            fontSize   = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color      = ChambaBlue
        )
        Box {
            IconButton(onClick = {}) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = ChambaBlue)
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
private fun SkillChip(
    skill: Skill,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (skill.selected) SkillSelected else SkillUnselected)
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(skill.emoji, fontSize = 18.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                text       = skill.label,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color      = if (skill.selected) Color.White else Color(0xFF4B4B4B)
            )
        }
    }
}

@Composable
private fun ManagementRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    labelColor: Color = Color(0xFF0D0D0D),
    showChevron: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = icon,
                    contentDescription = null,
                    tint               = iconTint,
                    modifier           = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Text(
                text       = label,
                fontSize   = 15.sp,
                fontWeight = FontWeight.Medium,
                color      = labelColor
            )
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
