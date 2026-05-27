package com.example.chambape.presentation.auth

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaBlue = Color(0xFF1A3FD8)

@Composable
fun PhoneVerificationScreen(
    onVerified: () -> Unit
) {
    var otpValue by remember { mutableStateOf("") }
    val otpLength = 6

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color(0xFFF7F8FC)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))

            // Ícono central
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8EDFB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Outlined.PhoneAndroid,
                    contentDescription = null,
                    tint               = ChambaBlue,
                    modifier           = Modifier.size(38.dp)
                )
            }

            Spacer(Modifier.height(28.dp))

            // Título
            Text(
                text       = "Verificar número",
                fontSize   = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color(0xFF0D0D0D),
                textAlign  = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            // Subtítulo
            Text(
                text      = "Hemos enviado un código de 6 dígitos a su teléfono móvil +52 ••• ••• 1234",
                fontSize  = 14.sp,
                color     = Color(0xFF6B6B6B),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(36.dp))

            // OTP input
            BasicTextField(
                value         = otpValue,
                onValueChange = { if (it.length <= otpLength) otpValue = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                decorationBox = { _ ->
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        repeat(otpLength) { index ->
                            val char = otpValue.getOrNull(index)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .border(
                                        width = 1.5.dp,
                                        color = if (char != null) ChambaBlue
                                        else Color(0xFFDDDDDD),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text       = if (char != null) "•" else "",
                                    fontSize   = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = Color(0xFF0D0D0D)
                                )
                            }
                        }
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            // ¿No recibiste el código?
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text     = "¿No recibiste el código?",
                    fontSize = 14.sp,
                    color    = Color(0xFF6B6B6B)
                )
                TextButton(onClick = { otpValue = "" }) {
                    Text(
                        text       = "Reenviar código",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color      = ChambaBlue
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Card de seguridad
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier          = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Outlined.Security,
                            contentDescription = null,
                            tint               = Color(0xFF2E7D32),
                            modifier           = Modifier.size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text      = "Tu información está protegida con encriptación de extremo a extremo.",
                        fontSize  = 13.sp,
                        color     = Color(0xFF4B4B4B),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // Botón Sign Up
            Button(
                onClick  = { onVerified() },
                enabled  = otpValue.length == otpLength,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor         = ChambaBlue,
                    disabledContainerColor = Color(0xFFBBCCF5)
                )
            ) {
                Text(
                    text       = "Sign Up",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PhoneVerificationScreenPreview() {
    PhoneVerificationScreen(onVerified = { })
}