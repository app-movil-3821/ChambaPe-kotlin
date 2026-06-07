package com.example.chambape.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

//private val ChambaPeBlue  = Color(0xFF1A3FD8)
private val BackgroundGray = Color(0xFFF7F8FC)

private val ChambaPeBlue = Color(0xFF0B57D0)
private val TextPrimary = Color(0xFF1D1B20)
private val TextSecondary = Color(0xFF49454F)

// Modelo de dato local para preview
data class JobItem(
    val id: String,
    val title: String,
    val company: String,
    val pricePerHour: Int,
    val distance: String,
    val isUrgent: Boolean,
    val isAvailable: Boolean
)

private val sampleJobs = listOf(
    JobItem("1", "Mesero",          "Café Central",      25, "0.5km", false, true),
    JobItem("2", "Cajero",          "Minimarket Solo",   22, "1.2km", true,  true),
    JobItem("3", "Repartidor",      "FastExpress S.L.",  20, "2.0km", false, true),
    JobItem("4", "Asistente Cocina","La Trattoria",      18, "0.8km", true,  true),
)

@Composable
fun HomeFeedScreen(
    viewModel: HomeFeedViewModel = viewModel(),
    onJobClick: (jobId: String) -> Unit
) {
    var searchQuery     by remember { mutableStateOf("") }
    var selectedFilter  by remember { mutableStateOf("Cerca") }
    val filters = listOf("Cerca", "Urgente", "Hoy")

    val jobsState by viewModel.jobs.collectAsState()
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = BackgroundGray
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(jobsState) { job ->
                // Aquí pegamos el diseño exacto que tus compañeros ya habían hecho,
                // pero reemplazando los textos estáticos por las propiedades de "job"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Título de la chamba y el botón de guardar (corazón)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = job.title, // <- Dato real
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "Guardar",
                                tint = TextSecondary,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickable { /* Acción para guardar (futuro Room) */ }
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        // Fila de Distrito y Pago
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = job.district, // <- Dato real
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                            Spacer(Modifier.width(16.dp))

                            // Agregamos un texto para el pago, ya que en el original solo estaba el ícono
                            Text(
                                text = "S/ ${job.paymentAmount}", // <- Dato real
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        // Fila de disponibilidad y Botón de Aplicar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Badge disponibilidad
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFFE8F5E9)) // Asumimos disponible siempre por ahora
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "● Available",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF43A047)
                                )
                            }

                            // Botón Aplicar
                            androidx.compose.material3.Button( // Evitamos conflicto de imports con Material2
                                onClick = { onJobClick(job.id) }, // <- Dato real para la navegación
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = ChambaPeBlue
                                ),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "Aplicar",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeFeedHeader() {
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
            color      = ChambaPeBlue
        )

        Box {
            IconButton(onClick = { }) {
                Icon(
                    imageVector        = Icons.Outlined.Notifications,
                    contentDescription = "Notificaciones",
                    tint               = Color(0xFF1A3FD8)
                )
            }
            // Badge rojo
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .align(Alignment.TopEnd)
                    .padding(end = 10.dp, top = 10.dp)
            )
        }
    }
}

@Composable
private fun MapPreview(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFD0E8E4), Color(0xFFB8D8D4))
                )
            )
    ) {
        // Simulación del mapa con puntos
        repeat(3) { i ->
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (i == 0) ChambaPeBlue else Color(0xFF2196F3))
                    .align(
                        when (i) {
                            0    -> Alignment.Center
                            1    -> Alignment.TopEnd
                            else -> Alignment.BottomStart
                        }
                    )
                    .padding(
                        end    = if (i == 1) 40.dp else 0.dp,
                        top    = if (i == 1) 30.dp else 0.dp,
                        start  = if (i == 2) 50.dp else 0.dp,
                        bottom = if (i == 2) 30.dp else 0.dp
                    )
            )
        }

        // Botón "Ver en mapa"
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector        = Icons.Outlined.Map,
                contentDescription = null,
                tint               = ChambaPeBlue,
                modifier           = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text     = "Ver en mapa",
                fontSize = 13.sp,
                color    = ChambaPeBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun JobCard(
    job: JobItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top
            ) {
                // Info izquierda
                Column {
                    Text(
                        text       = job.title,
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF0D0D0D)
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector        = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint               = Color(0xFF9E9E9E),
                            modifier           = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text     = job.company,
                            fontSize = 13.sp,
                            color    = Color(0xFF6B6B6B)
                        )
                    }
                }

                // Precio + distancia derecha
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text       = "\$${job.pricePerHour}/hr",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = ChambaPeBlue
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector        = Icons.Outlined.NearMe,
                            contentDescription = null,
                            tint               = Color(0xFF9E9E9E),
                            modifier           = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text     = job.distance,
                            fontSize = 12.sp,
                            color    = Color(0xFF9E9E9E)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Badge disponibilidad
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            if (job.isUrgent) Color(0xFFFFEEEE) else Color(0xFFE8F5E9)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text       = if (job.isUrgent) "⚡ Urgent" else "● Available",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = if (job.isUrgent) Color(0xFFE53935) else Color(0xFF43A047)
                    )
                }

                // Botón Aplicar
                Button(
                    onClick  = { onClick() },
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text       = "Aplicar",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color.White
                    )
                }
            }
        }
    }
}