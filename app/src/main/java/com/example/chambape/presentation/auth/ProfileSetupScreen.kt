package com.example.chambape.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.EmojiObjects
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaBlue = Color(0xFF1A3FD8)

@Composable
fun ProfileSetupScreen(
    onNext: () -> Unit
) {
    var bio by remember { mutableStateOf("") }
    val maxChars = 250

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(32.dp))

            // Progress dots (paso 1 de 3)
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(3) { index ->
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier
                            .size(
                                width  = if (index == 0) 28.dp else 10.dp,
                                height = 6.dp
                            )
                            .padding(end = if (index < 2) 6.dp else 0.dp)
                    ) {
                        drawRoundRect(
                            color        = if (index == 0) ChambaBlue else Color(0xFFDDDDDD),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                        )
                    }
                    if (index < 2) Spacer(Modifier.width(6.dp))
                }
            }

            Spacer(Modifier.height(32.dp))

            // Título
            Text(
                text       = "Completa tu perfil",
                fontSize   = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color(0xFF0D0D0D)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text      = "Tu foto y biografía ayudan a generar confianza con otros usuarios.",
                fontSize  = 14.sp,
                color     = Color(0xFF6B6B6B),
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(36.dp))

            // Avatar + botón subir foto
            Box(
                modifier            = Modifier.fillMaxWidth(),
                contentAlignment    = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        // Avatar circle
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF0F0F5))
                                .border(2.dp, Color(0xFFDDDDDD), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Outlined.Person,
                                contentDescription = null,
                                tint               = Color(0xFFAAAAAA),
                                modifier           = Modifier.size(50.dp)
                            )
                        }

                        // Botón cámara
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(ChambaBlue)
                                .align(Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Outlined.AddAPhoto,
                                contentDescription = "Subir foto",
                                tint               = Color.White,
                                modifier           = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    TextButton(onClick = { }) {
                        Text(
                            text       = "Subir foto",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = ChambaBlue
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Bio
            Text(
                text       = "Cuéntanos sobre ti",
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color      = Color(0xFF0D0D0D)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value         = bio,
                onValueChange = { if (it.length <= maxChars) bio = it },
                modifier      = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                placeholder   = { },
                shape         = RoundedCornerShape(12.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor   = ChambaBlue,
                    unfocusedContainerColor = Color(0xFFF7F8FC),
                    focusedContainerColor   = Color.White
                )
            )

            // Contador de caracteres
            Text(
                text     = "${bio.length} / $maxChars",
                fontSize = 12.sp,
                color    = Color(0xFF9E9E9E),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, end = 4.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text     = "Este texto será visible en tu perfil público.",
                fontSize = 12.sp,
                color    = Color(0xFF9E9E9E)
            )

            Spacer(Modifier.height(16.dp))

            // Tip card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFEEF1FB))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector        = Icons.Outlined.EmojiObjects,
                        contentDescription = null,
                        tint               = ChambaBlue,
                        modifier           = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text       = "Tip de experto",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color      = ChambaBlue
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text      = "Los perfiles con una biografía detallada tienen un 40% más de probabilidades de conseguir un empleo rápido.",
                            fontSize  = 13.sp,
                            color     = Color(0xFF4B6FD8),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Botón Siguiente
            Button(
                onClick  = { onNext() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ChambaBlue)
            ) {
                Text(
                    text       = "Siguiente",
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
fun ProfileSetupScreenPreview() {
    ProfileSetupScreen(onNext = { })
}