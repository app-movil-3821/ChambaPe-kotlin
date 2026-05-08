package com.example.chambape.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.chambape.ui.theme.ChambaPeTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaBlue = Color(0xFF0B57D0)
private val ScreenBackground = Color(0xFFF8F7FD)
private val TextPrimary = Color(0xFF202124)
private val TextSecondary = Color(0xFF5F6368)
private val BorderGray = Color(0xFFE1E4EC)

// HomeFeedScreen   -> ver HomeFeedScreen.kt

@Composable
fun JobDetailsScreen(
    jobId: String,
    onBack: () -> Unit,
    onApply: (jobId: String) -> Unit
) {
    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            JobDetailsTopBar(
                onBack = onBack,
                onShare = { }
            )
        },
        bottomBar = {
            Surface(color = ScreenBackground) {
                Button(
                    onClick = { onApply(jobId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .navigationBarsPadding()
                        .height(54.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChambaBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Aceptar Turno",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Mesero - Café Central",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Storefront,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(17.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Café Central",
                    fontSize = 15.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "•",
                    fontSize = 16.sp,
                    color = Color(0xFFB0B4BE)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = Color(0xFFD66A2C),
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "4.8",
                    fontSize = 16.sp,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "\$25 / hora",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChambaBlue
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Hoy, 4:00 PM - 9:00 PM",
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Detalles del Turno",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "Se requiere uniforme negro y buena disposición.\n" +
                            "El turno consistirá en atención a mesas en el\n" +
                            "área de terraza, toma de pedidos rápidos y\n" +
                            "apoyo en la limpieza de estaciones durante las horas pico de la tarde.\n" +
                            "Preferencia por candidatos con experiencia en uso de terminales punto de venta.\n" +
                            "",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Ubicación",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            FakeMapPreview()

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Column {
                    Text(
                        text = "Av. Revolución 1234, Zona Centro.",
                        fontSize = 15.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Entrada por la puerta lateral de servicio.",
                        fontSize = 14.sp,
                        color = Color(0xFF8A8F99)
                    )
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
fun ApplyScreen(
    jobId: String,
    onConfirmed: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Apply Screen — $jobId")
    }
}

@Composable
fun ActiveShiftScreen(
    onClose: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Active Shift Screen")
    }
}

@Composable
private fun JobDetailsTopBar(
    onBack: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .background(ScreenBackground)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Volver",
                tint = TextPrimary
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onShare) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = "Compartir",
                tint = TextPrimary
            )
        }
    }
}

@Composable
private fun FakeMapPreview(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE9E4C7),
                        Color(0xFFE2E7CF),
                        Color(0xFFD8E9D0)
                    )
                )
            )
            .border(1.dp, BorderGray, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ){
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (i in 1..5){
                val y = size.height * i / 6
                drawLine(
                    color = Color.White.copy(alpha = 0.55f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y + 45f),
                    strokeWidth = 8f
                )
            }
            for (i in 1..4){
                val x = size.width * i / 5
                drawLine(
                    color = Color.White.copy(alpha = 0.55f),
                    start = Offset(x, 0f),
                    end = Offset(x - 55f, size.height),
                    strokeWidth = 8f
                )
            }
        }
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            tint = Color(0xFFE53935),
            modifier = Modifier.size(58.dp)
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFFF8F7FD,
    widthDp = 393,
    heightDp = 852
)
@Composable
fun JobDetailsScreenPreview() {
    JobDetailsScreen(
        jobId = "1",
        onBack = { },
        onApply = { }
    )
}