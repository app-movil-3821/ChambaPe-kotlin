package com.example.chambape.presentation.shifts

import androidx.compose.runtime.Composable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
private val BorderGray = Color(0xFFC9CDE0)

@Composable
fun HelpScreen(
    onBack: () -> Unit,
    onSendMessage: () -> Unit
) {
    var problemType by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            HelpTopBar(onBack = onBack)
        },
        bottomBar = {
            Surface(color = ScreenBackground) {
                Button(
                    onClick = {
                        when {
                            problemType.isBlank() -> {
                                errorMessage = "Ingresa el tipo de problema."
                                successMessage = null
                            }

                            description.isBlank() -> {
                                errorMessage = "Describe brevemente el problema."
                                successMessage = null
                            }

                            else -> {
                                errorMessage = null
                                successMessage = "Solicitud enviada correctamente."

                                /*
                                 * Por ahora no se conecta al backend.
                                 * Este callback mantiene la navegación o acción definida en HomeNavHost.
                                 */
                                onSendMessage()
                            }
                        }
                    },
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
                        text = "Enviar mensaje",
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
                top = 24.dp,
                bottom = 24.dp
            )
        ) {
            item {
                HelpHeaderCard()

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Tipo de Problema",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = problemType,
                    onValueChange = {
                        problemType = it
                        errorMessage = null
                        successMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = ChambaBlue,
                        unfocusedBorderColor = BorderGray
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Descripción",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        errorMessage = null
                        successMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(154.dp),
                    placeholder = {
                        Text(
                            text = "Describe brevemente lo que ocurrió",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = ChambaBlue,
                        unfocusedBorderColor = BorderGray
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                errorMessage?.let { message ->
                    Text(
                        text = message,
                        fontSize = 13.sp,
                        color = Color(0xFFD93025),
                        fontWeight = FontWeight.Medium
                    )
                }

                successMessage?.let { message ->
                    Text(
                        text = message,
                        fontSize = 13.sp,
                        color = Color(0xFF2E9E6B),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Adjuntar evidencia (Opcional)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                UploadEvidenceBox()

                Spacer(modifier = Modifier.height(24.dp))

                HelpNoteBox()
            }
        }
    }
}

@Composable
private fun HelpTopBar(
    onBack: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .background(Color.White)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F1F7))
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = "Volver",
                tint = Color(0xFF687083)
            )
        }

        Text(
            text = "Pedir Ayuda",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = ChambaBlue,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun HelpHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFD8F3F6),
                                Color(0xFFB7D9E8),
                                Color(0xFFEAF2F8)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.75f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SupportAgent,
                        contentDescription = null,
                        tint = ChambaBlue,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "¿En qué podemos ayudarte?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Nuestro equipo de soporte está disponible las 24 horas para resolver cualquier duda con tus turnos o pagos.",
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun UploadEvidenceBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(154.dp)
            .border(
                BorderStroke(1.dp, Color(0xFFB8BCD2)),
                RoundedCornerShape(12.dp)
            )
            .background(Color(0xFFF8F8FF), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDDE5FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    tint = ChambaBlue,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Subir fotos o capturas",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = ChambaBlue
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Formatos permitidos: JPG, PNG (Máx. 5MB)",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun HelpNoteBox() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFE2E3ED))
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = Color(0xFFD65A31),
            modifier = Modifier.size(22.dp)
        )

        Column(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = "Nota:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Si el problema es con un pago reciente, recuerda que puede tardar hasta 24 horas hábiles en reflejarse.",
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = TextSecondary
            )
        }
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
private fun HelpScreenPreview() {
    ChambaPeTheme {
        HelpScreen(
            onBack = {},
            onSendMessage = {}
        )
    }
}