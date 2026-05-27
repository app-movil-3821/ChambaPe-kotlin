package com.example.chambape.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaBlue   = Color(0xFF1A3FD8)
private val TextPrimary  = Color(0xFF0D0D0D)
private val TextSecondary = Color(0xFF6B6B6B)

@Composable
fun HelpScreen(
    onBack: () -> Unit
) {
    var problemType   by remember { mutableStateOf("") }
    var description   by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Atrás",
                        tint               = ChambaBlue
                    )
                }
                Text(
                    text       = "Pedir Ayuda",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = ChambaBlue
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                // Imagen de soporte
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE8EDFB)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🎧", fontSize = 64.sp)
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Título y descripción
                Text(
                    text       = "¿En qué podemos ayudarte?",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = TextPrimary
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text      = "Nuestro equipo de soporte está disponible las 24 horas para resolver cualquier duda con tus turnos o pagos.",
                    fontSize  = 14.sp,
                    color     = TextSecondary,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(24.dp))

                // Campo Tipo de problema
                Text(
                    text       = "Tipo de Problema",
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color      = TextPrimary
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value         = problemType,
                    onValueChange = { problemType = it },
                    modifier      = Modifier.fillMaxWidth(),
                    singleLine    = true,
                    shape         = RoundedCornerShape(12.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFDDDDDD),
                        focusedBorderColor   = ChambaBlue
                    )
                )

                Spacer(Modifier.height(16.dp))

                // Campo Descripción
                Text(
                    text       = "Descripción del problema",
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color      = TextPrimary
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value         = description,
                    onValueChange = { description = it },
                    modifier      = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape  = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFDDDDDD),
                        focusedBorderColor   = ChambaBlue
                    )
                )

                Spacer(Modifier.height(20.dp))

                // Adjuntar evidencia
                Text(
                    text       = "Adjuntar evidencia (Opcional)",
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color      = TextPrimary
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFFCCCCCC),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(Color(0xFFFAFAFA)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFE8EDFB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Outlined.CameraAlt,
                                contentDescription = null,
                                tint               = ChambaBlue,
                                modifier           = Modifier.size(22.dp)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text       = "Subir fotos o capturas",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = ChambaBlue
                        )
                        Text(
                            text     = "Formatos permitidos: JPG, PNG (Máx. 5MB)",
                            fontSize = 12.sp,
                            color    = TextSecondary
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Nota informativa
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFF3E0))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text     = "info",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color    = Color(0xFFE65100),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFCC80))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text      = "Nota: Si el problema es con un pago reciente, recuerda que puede tardar hasta 24 horas hábiles en reflejarse.",
                            fontSize  = 13.sp,
                            color     = Color(0xFF6B6B6B),
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            // Botón Enviar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick  = { onBack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape  = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ChambaBlue)
                ) {
                    Text(
                        text       = "Enviar mensaje",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HelpScreenPreview() {
    HelpScreen(onBack = { })
}