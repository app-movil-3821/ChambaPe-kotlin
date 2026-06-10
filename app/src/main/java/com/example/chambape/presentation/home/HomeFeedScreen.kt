package com.example.chambape.presentation.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
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

// NUEVAS IMPORTACIONES PARA EL MAPA
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private val BackgroundGray  = Color(0xFFF7F8FC)
private val ChambaPeBlue    = Color(0xFF0B57D0)
private val TextPrimary     = Color(0xFF1D1B20)
private val TextSecondary   = Color(0xFF49454F)

private fun statusLabel(status: String) = when (status.uppercase()) {
    "PUBLISHED"   -> "● Disponible"
    "IN_PROGRESS" -> "● En progreso"
    "CLOSED"      -> "● Cerrado"
    else          -> "● $status"
}

private fun statusBackgroundColor(status: String) = when (status.uppercase()) {
    "PUBLISHED"   -> Color(0xFFE8F5E9)
    "IN_PROGRESS" -> Color(0xFFE3F2FD)
    "CLOSED"      -> Color(0xFFFFEEEE)
    else          -> Color(0xFFF5F5F5)
}

private fun statusTextColor(status: String) = when (status.uppercase()) {
    "PUBLISHED"   -> Color(0xFF43A047)
    "IN_PROGRESS" -> Color(0xFF1E88E5)
    "CLOSED"      -> Color(0xFFE53935)
    else          -> Color(0xFF757575)
}

