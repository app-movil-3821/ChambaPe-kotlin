package com.example.chambape.presentation.auth

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chambape.di.AppModule

private val ChambaPeBlue = Color(0xFF1A3FD8)
private val ErrorRed     = Color(0xFFD93025)

fun validateEmail(email: String): String? {
    if (email.isBlank()) return "El email es requerido"
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return "Ingresa un email válido"
    return null
}

fun validatePasswordLogin(password: String): String? {
    if (password.isBlank()) return "La contraseña es requerida"
    if (password.length < 6) return "Mínimo 6 caracteres" // ← cambia 8 por 6
    return null
}


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(AppModule.authRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError      by remember { mutableStateOf<String?>(null) }
    var passwordError   by remember { mutableStateOf<String?>(null) }
    var submitted       by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val isFormValid  = validateEmail(email) == null && validatePasswordLogin(password) == null
    val isLoading    = uiState is LoginUiState.Loading

    // Navegar cuando login es exitoso
    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(48.dp))

            Text(text = "ChambaYa", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = ChambaPeBlue)

            Spacer(Modifier.height(40.dp))

            Text(text = "Bienvenido de nuevo", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D0D0D))
            Spacer(Modifier.height(8.dp))
            Text(text = "Inicia sesión para encontrar tu próximo chambe.", fontSize = 15.sp, color = Color(0xFF6B6B6B))

            Spacer(Modifier.height(32.dp))

            // Error del servidor
            if (uiState is LoginUiState.Error) {
                Text(
                    text     = (uiState as LoginUiState.Error).message,
                    color    = ErrorRed,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }

            // ── Email ──────────────────────────────────────────────────────────
            Text(text = "Email o Teléfono", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D0D0D))
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = email,
                onValueChange = {
                    email = it
                    if (submitted) emailError = validateEmail(it)
                },
                modifier        = Modifier.fillMaxWidth(),
                placeholder     = { Text("nombre@ejemplo.com", color = Color(0xFFAAAAAA)) },
                leadingIcon     = { Icon(Icons.Outlined.Email, null, tint = Color(0xFFAAAAAA)) },
                isError         = emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine      = true,
                shape           = RoundedCornerShape(12.dp),
                colors          = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = ChambaPeBlue,
                    unfocusedBorderColor = Color(0xFFDDDDDD),
                    errorBorderColor     = ErrorRed
                )
            )
            if (emailError != null) {
                Text(text = emailError!!, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
            }

            Spacer(Modifier.height(16.dp))

            // ── Contraseña ─────────────────────────────────────────────────────
            Text(text = "Contraseña", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D0D0D))
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = password,
                onValueChange = {
                    password = it
                    if (submitted) passwordError = validatePasswordLogin(it)
                },
                modifier      = Modifier.fillMaxWidth(),
                placeholder   = { Text("••••••••", color = Color(0xFFAAAAAA)) },
                leadingIcon   = { Icon(Icons.Outlined.Lock, null, tint = Color(0xFFAAAAAA)) },
                trailingIcon  = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFFAAAAAA)
                        )
                    }
                },
                isError              = passwordError != null,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions      = KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine           = true,
                shape                = RoundedCornerShape(12.dp),
                colors               = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = ChambaPeBlue,
                    unfocusedBorderColor = Color(0xFFDDDDDD),
                    errorBorderColor     = ErrorRed
                )
            )
            if (passwordError != null) {
                Text(text = passwordError!!, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { }) {
                    Text("¿Olvidaste tu contraseña?", color = ChambaPeBlue, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Botón ──────────────────────────────────────────────────────────
            Button(
                onClick = {
                    submitted     = true
                    emailError    = validateEmail(email)
                    passwordError = validatePasswordLogin(password)
                    if (isFormValid && !isLoading) {
                        viewModel.login(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(12.dp),
                enabled  = !isLoading,
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = ChambaPeBlue,
                    disabledContainerColor = Color(0xFFBBCCF5)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color    = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Divider ────────────────────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                Text("  o continúa con  ", fontSize = 13.sp, color = Color(0xFF9E9E9E))
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
            }

            Spacer(Modifier.height(20.dp))

            // ── Google ─────────────────────────────────────────────────────────
            OutlinedButton(
                onClick  = { },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(12.dp),
                border   = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("G", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4285F4))
                    Spacer(Modifier.size(10.dp))
                    Text("Google", fontSize = 15.sp, color = Color(0xFF0D0D0D))
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Apple ──────────────────────────────────────────────────────────
            Button(
                onClick  = { },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⌘", fontSize = 20.sp, color = Color.White)
                    Spacer(Modifier.size(10.dp))
                    Text("Apple", fontSize = 15.sp, color = Color.White)
                }
            }

            Spacer(Modifier.weight(1f))

            // ── ¿No tienes cuenta? ─────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("¿No tienes cuenta? ", fontSize = 14.sp, color = Color(0xFF6B6B6B))
                TextButton(
                    onClick        = { onGoToRegister() },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Text(text = "Regístrate", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ChambaPeBlue)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = { }, onGoToRegister = { })
}