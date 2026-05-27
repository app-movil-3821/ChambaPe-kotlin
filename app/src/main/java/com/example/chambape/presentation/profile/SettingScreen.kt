package com.example.chambape.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding

private val ChambaBlue    = Color(0xFF1A3FD8)
private val TextPrimary   = Color(0xFF0D0D0D)
private val TextSecondary = Color(0xFF6B6B6B)

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    var name  by remember { mutableStateOf("Diego") }
    var email by remember { mutableStateOf("diego@ejemplo.com") }
    var phone by remember { mutableStateOf("+51 987 654 321") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color(0xFFF7F8FC)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Atrás",
                        tint               = TextPrimary
                    )
                }
                Text(
                    text       = "Configuración",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(28.dp))

                // Avatar
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8EDFB))
                            .border(2.dp, Color(0xFFDDDDDD), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "😊", fontSize = 48.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(ChambaBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Outlined.AddAPhoto,
                            contentDescription = "Cambiar foto",
                            tint               = Color.White,
                            modifier           = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                TextButton(onClick = { }) {
                    Text(
                        text       = "Cambiar foto",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = ChambaBlue
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Campos
                SettingsField(
                    label         = "Nombre completo",
                    value         = name,
                    onValueChange = { name = it },
                    leadingIcon   = {
                        Icon(
                            imageVector        = Icons.Outlined.Person,
                            contentDescription = null,
                            tint               = Color(0xFFAAAAAA),
                            modifier           = Modifier.size(20.dp)
                        )
                    }
                )

                Spacer(Modifier.height(16.dp))

                SettingsField(
                    label         = "Correo electrónico",
                    value         = email,
                    onValueChange = { email = it },
                    leadingIcon   = {
                        Icon(
                            imageVector        = Icons.Outlined.Email,
                            contentDescription = null,
                            tint               = Color(0xFFAAAAAA),
                            modifier           = Modifier.size(20.dp)
                        )
                    }
                )

                Spacer(Modifier.height(16.dp))

                SettingsField(
                    label         = "Teléfono",
                    value         = phone,
                    onValueChange = { phone = it },
                    leadingIcon   = {
                        Icon(
                            imageVector        = Icons.Outlined.Phone,
                            contentDescription = null,
                            tint               = Color(0xFFAAAAAA),
                            modifier           = Modifier.size(20.dp)
                        )
                    }
                )

                Spacer(Modifier.height(32.dp))
            }

            // Botón Guardar
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
                        text       = "Guardar cambios",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text       = label,
            fontSize   = 14.sp,
            fontWeight = FontWeight.Medium,
            color      = Color(0xFF0D0D0D)
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            modifier      = Modifier.fillMaxWidth(),
            leadingIcon   = leadingIcon,
            singleLine    = true,
            shape         = RoundedCornerShape(12.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor   = Color.White,
                unfocusedBorderColor    = Color(0xFFDDDDDD),
                focusedBorderColor      = ChambaBlue
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(onBack = { })
}