package com.example.chambape.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val ChambaBlue    = Color(0xFF0B57D0)
private val BorderGray    = Color(0xFFE1E4EC)
private val TextPrimary   = Color(0xFF202124)
private val TextSecondary = Color(0xFF5F6368)

@Composable
fun ApplyScreen(
    jobId: String,
    contractorId: String,
    onConfirmed: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: ApplyViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(jobId, contractorId) {
        viewModel.apply(jobId, contractorId)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onConfirmed()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F7FD)
    ) {
        Box(
            modifier         = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = ChambaBlue)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Procesando tu solicitud...",
                            fontSize = 15.sp,
                            color = TextSecondary
                        )
                    }
                }

                uiState.errorMessage != null -> {
                    Card(
                        modifier  = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(containerColor = Color.White),
                        border    = BorderStroke(1.dp, BorderGray),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier            = Modifier.fillMaxWidth().padding(vertical = 40.dp, horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Outlined.ErrorOutline,
                                contentDescription = null,
                                tint               = Color(0xFFD93025),
                                modifier           = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.height(20.dp))
                            Text(
                                text       = "No se pudo enviar la solicitud",
                                fontSize   = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color      = TextPrimary,
                                textAlign  = TextAlign.Center
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text      = uiState.errorMessage ?: "Error desconocido.",
                                fontSize  = 14.sp,
                                color     = TextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                            Spacer(Modifier.height(28.dp))
                            Button(
                                onClick  = onBack,
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape    = RoundedCornerShape(10.dp),
                                colors   = ButtonDefaults.buttonColors(containerColor = ChambaBlue)
                            ) {
                                Text("Volver", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.White)
                            }
                        }
                    }
                }

                uiState.isSuccess -> {
                    Card(
                        modifier  = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(containerColor = Color.White),
                        border    = BorderStroke(1.dp, BorderGray),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier            = Modifier.fillMaxWidth().padding(vertical = 40.dp, horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Outlined.CheckCircleOutline,
                                contentDescription = null,
                                tint               = TextPrimary,
                                modifier           = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.height(20.dp))
                            Text(
                                text       = "¡Turno Aceptado!",
                                fontSize   = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color      = TextPrimary,
                                textAlign  = TextAlign.Center
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text      = "Tu solicitud fue enviada exitosamente.",
                                fontSize  = 15.sp,
                                color     = TextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ApplyScreenPreview() {
    ApplyScreen(jobId = "1", contractorId = "c1", onConfirmed = { }, onBack = { })
}
