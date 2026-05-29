package com.example.chambape.presentation.shifts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

private val ChambaBlue    = Color(0xFF1A3FD8)
private val TextPrimary   = Color(0xFF202124)
private val TextSecondary = Color(0xFF5F6368)
private val BorderGray    = Color(0xFFE1E4EC)
private val StarYellow    = Color(0xFF2E9E6B)

@Composable
fun ShiftSummaryScreen(
    shiftId: String,
    onDone: () -> Unit
) {
    // Busca el turno seleccionado; si no existe usa el primero como respaldo.
    val shift = sampleShifts.firstOrNull { it.id == shiftId } ?: sampleShifts.first()

    var rating     by remember { mutableIntStateOf(4) }
    var comment    by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // Ilustración circular
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF4FC3C8),
                                Color(0xFF2196F3),
                                Color(0xFF9C27B0).copy(alpha = 0.6f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Círculos decorativos internos
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✨", fontSize = 36.sp)
                }
            }

            Spacer(Modifier.height(28.dp))

            // Título
            Text(
                text       = "¡Buen trabajo, Diego!",
                fontSize   = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = TextPrimary,
                textAlign  = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text      = "Turno completado exitosamente.",
                fontSize  = 15.sp,
                color     = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // Card ganancias (azul)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = ChambaBlue)
            ) {
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text       = "GANANCIAS DE HOY",
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color      = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.2.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text       = "Ganaste \$%,.2f por este turno".format(shift.payment),
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Card de rating
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderGray, RoundedCornerShape(16.dp))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text       = shift.company,
                        fontSize   = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextPrimary
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text     = "¿Cómo fue tu experiencia?",
                        fontSize = 14.sp,
                        color    = TextSecondary
                    )

                    Spacer(Modifier.height(16.dp))

                    // Estrellas
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..5).forEach { star ->
                            IconButton(
                                onClick  = { rating = star },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector        = Icons.Outlined.Star,
                                    contentDescription = "Estrella $star",
                                    tint               = if (star <= rating) StarYellow
                                    else Color(0xFFDDDDDD),
                                    modifier           = Modifier.size(32.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Campo comentario
                    OutlinedTextField(
                        value         = comment,
                        onValueChange = { comment = it },
                        modifier      = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
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

            // Botón Listo
            Button(
                onClick  = { onDone() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .navigationBarsPadding(),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ChambaBlue)
            ) {
                Text(
                    text       = "Listo",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShiftSummaryScreenPreview() {
    ShiftSummaryScreen(shiftId = "1", onDone = { })
}