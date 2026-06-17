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
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val ChambaBlue     = Color(0xFF1A3FD8)
private val BackgroundGray = Color(0xFFF7F8FC)
private val DarkCard       = Color(0xFF1A2340)

@Composable
fun MyShiftsScreen(
    onShiftClick:         (jobId: String) -> Unit,
    onShiftCompleted:     (jobId: String) -> Unit = {},
    onBack:               (() -> Unit)? = null,
    onNotificationsClick: (() -> Unit)? = null
) {
    val viewModel: MyShiftsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.load() }

    Surface(modifier = Modifier.fillMaxSize(), color = BackgroundGray) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item { ShiftsTopBar(onBack = onBack, onNotificationsClick = onNotificationsClick) }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
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

            when {
                uiState.isLoading -> {
                    item {
                        Box(
                            modifier         = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = ChambaBlue)
                                Spacer(Modifier.height(16.dp))
                                Text("Cargando turnos...", fontSize = 14.sp, color = Color(0xFF6B6B6B))
                            }
                        }
                    }
                }

                uiState.errorMessage != null -> {
                    item {
                        Box(
                            modifier         = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text      = uiState.errorMessage ?: "Error al cargar tus turnos.",
                                fontSize  = 15.sp,
                                color     = Color(0xFFD93025),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                uiState.shifts.isEmpty() -> {
                    item {
                        Box(
                            modifier         = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text      = "Aún no tienes turnos registrados.",
                                fontSize  = 15.sp,
                                color     = Color(0xFF6B6B6B),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                else -> {
                    val completedTotal = uiState.shifts
                        .filter { it.job?.status == "COMPLETED" }
                        .sumOf { it.job?.paymentAmount ?: 0.0 }

                    item {
                        TotalEarningsCard(
                            total    = completedTotal,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    item {
                        StatsRow(
                            totalShifts = uiState.shifts.size,
                            modifier    = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(24.dp))
                    }

                    items(uiState.shifts) { shiftWithJob ->
                        ShiftCard(
                            shiftWithJob     = shiftWithJob,
                            onClick          = { onShiftClick(shiftWithJob.shift.jobId) },
                            onClickCompleted = { onShiftCompleted(shiftWithJob.shift.jobId) },
                            modifier         = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    item {
                        Spacer(Modifier.height(8.dp))
                        PromoBanner(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ShiftsTopBar(
    onBack:               (() -> Unit)? = null,
    onNotificationsClick: (() -> Unit)? = null
) {
    Row(
        modifier              = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onBack?.invoke() }) {
            Icon(
                imageVector        = if (onBack != null) Icons.Outlined.ArrowBack else Icons.Outlined.Menu,
                contentDescription = null,
                tint               = Color(0xFF0D0D0D)
            )
        }
        Text(text = "ChambaYa", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = ChambaBlue)
        Box {
            IconButton(onClick = { onNotificationsClick?.invoke() }) {
                Icon(Icons.Outlined.Notifications, "Notificaciones", tint = ChambaBlue)
            }
            Box(
                modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Red).align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
private fun TotalEarningsCard(total: Double, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF1A3FD8), Color(0xFF2D5BE3))))
            .padding(24.dp)
    ) {
        Column {
            Text("Ganancias Totales", fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
            Spacer(Modifier.height(8.dp))
            Text(
                text       = "S/ ${"%.2f".format(total)}",
                fontSize   = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White
            )
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.TrendingUp, null, tint = Color(0xFF69F0AE), modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Turnos completados", fontSize = 13.sp, color = Color(0xFF69F0AE), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun StatsRow(totalShifts: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            modifier  = Modifier.weight(1f),
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Turnos", fontSize = 12.sp, color = Color(0xFF9E9E9E))
                Spacer(Modifier.height(4.dp))
                Text(totalShifts.toString(), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0D0D0D))
            }
        }
        Card(
            modifier  = Modifier.weight(1f),
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Calificación", fontSize = 12.sp, color = Color(0xFF9E9E9E))
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("4.9", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0D0D0D))
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Outlined.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}

@Composable
private fun ShiftCard(
    shiftWithJob: ShiftWithJob,
    onClick: () -> Unit,
    onClickCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shift = shiftWithJob.shift
    val job   = shiftWithJob.job

    // El estado visible para el chambeador se deriva del job, no de la postulación.
    // La postulación solo tiene PENDING/ACCEPTED/REJECTED/CANCELLED; el progreso
    // real del trabajo vive en el job (IN_PROGRESS, COMPLETED, etc).
    val displayStatus = when {
        job?.status == "IN_PROGRESS" -> "IN_PROGRESS"
        job?.status == "COMPLETED"   -> "COMPLETED"
        job?.status == "CANCELLED"   -> "CANCELLED"
        shift.status == "ACCEPTED"   -> "ACCEPTED"
        shift.status == "REJECTED"   -> "REJECTED"
        else                         -> shift.status  // PENDING u otros
    }
    val (statusLabel, statusColor, statusBg) = shiftStatusStyle(displayStatus)

    Card(
        onClick   = if (displayStatus == "COMPLETED") onClickCompleted else onClick,
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = job?.title ?: "Turno",
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF0D0D0D)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text     = job?.category ?: shift.jobId.take(8),
                        fontSize = 13.sp,
                        color    = Color(0xFF6B6B6B)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(statusBg)
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(text = statusLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = statusColor)
                }
            }

            Spacer(Modifier.height(12.dp))
            Divider(color = Color(0xFFF0F0F0))
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CalendarToday, null, tint = Color(0xFF9E9E9E), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(formatShiftDate(shift.appliedAt), fontSize = 12.sp, color = Color(0xFF6B6B6B))
                }
                if (job != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Schedule, null, tint = Color(0xFF9E9E9E), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(calculateDuration(job.scheduledStart, job.scheduledEnd), fontSize = 12.sp, color = Color(0xFF6B6B6B))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Pago recibido", fontSize = 13.sp, color = Color(0xFF6B6B6B))
                Text(
                    text       = "S/ ${"%.2f".format(job?.paymentAmount ?: 0.0)}",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = ChambaBlue
                )
            }
        }
    }
}

@Composable
private fun PromoBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(DarkCard).padding(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("¿Quieres ganar más?", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Completa tu perfil de seguridad y accede a turnos premium.",
                    fontSize   = 12.sp,
                    color      = Color.White.copy(alpha = 0.75f),
                    lineHeight = 17.sp
                )
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick        = { },
                    shape          = RoundedCornerShape(10.dp),
                    colors         = ButtonDefaults.buttonColors(containerColor = ChambaBlue),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text("Ver Requisitos", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
            Spacer(Modifier.width(16.dp))
            Box(
                modifier         = Modifier.size(width = 80.dp, height = 100.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👷", fontSize = 40.sp)
            }
        }
    }
}

private fun shiftStatusStyle(status: String): Triple<String, Color, Color> = when (status) {
    "PENDING"     -> Triple("Pendiente",   Color(0xFF7B5800), Color(0xFFFFF3CD))
    "MATCHED",
    "ACCEPTED"    -> Triple("Asignado",    Color(0xFF1A3FD8), Color(0xFFE8EDFF))
    "IN_PROGRESS" -> Triple("En progreso", Color(0xFF7B3F00), Color(0xFFFFEDD5))
    "COMPLETED"   -> Triple("Completado",  Color(0xFF2E7D32), Color(0xFFE8F5E9))
    "CANCELLED"   -> Triple("Cancelado",   Color(0xFF6B6B6B), Color(0xFFF0F0F0))
    "REJECTED"    -> Triple("Rechazado",   Color(0xFFD93025), Color(0xFFFFEBEA))
    else          -> Triple(status,        Color(0xFF6B6B6B), Color(0xFFF0F0F0))
}

private fun formatShiftDate(isoDate: String): String {
    return if (isoDate.length >= 10) isoDate.substring(0, 10) else isoDate
}

private fun calculateDuration(start: String, end: String): String {
    return try {
        val startHour = start.substring(11, 13).toInt()
        val endHour   = end.substring(11, 13).toInt()
        val diff      = endHour - startHour
        if (diff > 0) "$diff hrs" else "–"
    } catch (e: Exception) {
        "–"
    }
}