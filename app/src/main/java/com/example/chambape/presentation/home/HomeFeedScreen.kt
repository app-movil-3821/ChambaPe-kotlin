package com.example.chambape.presentation.home

import com.example.chambape.di.AppModule
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// IMPORTACIONES DE GOOGLE MAPS
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private val BackgroundGray  = Color(0xFFF8FAFC)
private val ChambaPeBlue    = Color(0xFF0B57D0)
private val TextPrimary     = Color(0xFF1E293B)
private val TextSecondary   = Color(0xFF64748B)

private fun statusLabel(status: String) = when (status.uppercase()) {
    "PUBLISHED"   -> "Available"
    "URGENT"      -> "Urgent"
    else          -> "Available"
}

private fun statusBackgroundColor(status: String) = when (status.uppercase()) {
    "PUBLISHED"   -> Color(0xFFDCFCE7)
    "URGENT"      -> Color(0xFFFFE4E6)
    else          -> Color(0xFFDCFCE7)
}

private fun statusTextColor(status: String) = when (status.uppercase()) {
    "PUBLISHED"   -> Color(0xFF15803D)
    "URGENT"      -> Color(0xFFB91C1C)
    else          -> Color(0xFF15803D)
}

@Composable
fun HomeFeedScreen(
    viewModel: HomeFeedViewModel = viewModel(),
    onJobClick: (jobId: String) -> Unit,
    onNavigateToCreateJob: () -> Unit
) {
    val jobs         by viewModel.jobs.collectAsState()
    val isLoading    by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val userName     by viewModel.userName.collectAsState()
    val searchQuery  by viewModel.searchQuery.collectAsState()
    val categorySelected by viewModel.selectedCategory.collectAsState()
    val userRole by viewModel.userRole.collectAsState()

    var verMapa      by remember { mutableStateOf(false) }
    val limaCentro   = LatLng(-12.046374, -77.042793)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(limaCentro, 13f)
    }

    Scaffold(containerColor = BackgroundGray) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color    = BackgroundGray
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ChambaPeBlue)
                    }
                }

                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = errorMessage!!, fontSize = 15.sp, color = Color(0xFFD93025))
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadJobs() }, colors = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue)) {
                                Text("Reintentar", color = Color.White)
                            }
                        }
                    }
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize()) {

                        if (verMapa) {
                            // ─── VISTA 1: MAPA EN PANTALLA COMPLETA (Mantiene tu regla de UX) ───
                            Box(modifier = Modifier.fillMaxSize()) {
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraPositionState
                                ) {
                                    jobs.forEach { job ->
                                        // Saltamos jobs sin ubicación real (datos antiguos en 0.0/0.0)
                                        if (job.latitude != 0.0 || job.longitude != 0.0) {
                                            Marker(
                                                state = MarkerState(
                                                    position = LatLng(job.latitude, job.longitude)
                                                ),
                                                title = job.title,
                                                snippet = "S/ ${job.paymentAmount}"
                                            )
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = { verMapa = false },
                                    modifier = Modifier
                                        .padding(start = 16.dp, top = 32.dp)
                                        .background(Color.White, shape = RoundedCornerShape(50.dp))
                                        .size(44.dp)
                                ) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = TextPrimary)
                                }
                            }
                        } else {
                            // ─── VISTA 2: HOME FEED ESTILO FIGMA ───
                            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

                                // Cabecera fija (Saludo + Subtítulo de Figma)
                                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
                                    Text(
                                        text       = if (AppModule.tokenManager.isNewUser()) {
                                            AppModule.tokenManager.setNewUser(false)
                                            if (userName.isNotBlank()) "¡Bienvenido, $userName! 🎉" else "¡Bienvenido!"
                                        } else {
                                            if (userName.isNotBlank()) "¡Hola, $userName!" else "¡Hola!"
                                        },
                                        fontSize   = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color      = TextPrimary
                                    )
                                    Text(
                                        text     = "Encuentra tu próximo turno al instante.",
                                        fontSize = 14.sp,
                                        color    = TextSecondary,
                                        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                                    )

                                    // Buscador Estilo Figma
                                    OutlinedTextField(
                                        value         = searchQuery,
                                        onValueChange = { viewModel.onSearchQueryChange(it) },
                                        modifier      = Modifier.fillMaxWidth(),
                                        placeholder   = { Text("Buscar turnos...", fontSize = 14.sp, color = Color(0xFF94A3B8)) },
                                        leadingIcon   = { Icon(Icons.Outlined.Search, null, tint = Color(0xFF94A3B8)) },
                                        trailingIcon  = {
                                            if (searchQuery.isNotBlank()) {
                                                IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                                    Icon(Icons.Outlined.Close, null, tint = TextSecondary)
                                                }
                                            }
                                        },
                                        singleLine    = true,
                                        shape         = RoundedCornerShape(12.dp),
                                        colors        = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor = Color.White,
                                            unfocusedContainerColor = Color.White,
                                            focusedBorderColor = ChambaPeBlue,
                                            unfocusedBorderColor = Color(0xEFE0E0E0)
                                        )
                                    )

                                    Spacer(Modifier.height(14.dp))

                                    // Fila de Categorías Horizontales (Figma)
                                    Row(
                                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        CategoryChip(
                                            text = "💼 Todos",
                                            isSelected = categorySelected == "TODOS",
                                            onChipClick = { viewModel.onCategoryChange("TODOS") }
                                        )
                                        CategoryChip(
                                            text = "📍 Cerca",
                                            isSelected = categorySelected == "CERCA",
                                            onChipClick = {
                                                viewModel.onCategoryChange("CERCA")
                                                verMapa = true // Al tocar cerca, abre el mapa directamente
                                            }
                                        )
                                        CategoryChip(
                                            text = "⚡ Urgente",
                                            isSelected = categorySelected == "URGENTE",
                                            onChipClick = { viewModel.onCategoryChange("URGENTE") }
                                        )
                                        CategoryChip(
                                            text = "🕒 Hoy",
                                            isSelected = categorySelected == "HOY",
                                            onChipClick = { viewModel.onCategoryChange("HOY") }
                                        )
                                    }
                                }

                                // Contenido Scrolleable (Mapa estático + Tarjetas)
                                LazyColumn(
                                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {

                                    // Componente del mapa estático "Preview" de Figma
                                    item {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(140.dp)
                                                .clickable { verMapa = true },
                                            shape = RoundedCornerShape(16.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                        ) {
                                            Box(modifier = Modifier.fillMaxSize().clickable{verMapa = true}) {
                                                // Un GoogleMap real pero con controles desactivados actúa como preview estático
                                                GoogleMap(
                                                    modifier = Modifier.fillMaxSize(),
                                                    cameraPositionState = rememberCameraPositionState {
                                                        position = CameraPosition.fromLatLngZoom(limaCentro, 12f)
                                                    },
                                                    uiSettings = MapUiSettings(
                                                        zoomControlsEnabled = false,
                                                        scrollGesturesEnabled = false,
                                                        zoomGesturesEnabled = false
                                                    )
                                                )

                                                // Botón flotante blanco sobre el preview
                                                Row(
                                                    modifier = Modifier
                                                        .padding(12.dp)
                                                        .align(Alignment.BottomStart)
                                                        .background(Color.White, shape = RoundedCornerShape(50.dp))
                                                        .clickable { verMapa = true}
                                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(Icons.Filled.Map, null, tint = ChambaPeBlue, modifier = Modifier.size(16.dp))
                                                    Spacer(Modifier.width(6.dp))
                                                    Text("Ver en mapa", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ChambaPeBlue)
                                                }
                                            }
                                        }

                                        Spacer(Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("Turnos disponibles", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            Text("Ver todos", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ChambaPeBlue, modifier = Modifier.clickable {})
                                        }
                                    }

                                    // Lista de Tarjetas Estilo Figma
                                    items(jobs) { job ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(text = job.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                                        Spacer(Modifier.height(4.dp))
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(Icons.Outlined.Storefront, null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                                                            Spacer(Modifier.width(4.dp))
                                                            Text(text = job.district, fontSize = 13.sp, color = TextSecondary)
                                                        }
                                                    }
                                                    Column(horizontalAlignment = Alignment.End) {
                                                        Text(text = "S/ ${job.paymentAmount}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ChambaPeBlue)
                                                        Text(text = "📍 0.5km", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(top = 2.dp))
                                                    }
                                                }

                                                Spacer(Modifier.height(14.dp))

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(statusBackgroundColor(job.status))
                                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                                    ) {
                                                        Text(text = statusLabel(job.status), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = statusTextColor(job.status))
                                                    }

                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Outlined.FavoriteBorder, null, tint = TextSecondary, modifier = Modifier.size(20.dp).clickable {})
                                                        Spacer(Modifier.width(16.dp))
                                                        Button(
                                                            onClick = { onJobClick(job.id) },
                                                            shape = RoundedCornerShape(8.dp),
                                                            colors = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue),
                                                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                                                        ) {
                                                            Text("Aplicar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // El botón "Publicar Chamba" se movió a la pestaña "Jobs" (vista de contratante).
                    }
                }
            }
        }
    }
}

// Componente Reutilizable para los Chips de Figma
@Composable
fun CategoryChip(text: String, isSelected: Boolean, onChipClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = if (isSelected) ChambaPeBlue else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.clickable { onChipClick() }
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) Color.White else TextPrimary,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}