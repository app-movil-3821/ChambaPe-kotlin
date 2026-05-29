package com.example.chambape.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaPeBlue = Color(0xFF1A3FD8)

@Composable
fun PhoneVerificationScreen(
    onVerified: () -> Unit,
    onBack: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    val isValid = code.length == 6

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color(0xFFF7F8FC)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
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

                // Progress dots (paso 1 de 3)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(3) { index ->
                        androidx.compose.foundation.Canvas(
                            modifier = Modifier.size(
                                width  = if (index == 0) 24.dp else 8.dp,
                                height = 8.dp
                            )
                        ) {
                            drawRoundRect(
                                color        = if (index == 0) ChambaPeBlue else Color(0xFFDDDDDD),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            Text(
                text       = "📱",
                fontSize   = 48.sp
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text       = "Verifica tu número",
                fontSize   = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color(0xFF0D0D0D)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text      = "Ingresa el código de 6 dígitos que enviamos a tu teléfono.",
                fontSize  = 14.sp,
                color     = Color(0xFF6B6B6B),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(36.dp))

            OutlinedTextField(
                value         = code,
                onValueChange = { if (it.length <= 6) code = it },
                placeholder   = { Text("000000", textAlign = TextAlign.Center) },
                modifier      = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape         = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine    = true,
                textStyle     = LocalTextStyle.current.copy(
                    textAlign  = TextAlign.Center,
                    fontSize   = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 8.sp
                )
            )

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = { /* reenviar código */ }) {
                Text(
                    text     = "¿No recibiste el código? Reenviar",
                    color    = ChambaPeBlue,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick  = { onVerified() },
                enabled  = isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor         = ChambaPeBlue,
                    disabledContainerColor = Color(0xFFBBCCF5)
                )
            ) {
                Text(
                    text       = "Verificar",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}