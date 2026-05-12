package com.example.chambape.presentation.shifts

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Brand colors ────────────────────────────────────────────────────────────
private val ChambaBlue      = Color(0xFF1A3FD8)
private val BackgroundGray  = Color(0xFFF7F8FC)
private val GreenCompleted  = Color(0xFF2E7D32)
private val GreenBg         = Color(0xFFE8F5E9)
private val DarkCard        = Color(0xFF1A2340)

// ─── Domain model ─────────────────────────────────────────────────────────────
data class CompletedShift(
    val id: String,
    val role: String,
    val company: String,
    val date: String,
    val hours: Int,
    val payment: Double,
    val status: String = "Completado"
)

private val sampleShifts = listOf(
    CompletedShift("1", "Asistente de Logística", "Almacenes Global S.A.", "15 Oct, 2023", 8,  1200.00),
    CompletedShift("2", "Mesero de Eventos",      "Catering Luxury",       "12 Oct, 2023", 6,   950.00),
    CompletedShift("3", "Repartidor de Paquetería","FastExpress S.L.",     "10 Oct, 2023", 5,   780.00),
)

// ─── Main screen ──────────────────────────────────────────────────────────────
@Composable
fun MyShiftsScreen(
    onShiftClick: (shiftId: String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = BackgroundGray
    ) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Top app bar
            item { ShiftsTopBar() }

            // Page title
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text       = "Mis Turnos",
                        fontSize   = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color(0xFF0D0D0D)
                    )
                    Text(
                        text     = "Historial de tus trabajos realizados y ganancias.",
                        fontSize = 13.sp,
                        color    = Color(0xFF6B6B6B)
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }

            // Earnings card
            item {
                TotalEarningsCard(
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(16.dp))
            }

            // Stats row
            item {
                StatsRow(
                    totalShifts = sampleShifts.size + 21, // 24 total as in design
                    rating      = 4.9f,
                    modifier    = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(24.dp))
            }

            // Shift cards
            items(sampleShifts) { shift ->
                ShiftCard(
                    shift    = shift,
                    onClick  = { onShiftClick(shift.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))
            }

            // Promo / upsell banner
            item {
                Spacer(Modifier.height(8.dp))
                PromoBanner(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

// ─── Top bar ──────────────────────────────────────────────────────────────────
@Composable
private fun ShiftsTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector        = Icons.Outlined.Menu,
                contentDescription = "Menú",
                tint               = Color(0xFF0D0D0D)
            )
        }
        Text(
            text       = "ChambaYa",
            fontSize   = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color      = ChambaBlue
        )
        Box {
            IconButton(onClick = { }) {
                Icon(
                    imageVector        = Icons.Outlined.Notifications,
                    contentDescription = "Notificaciones",
                    tint               = ChambaBlue
                )
            }
            // Notification badge
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

// ─── Total earnings card ───────────────────────────────────────────────────────
@Composable
private fun TotalEarningsCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF1A3FD8), Color(0xFF2D5BE3))
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Text(
                text     = "Ganancias Totales",
                fontSize = 13.sp,
                color    = Color.White.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text       = "\$12,450.00",
                fontSize   = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White
            )
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector        = Icons.Outlined.TrendingUp,
                    contentDescription = null,
                    tint               = Color(0xFF69F0AE),
                    modifier           = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text     = "+12% este mes",
                    fontSize = 13.sp,
                    color    = Color(0xFF69F0AE),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ─── Stats row ────────────────────────────────────────────────────────────────
@Composable
private fun StatsRow(
    totalShifts: Int,
    rating: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Shifts card
        Card(
            modifier  = Modifier.weight(1f),
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text     = "Turnos",
                    fontSize = 12.sp,
                    color    = Color(0xFF9E9E9E)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text       = totalShifts.toString(),
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = Color(0xFF0D0D0D)
                )
            }
        }

        // Rating card
        Card(
            modifier  = Modifier.weight(1f),
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text     = "Calificación",
                    fontSize = 12.sp,
                    color    = Color(0xFF9E9E9E)
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text       = rating.toString(),
                        fontSize   = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color(0xFF0D0D0D)
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector        = Icons.Outlined.Star,
                        contentDescription = null,
                        tint               = Color(0xFFFFC107),
                        modifier           = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

// ─── Shift card ───────────────────────────────────────────────────────────────
@Composable
private fun ShiftCard(
    shift: CompletedShift,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick   = onClick,   // ← agrega esto
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title + status badge
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = shift.role,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF0D0D0D)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text     = shift.company,
                        fontSize = 13.sp,
                        color    = Color(0xFF6B6B6B)
                    )
                }
                // Status chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(GreenBg)
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text       = shift.status,
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = GreenCompleted
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Divider(color = Color(0xFFF0F0F0))
            Spacer(Modifier.height(12.dp))

            // Date + hours
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Outlined.CalendarToday,
                        contentDescription = null,
                        tint               = Color(0xFF9E9E9E),
                        modifier           = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text     = shift.date,
                        fontSize = 12.sp,
                        color    = Color(0xFF6B6B6B)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint               = Color(0xFF9E9E9E),
                        modifier           = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text     = "${shift.hours} hrs",
                        fontSize = 12.sp,
                        color    = Color(0xFF6B6B6B)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Payment row
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text     = "Pago recibido",
                    fontSize = 13.sp,
                    color    = Color(0xFF6B6B6B)
                )
                Text(
                    text       = "\$%,.2f".format(shift.payment),
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = ChambaBlue
                )
            }
        }
    }
}

// ─── Promo / upsell banner ────────────────────────────────────────────────────
@Composable
private fun PromoBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(DarkCard)
            .padding(20.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "¿Quieres ganar más?",
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text     = "Completa tu perfil de seguridad y accede a turnos premium.",
                    fontSize = 12.sp,
                    color    = Color.White.copy(alpha = 0.75f),
                    lineHeight = 17.sp
                )
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick        = { },
                    shape          = RoundedCornerShape(10.dp),
                    colors         = ButtonDefaults.buttonColors(containerColor = ChambaBlue),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text       = "Ver Requisitos",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color.White
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            // Worker silhouette placeholder
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text     = "👷",
                    fontSize = 40.sp
                )
            }
        }
    }
}
