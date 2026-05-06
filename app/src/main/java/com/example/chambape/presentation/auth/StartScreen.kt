package com.example.chambape.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaPeBlue  = Color(0xFF1A3FD8)
private val LightBlue     = Color(0xFFE8EDFB)
private val TealAccent    = Color(0xFF4FC3C8)

@Composable
fun StartScreen(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color.White
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(52.dp))

            // Logo
            Text(
                text       = "ChambaYa",
                fontSize   = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = ChambaPeBlue
            )

            Spacer(Modifier.height(36.dp))

            // Ilustración placeholder con badge
            Box(
                modifier        = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(LightBlue, Color(0xFFD0E4F7))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Ícono central representando trabajo
                    Box(
                        modifier        = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(ChambaPeBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text     = "💼",
                            fontSize = 48.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Badge "No CV Required"
                    Row(
                        modifier            = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White)
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment   = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector        = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint               = Color(0xFF22C55E),
                            modifier           = Modifier.size(18.dp)
                        )
                        Text(
                            text       = "No CV Required",
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = Color(0xFF0D0D0D)
                        )
                    }
                }
            }

            Spacer(Modifier.height(36.dp))

            // Título
            Text(
                text       = "Find work instantly.",
                fontSize   = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color(0xFF0D0D0D),
                textAlign  = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            // Subtítulo
            Text(
                text      = "Connect with flexible shifts in your area\nwithout the hassle of traditional hiring.",
                fontSize  = 15.sp,
                color     = Color(0xFF6B6B6B),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(40.dp))

            // Botón Get Started
            Button(
                onClick  = { onGetStarted() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue)
            ) {
                Text(
                    text       = "Get Started",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }

            Spacer(Modifier.height(12.dp))

            // Botón Phone Number
            OutlinedButton(
                onClick  = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape  = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFDDDDDD))
            ) {
                Row(
                    verticalAlignment      = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector        = Icons.Outlined.Phone,
                        contentDescription = null,
                        tint               = Color(0xFF0D0D0D),
                        modifier           = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.size(10.dp))
                    Text(
                        text     = "Phone Number",
                        fontSize = 15.sp,
                        color    = Color(0xFF0D0D0D)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Botón Google
            OutlinedButton(
                onClick  = { onLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape  = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFDDDDDD))
            ) {
                Row(
                    verticalAlignment      = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text       = "G",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF4285F4)
                    )
                    Spacer(Modifier.size(10.dp))
                    Text(
                        text     = "Google",
                        fontSize = 15.sp,
                        color    = Color(0xFF0D0D0D)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // Terms of Service
            Text(
                text      = buildAnnotatedString {
                    append("By continuing, you agree to our ")
                    withStyle(SpanStyle(
                        color          = ChambaPeBlue,
                        textDecoration = TextDecoration.Underline
                    )) {
                        append("Terms of Service")
                    }
                    append(" and ")
                    withStyle(SpanStyle(
                        color          = ChambaPeBlue,
                        textDecoration = TextDecoration.Underline
                    )) {
                        append("Privacy Policy")
                    }
                    append(".")
                },
                fontSize  = 12.sp,
                color     = Color(0xFF9E9E9E),
                textAlign = TextAlign.Center,
                modifier  = Modifier.padding(bottom = 28.dp)
            )
        }
    }
}