@Composable
fun HomeFeedScreen(
    viewModel: HomeFeedViewModel = viewModel(),
    onJobClick: (jobId: String) -> Unit
) {
    val jobs         by viewModel.jobs.collectAsState()
    val isLoading    by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val userName     by viewModel.userName.collectAsState()
    val searchQuery  by viewModel.searchQuery.collectAsState()

    // 1. NUEVO: Estado para alternar entre ver la lista o ver el mapa
    var verMapa by remember { mutableStateOf(false) }

    // 2. NUEVO: Configuración de cámara para el mapa centrado en Lima
    val limaCentro = LatLng(-12.046374, -77.042793)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(limaCentro, 13f)
    }

    // 3. NUEVO: Envolvemos todo en un Scaffold para soportar el Botón Flotante de forma limpia
    Scaffold(
        containerColor = BackgroundGray,
        floatingActionButton = {
            // Solo mostramos el FAB si no está cargando, no hay errores y la lista no está vacía
            if (!isLoading && errorMessage == null && jobs.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { verMapa = !verMapa },
                    icon = {
                        Icon(
                            imageVector = if (verMapa) Icons.Filled.List else Icons.Filled.Map,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = if (verMapa) "Ver Lista" else "Ver Mapa") },
                    containerColor = ChambaPeBlue,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
                )
            }
        }
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color    = BackgroundGray
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ChambaPeBlue)
                    }
                }

                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text      = errorMessage!!,
                                fontSize  = 15.sp,
                                color     = Color(0xFFD93025),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.loadJobs() },
                                colors  = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue),
                                shape   = RoundedCornerShape(10.dp)
                            ) {
                                Text("Reintentar", color = Color.White)
                            }
                        }
                    }
                }

                jobs.isEmpty() -> {
                    val isFiltering = searchQuery.isNotBlank()
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Outlined.WorkOutline,
                                contentDescription = null,
                                tint     = Color(0xFFCCCCCC),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text     = if (isFiltering) "Sin resultados para \"$searchQuery\"."
                                else "No hay trabajos disponibles.",
                                fontSize = 15.sp,
                                color    = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                            if (!isFiltering) {
                                Spacer(Modifier.height(12.dp))
                                Button(
                                    onClick = { viewModel.loadJobs() },
                                    colors  = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue),
                                    shape   = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Actualizar", color = Color.White)
                                }
                            }
                        }
                    }
                }

                // SI TODO ESTÁ CORRECTO: Renderizamos la UI Principal
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {

                        // CABECERA ESTÁTICA (Saludo + Input de Búsqueda)
                        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
                            Text(
                                text       = if (userName.isNotBlank()) "¡Hola, $userName! 👋" else "¡Hola!",
                                fontSize   = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color      = TextPrimary,
                                modifier   = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text     = "Estos son los trabajos disponibles",
                                fontSize = 14.sp,
                                color    = TextSecondary,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            OutlinedTextField(
                                value         = searchQuery,
                                onValueChange = { viewModel.onSearchQueryChange(it) },
                                modifier      = Modifier.fillMaxWidth(),
                                placeholder   = { Text("Buscar por título, categoría o distrito…", fontSize = 14.sp) },
                                leadingIcon   = { Icon(Icons.Outlined.Search, contentDescription = null, tint = TextSecondary) },
                                trailingIcon  = {
                                    if (searchQuery.isNotBlank()) {
                                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                            Icon(Icons.Outlined.Close, contentDescription = "Limpiar", tint = TextSecondary)
                                        }
                                    }
                                },
                                singleLine    = true,
                                shape         = RoundedCornerShape(14.dp),
                                colors        = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor     = TextPrimary,
                                    unfocusedTextColor   = TextPrimary,
                                    focusedContainerColor   = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor   = ChambaPeBlue,
                                    unfocusedBorderColor = Color(0xFFDDDDDD)
                                )
                            )
                        }

                        // CONDICIONAL: ¿Mostramos Mapa o la Lista tradicional?
                        if (verMapa) {
                            // VISTA DE MAPA COMPLETO
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState
                            ) {
                                // Pintamos los pines reales con la data de tu backend (jobs)
                                jobs.forEach { job ->

                                    val coordenadasSimuladas = LatLng(
                                        -12.046374 + (job.id.hashCode() % 100 * 0.0005),
                                        -77.042793 + (job.id.hashCode() % 100 * 0.0005)
                                    )

                                    Marker(
                                        state = MarkerState(position = coordenadasSimuladas),
                                        title = job.title,
                                        snippet = "${job.district} - S/ ${job.paymentAmount}",
                                        onClick = {
                                            onJobClick(job.id) // Navega al detalle del trabajo al tocar el pin
                                            true
                                        }
                                    )
                                }
                            }
                        } else {
                            // VISTA DE LISTA VERTICAL
                            LazyColumn(
                                contentPadding      = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier            = Modifier.fillMaxSize()
                            ) {
                                items(jobs) { job ->
                                    Card(
                                        modifier  = Modifier.fillMaxWidth(),
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
                                                Text(
                                                    text       = job.title,
                                                    fontSize   = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color      = TextPrimary,
                                                    modifier   = Modifier.weight(1f)
                                                )
                                                Icon(
                                                    imageVector        = Icons.Outlined.FavoriteBorder,
                                                    contentDescription = "Guardar",
                                                    tint               = TextSecondary,
                                                    modifier           = Modifier
                                                        .padding(start = 8.dp)
                                                        .clickable { }
                                                )
                                            }

                                            Spacer(Modifier.height(8.dp))

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector        = Icons.Outlined.LocationOn,
                                                    contentDescription = null,
                                                    tint               = TextSecondary,
                                                    modifier           = Modifier.size(16.dp)
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text(
                                                    text     = job.district,
                                                    fontSize = 14.sp,
                                                    color    = TextSecondary
                                                )
                                                Spacer(Modifier.width(16.dp))
                                                Text(
                                                    text       = "S/ ${job.paymentAmount}",
                                                    fontSize   = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color      = TextPrimary
                                                )
                                            }

                                            Spacer(Modifier.height(16.dp))

                                            Row(
                                                modifier              = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment     = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(50.dp))
                                                        .background(statusBackgroundColor(job.status))
                                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                                ) {
                                                    Text(
                                                        text       = statusLabel(job.status),
                                                        fontSize   = 12.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color      = statusTextColor(job.status)
                                                    )
                                                }

                                                Button(
                                                    onClick        = { onJobClick(job.id) },
                                                    shape          = RoundedCornerShape(10.dp),
                                                    colors         = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue),
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
                            }
                        }
                    }
                }
            }
        }
    }
}