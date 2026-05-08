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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Close
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.outlined.AccessTime

import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.QuestionMark
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

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chambape.ui.theme.ChambaPeTheme

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 24.dp
            )
        ) {
            item {
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

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = "•",
                        fontSize = 16.sp,
                        color = Color(0xFFB0B4BE)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

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

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "$25 / hora",
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

                Spacer(modifier = Modifier.height(30.dp))

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
                        text = "Se requiere uniforme negro y buena disposición. " +
                                "El turno consistirá en atención a mesas en el área de terraza, " +
                                "toma de pedidos rápidos y apoyo en la limpieza de estaciones " +
                                "durante las horas pico de la tarde. Preferencia por candidatos " +
                                "con experiencia en uso de terminales punto de venta.",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        lineHeight = 23.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

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
            }
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
    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            ActiveShiftTopBar(onClose = onClose)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ScreenBackground)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChambaBlue,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Confirmar llegada",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFF0B8B4)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD93025)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.QuestionMark,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Pedir ayuda",
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
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(0xFFF4FFF5),
                                    Color(0xFFF2F6FF)
                                )
                            )
                        )
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(78.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF0EFFB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Storefront,
                                contentDescription = null,
                                tint = ChambaBlue,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Café Central",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(17.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "Av. Reforma 222, CDMX",
                                fontSize = 15.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(56.dp))

            Text(
                text = "HORARIO DEL TURNO",
                fontSize = 12.sp,
                letterSpacing = 1.2.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF7D8290)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = Color(0xFFC2C7D3),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "4:00 PM",
                    fontSize = 31.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "–",
                    fontSize = 28.sp,
                    color = Color(0xFFD2D5DD)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "9:00 PM",
                    fontSize = 31.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFF0EFF7))
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(ChambaBlue)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Esperando llegada...",
                    fontSize = 16.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun ActiveShiftTopBar(
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFEDEFF5),
                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
            )
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Cerrar",
                tint = Color(0xFF687083)
            )
        }

        Text(
            text = "Turno en progreso",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
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
                imageVector = Icons.Outlined.ArrowBackIosNew,
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
            .height(80.dp)
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
            modifier = Modifier.size(40.dp)
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

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFFF8F7FD,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun ActiveShiftScreenPreview() {
    ChambaPeTheme {
        ActiveShiftScreen(
            onClose = {}
        )
    }
}