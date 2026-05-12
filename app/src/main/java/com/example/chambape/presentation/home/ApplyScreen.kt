package com.example.chambape.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaBlue   = Color(0xFF0B57D0)
private val BorderGray   = Color(0xFFE1E4EC)
private val TextPrimary  = Color(0xFF202124)
private val TextSecondary = Color(0xFF5F6368)

@Composable
fun ApplyScreen(
    jobId: String,
    onConfirmed: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onConfirmed() }, // ← toca cualquier parte y vuelve
        color = Color(0xFFF8F7FD)
    ) {
        Box(
            modifier         = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                border    = BorderStroke(1.dp, BorderGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Ícono check
                    Icon(
                        imageVector        = Icons.Outlined.CheckCircleOutline,
                        contentDescription = null,
                        tint               = TextPrimary,
                        modifier           = Modifier.size(64.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    // Título
                    Text(
                        text       = "¡Turno Aceptado!",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                        textAlign  = TextAlign.Center
                    )

                    Spacer(Modifier.height(10.dp))

                    // Subtítulo
                    Text(
                        text      = "Te esperamos en Café Central a las 4:00 PM.",
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ApplyScreenPreview() {
    ApplyScreen(jobId = "1", onConfirmed = { })
}