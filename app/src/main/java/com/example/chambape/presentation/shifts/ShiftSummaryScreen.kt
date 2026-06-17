package com.example.chambape.presentation.shifts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chambape.domain.model.Job

private val ChambaBlue    = Color(0xFF1A3FD8)
private val TextPrimary   = Color(0xFF202124)
private val TextSecondary = Color(0xFF5F6368)
private val BorderGray    = Color(0xFFE1E4EC)
private val StarYellow    = Color(0xFFFFC107)
private val BgGray        = Color(0xFFF8FAFC)

@Composable
fun ShiftSummaryScreen(
    shiftId: String,
    onDone: () -> Unit,
    previewUiState: ShiftSummaryUiState? = null
) {
    val runtimeViewModel: ShiftSummaryViewModel? =
        if (previewUiState == null) viewModel() else null

    val uiState by if (previewUiState != null) {
        remember { mutableStateOf(previewUiState) }
    } else {
        runtimeViewModel!!.uiState.collectAsState()
    }

    LaunchedEffect(shiftId, previewUiState) {
        if (previewUiState == null) runtimeViewModel?.loadJob(shiftId)
    }

    // Navega a Done automáticamente cuando la review se envió con éxito
    LaunchedEffect(uiState.reviewSent) {
        if (uiState.reviewSent) onDone()
    }

    var rating  by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = BgGray
    ) {
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = ChambaBlue)
                        Spacer(Modifier.height(16.dp))
                        Text("Cargando resumen...", fontSize = 15.sp, color = TextSecondary)
                    }
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text      = uiState.errorMessage ?: "Error al cargar resumen.",
                            fontSize  = 15.sp,
                            color     = Color(0xFFD93025),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = onDone,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape    = RoundedCornerShape(14.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = ChambaBlue)
                        ) {
                            Text("Volver", fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }
                }
            }

            else -> {
                val job = uiState.job
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(40.dp))

                    // ─── Ícono central ───
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF4FC3C8),
                                        Color(0xFF2196F3),
                                        Color(0xFF9C27B0).copy(alpha = 0.6f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✨", fontSize = 32.sp)
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // ─── Título ───
                    Text(
                        text       = "¡Turno completado!",
                        fontSize   = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = TextPrimary,
                        textAlign  = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text      = job?.title ?: "Turno completado exitosamente.",
                        fontSize  = 15.sp,
                        color     = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(28.dp))

                    // ─── Card ganancias ───
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(18.dp),
                        colors   = CardDefaults.cardColors(containerColor = ChambaBlue),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier            = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text          = "GANANCIAS DE HOY",
                                fontSize      = 11.sp,
                                fontWeight    = FontWeight.SemiBold,
                                color         = Color.White.copy(alpha = 0.75f),
                                letterSpacing = 1.5.sp
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text       = "S/ ${"%.2f".format(job?.paymentAmount ?: 0.0)}",
                                fontSize   = 36.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color      = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ─── Card calificación ───
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = RoundedCornerShape(18.dp),
                        colors    = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(
                            modifier            = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text       = "¿Cómo estuvo la chamba?",
                                fontSize   = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color      = TextPrimary
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text     = job?.let { "${it.district} · ${formatSummaryHour(it.scheduledStart)} - ${formatSummaryHour(it.scheduledEnd)}" }
                                    ?: "Califica tu experiencia",
                                fontSize = 13.sp,
                                color    = TextSecondary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(20.dp))

                            // Estrellas
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                (1..5).forEach { star ->
                                    IconButton(
                                        onClick  = { rating = star },
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Icon(
                                            imageVector        = if (star <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                                            contentDescription = "Estrella $star",
                                            tint               = if (star <= rating) StarYellow else Color(0xFFCCCCCC),
                                            modifier           = Modifier.size(36.dp)
                                        )
                                    }
                                }
                            }

                            if (rating > 0) {
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = when (rating) {
                                        1 -> "Muy malo"
                                        2 -> "Malo"
                                        3 -> "Regular"
                                        4 -> "Bueno"
                                        else -> "¡Excelente!"
                                    },
                                    fontSize   = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color      = ChambaBlue
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            // Campo comentario
                            OutlinedTextField(
                                value         = comment,
                                onValueChange = { comment = it },
                                modifier      = Modifier.fillMaxWidth().height(96.dp),
                                placeholder   = {
                                    Text(
                                        "Deja un comentario (opcional)",
                                        color    = Color(0xFFAAAAAA),
                                        fontSize = 14.sp
                                    )
                                },
                                shape  = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = BorderGray,
                                    focusedBorderColor   = ChambaBlue
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(28.dp))

                    // ─── Botón Listo ───
                    Button(
                        onClick = {
                            if (rating > 0 && runtimeViewModel != null) {
                                runtimeViewModel.submitReview(rating, comment)
                            } else {
                                onDone()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .navigationBarsPadding(),
                        shape   = RoundedCornerShape(14.dp),
                        colors  = ButtonDefaults.buttonColors(containerColor = ChambaBlue),
                        enabled = !uiState.isSending
                    ) {
                        if (uiState.isSending) {
                            CircularProgressIndicator(
                                modifier  = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color     = Color.White
                            )
                        } else {
                            Text(
                                text       = if (rating > 0) "Enviar calificación" else "Omitir",
                                fontSize   = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color      = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

private fun formatSummaryHour(dateTime: String): String =
    if (dateTime.length >= 16) dateTime.substring(11, 16) else dateTime

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShiftSummaryScreenPreview() {
    ShiftSummaryScreen(
        shiftId        = "preview-job",
        onDone         = {},
        previewUiState = ShiftSummaryUiState(
            isLoading = false,
            job = Job(
                id             = "preview-job",
                contractorId   = "preview-contractor",
                title          = "Apoyo en cafetería por la tarde",
                description    = "Atención al cliente y preparación de pedidos.",
                category       = "Gastronomía",
                requiredSkills = listOf("puntualidad", "orden"),
                paymentAmount  = 60.0,
                latitude       = -12.046374,
                longitude      = -77.042793,
                address        = "Av. Larco 450",
                district       = "Miraflores",
                scheduledStart = "2026-06-15T14:00:00",
                scheduledEnd   = "2026-06-15T19:00:00",
                status         = "COMPLETED"
            ),
            errorMessage = null
        )
    )
}