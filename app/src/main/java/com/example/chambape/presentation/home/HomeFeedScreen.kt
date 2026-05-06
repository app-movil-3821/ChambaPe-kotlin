package com.example.chambape.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaPeBlue  = Color(0xFF1A3FD8)
private val BackgroundGray = Color(0xFFF7F8FC)

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
    onJobClick: (jobId: String) -> Unit
) {
    var searchQuery     by remember { mutableStateOf("") }
    var selectedFilter  by remember { mutableStateOf("Cerca") }
    val filters = listOf("Cerca", "Urgente", "Hoy")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = BackgroundGray
    ) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Header
            item {
                HomeFeedHeader()
            }

            // Saludo + búsqueda
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text       = "¡Hola, Diego!",
                        fontSize   = 30 .sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color(0xFF0D0D0D)
                    )
                    Text(
                        text     = "Encuentra tu próximo turno al instante.",
                        fontSize = 14.sp,
                        color    = Color(0xFF6B6B6B)
                    )
                    Spacer(Modifier.height(16.dp))

                    // Barra de búsqueda
                    OutlinedTextField(
                        value         = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier      = Modifier.fillMaxWidth(),
                        placeholder   = { Text("Buscar turnos...", color = Color(0xFFAAAAAA)) },
                        leadingIcon   = {
                            Icon(
                                imageVector        = Icons.Outlined.Search,
                                contentDescription = null,
                                tint               = Color(0xFFAAAAAA)
                            )
                        },
                        singleLine = true,
                        shape      = RoundedCornerShape(14.dp),
                        colors     = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor   = Color.White,
                            unfocusedBorderColor    = Color.Transparent,
                            focusedBorderColor      = ChambaPeBlue
                        )
                    )

                    Spacer(Modifier.height(14.dp))

                    // Filtros
                    Row(
                        modifier              = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filters.forEach { filter ->
                            val isSelected = filter == selectedFilter
                            FilterChip(
                                selected = isSelected,
                                onClick  = { selectedFilter = filter },
                                label    = { Text(filter, fontSize = 13.sp) },
                                leadingIcon = {
                                    val icon: ImageVector = when (filter) {
                                        "Cerca"   -> Icons.Outlined.LocationOn
                                        "Urgente" -> Icons.Outlined.Bolt
                                        else      -> Icons.Outlined.NearMe
                                    }
                                    Icon(
                                        imageVector        = icon,
                                        contentDescription = null,
                                        modifier           = Modifier.size(16.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor       = ChambaPeBlue,
                                    selectedLabelColor           = Color.White,
                                    selectedLeadingIconColor     = Color.White,
                                    containerColor               = Color.White,
                                    labelColor                   = Color(0xFF4B4B4B)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled          = true,
                                    selected         = isSelected,
                                    borderColor      = Color(0xFFDDDDDD),
                                    selectedBorderColor = Color.Transparent
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }

            // Mapa preview
            item {
                MapPreview(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(20.dp))
            }

            // Título sección + Ver todos
            item {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Turnos disponibles",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF0D0D0D)
                    )
                    TextButton(onClick = { }) {
                        Text(
                            text     = "Ver todos",
                            color    = ChambaPeBlue,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // Lista de turnos
            items(sampleJobs) { job ->
                JobCard(
                    job       = job,
                    onClick   = { onJobClick(job.id) },
                    modifier  = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))
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