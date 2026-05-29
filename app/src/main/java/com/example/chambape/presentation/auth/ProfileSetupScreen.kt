package com.example.chambape.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaPeBlue2 = Color(0xFF1A3FD8)

@Composable
fun ProfileSetupScreen(
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    var displayName by remember { mutableStateOf("") }
    var bio         by remember { mutableStateOf("") }
    var city        by remember { mutableStateOf("") }
    val isValid = displayName.isNotBlank() && city.isNotBlank()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color(0xFFF7F8FC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(52.dp))

            // Header
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Atrás",
                        tint               = Color(0xFF0D0D0D)
                    )
                }

                // Progress dots (paso 2 de 3)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(3) { index ->
                        androidx.compose.foundation.Canvas(
                            modifier = Modifier.size(
                                width  = if (index == 1) 24.dp else 8.dp,
                                height = 8.dp
                            )
                        ) {
                            drawRoundRect(
                                color        = if (index == 1) ChambaPeBlue2 else Color(0xFFDDDDDD),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text       = "Configura tu perfil",
                fontSize   = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color(0xFF0D0D0D),
                modifier   = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text      = "Así te verán los empleadores.",
                fontSize  = 14.sp,
                color     = Color(0xFF6B6B6B),
                modifier  = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(28.dp))

            // Avatar placeholder
            Box(
                modifier        = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E5F7)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Outlined.CameraAlt,
                    contentDescription = "Foto de perfil",
                    tint               = ChambaPeBlue2,
                    modifier           = Modifier.size(32.dp)
                )
            }

            TextButton(onClick = { /* abrir galería */ }) {
                Text(
                    text  = "Agregar foto",
                    color = ChambaPeBlue2,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value         = displayName,
                onValueChange = { displayName = it },
                label         = { Text("Nombre para mostrar") },
                placeholder   = { Text("Ej. Juan Pérez") },
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                singleLine    = true
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value         = city,
                onValueChange = { city = it },
                label         = { Text("Ciudad") },
                placeholder   = { Text("Ej. Lima") },
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                singleLine    = true
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value         = bio,
                onValueChange = { if (it.length <= 150) bio = it },
                label         = { Text("Bio (opcional)") },
                placeholder   = { Text("Cuéntanos algo sobre ti...") },
                modifier      = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape         = RoundedCornerShape(12.dp),
                maxLines      = 4,
                supportingText = {
                    Text(
                        text  = "${bio.length}/150",
                        color = Color(0xFF9E9E9E),
                        fontSize = 11.sp
                    )
                }
            )

            Spacer(Modifier.height(28.dp))

            Button(
                onClick  = { onContinue() },
                enabled  = isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor         = ChambaPeBlue2,
                    disabledContainerColor = Color(0xFFBBCCF5)
                )
            ) {
                Text(
                    text       = "Continuar",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}