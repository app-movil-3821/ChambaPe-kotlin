package com.example.chambape.presentation.auth
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaPeBlue = Color(0xFF1A3FD8)

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit
) {
    var fullName         by remember { mutableStateOf("") }
    var email            by remember { mutableStateOf("") }
    var phone            by remember { mutableStateOf("") }
    var password         by remember { mutableStateOf("") }
    var passwordVisible  by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // Logo
            Text(
                text       = "ChambaYa",
                fontSize   = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = ChambaPeBlue
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text      = "Regístrate para empezar a trabajar hoy mismo.",
                fontSize  = 15.sp,
                color     = Color(0xFF6B6B6B),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // Fila Google + Apple
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Google
                OutlinedButton(
                    onClick   = { },
                    modifier  = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape  = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFDDDDDD))
                ) {
                    Row(
                        verticalAlignment      = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text       = "G",
                            fontSize   = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFF4285F4)
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(
                            text     = "Google",
                            fontSize = 14.sp,
                            color    = Color(0xFF0D0D0D)
                        )
                    }
                }

                // Apple
                OutlinedButton(
                    onClick   = { },
                    modifier  = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape  = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFDDDDDD))
                ) {
                    Row(
                        verticalAlignment      = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text       = "⌘",
                            fontSize   = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFF0D0D0D)
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(
                            text     = "Apple",
                            fontSize = 14.sp,
                            color    = Color(0xFF0D0D0D)
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Divider "o regístrate con"
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                Text(
                    text     = "  o regístrate con  ",
                    fontSize = 13.sp,
                    color    = Color(0xFF9E9E9E)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
            }

            Spacer(Modifier.height(24.dp))

            // Campo Nombre completo
            Text(
                text       = "Nombre completo",
                fontSize   = 14.sp,
                fontWeight = FontWeight.Medium,
                color      = Color(0xFF0D0D0D),
                modifier   = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = fullName,
                onValueChange = { fullName = it },
                modifier      = Modifier.fillMaxWidth(),
                placeholder   = { Text("Ej. Juan Pérez", color = Color(0xFFAAAAAA)) },
                leadingIcon   = {
                    Icon(
                        imageVector        = Icons.Outlined.Person,
                        contentDescription = null,
                        tint               = Color(0xFFAAAAAA)
                    )
                },
                singleLine = true,
                shape      = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Campo Correo
            Text(
                text       = "Correo electrónico",
                fontSize   = 14.sp,
                fontWeight = FontWeight.Medium,
                color      = Color(0xFF0D0D0D),
                modifier   = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = email,
                onValueChange = { email = it },
                modifier      = Modifier.fillMaxWidth(),
                placeholder   = { Text("nombre@ejemplo.com", color = Color(0xFFAAAAAA)) },
                leadingIcon   = {
                    Icon(
                        imageVector        = Icons.Outlined.Email,
                        contentDescription = null,
                        tint               = Color(0xFFAAAAAA)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine      = true,
                shape           = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Campo Teléfono
            Text(
                text       = "Teléfono",
                fontSize   = 14.sp,
                fontWeight = FontWeight.Medium,
                color      = Color(0xFF0D0D0D),
                modifier   = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = phone,
                onValueChange = { phone = it },
                modifier      = Modifier.fillMaxWidth(),
                placeholder   = { Text("+51 987 654 321", color = Color(0xFFAAAAAA)) },
                leadingIcon   = {
                    Icon(
                        imageVector        = Icons.Outlined.Phone,
                        contentDescription = null,
                        tint               = Color(0xFFAAAAAA)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine      = true,
                shape           = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Campo Contraseña
            Text(
                text       = "Contraseña",
                fontSize   = 14.sp,
                fontWeight = FontWeight.Medium,
                color      = Color(0xFF0D0D0D),
                modifier   = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = password,
                onValueChange = { password = it },
                modifier      = Modifier.fillMaxWidth(),
                placeholder   = { Text("••••••••", color = Color(0xFFAAAAAA)) },
                leadingIcon   = {
                    Icon(
                        imageVector        = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint               = Color(0xFFAAAAAA)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector        = if (passwordVisible) Icons.Outlined.Visibility
                            else Icons.Outlined.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                            tint               = Color(0xFFAAAAAA)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine           = true,
                shape                = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(28.dp))

            // Botón Sign Up
            Button(
                onClick  = { onRegisterSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue)
            ) {
                Text(
                    text       = "Sign Up",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }

            Spacer(Modifier.height(20.dp))

            // ¿Ya tienes cuenta?
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text     = "¿Ya tienes una cuenta?",
                    fontSize = 14.sp,
                    color    = Color(0xFF6B6B6B)
                )
                TextButton(
                    onClick        = { onGoToLogin() },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = 4.dp, end = 0.dp, top = 0.dp, bottom = 0.dp
                    )
                ) {
                    Text(
                        text       = "Inicia sesión",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color      = ChambaPeBlue
                    )
                }
            }
        }
    }
}