package com.example.chambape.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaBlue     = Color(0xFF1A3FD8)
private val BackgroundGray = Color(0xFFF7F8FC)
private val TextPrimary    = Color(0xFF0D0D0D)
private val TextSecondary  = Color(0xFF6B6B6B)

@Composable
fun ProfileScreen(
    onGoToEditProfile: () -> Unit = {},
    onGoToMyShifts:    () -> Unit = {},
    onGoToSettings:    () -> Unit = {},
    onGoToWallet:      () -> Unit = {},
    onGoToSkills:      () -> Unit = {},
    onLogout:          () -> Unit = {}
) {
    val selectedSkills = remember { mutableStateOf(listOf("Mesero", "Cajero")) }

    Surface(modifier = Modifier.fillMaxSize(), color = BackgroundGray) {
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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.Menu, contentDescription = null, tint = TextPrimary)
                }
                Text(text = "ChambaYa", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = ChambaBlue)
                Box {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = null, tint = TextPrimary)
                    }
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Red).align(Alignment.TopEnd))
                }
            }

            Spacer(Modifier.height(24.dp))

            // Avatar — tap para editar perfil
            Column(modifier = Modifier.fillMaxWidth().clickable { onGoToEditProfile() }, horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier.size(90.dp).clip(CircleShape).background(Color(0xFFE8EDFB)).border(2.dp, ChambaBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Text(text = "😊", fontSize = 44.sp) }
                    Box(
                        modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFF22C55E)),
                        contentAlignment = Alignment.Center
                    ) { Text(text = "✓", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold) }
                }
                Spacer(Modifier.height(12.dp))
                Text(text = "Diego", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFFFBBC04), modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(text = "4.8 (124 reseñas)", fontSize = 14.sp, color = TextSecondary)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Habilidades
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Habilidades", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    IconButton(
                        onClick  = { onGoToSkills() },
                        modifier = Modifier.size(32.dp).clip(CircleShape).background(ChambaBlue)
                    ) {
                        Icon(Icons.Outlined.Add, contentDescription = "Agregar habilidad", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    selectedSkills.value.forEachIndexed { index, skill ->
                        val isFirst = index == 0
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isFirst) ChambaBlue else Color.White)
                                .border(1.dp, if (isFirst) Color.Transparent else Color(0xFFDDDDDD), RoundedCornerShape(12.dp))
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = if (skill == "Mesero") "🍽️" else "🏧", fontSize = 16.sp)
                                Spacer(Modifier.width(8.dp))
                                Text(text = skill, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = if (isFirst) Color.White else TextPrimary)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Gestión
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text(text = "Gestión", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(Modifier.height(10.dp))
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
                    Column {
                        ProfileMenuItem(icon = Icons.Outlined.Work,       label = "Mis Turnos",    onClick = { onGoToMyShifts() })
                        MenuDivider()
                        ProfileMenuItem(icon = Icons.Outlined.CreditCard,  label = "Billetera",     onClick = { onGoToWallet() })
                        MenuDivider()
                        ProfileMenuItem(icon = Icons.Outlined.Settings,    label = "Configuración", onClick = { onGoToSettings() })
                    }
                }
                Spacer(Modifier.height(16.dp))
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
                    ProfileMenuItem(icon = Icons.Outlined.ExitToApp, label = "Cerrar sesión", labelColor = Color(0xFFD93025), iconColor = Color(0xFFD93025), onClick = { onLogout() })
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    labelColor: Color = Color(0xFF0D0D0D),
    iconColor: Color  = Color(0xFF1A3FD8),
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) { Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp)) }
            Spacer(Modifier.width(14.dp))
            Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = labelColor)
        }
        Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun MenuDivider() {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(1.dp).background(Color(0xFFF0F0F0)))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}