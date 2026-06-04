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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaBlue = Color(0xFF1A3FD8)
private val ErrorRed   = Color(0xFFD93025)

// ─── Validaciones ─────────────────────────────────────────────────────────────

fun validateFullName(name: String): String? {
    if (name.isBlank()) return "El nombre es requerido"
    if (name.trim().split(" ").size < 2) return "Ingresa nombre y apellido"
    if (name.trim().length < 5) return "Nombre demasiado corto"
    return null
}

fun validatePhone(phone: String): String? {
    if (phone.isBlank()) return "El teléfono es requerido"
    val digits = phone.filter { it.isDigit() }
    if (digits.length < 9) return "Mínimo 9 dígitos"
    return null
}

fun validatePassword(password: String): String? {
    if (password.isBlank()) return "La contraseña es requerida"
    if (password.length < 8) return "Mínimo 8 caracteres"
    if (!password.any { it.isUpperCase() }) return "Debe tener al menos una mayúscula"
    if (!password.any { it.isDigit() }) return "Debe tener al menos un número"
    return null
}

fun validateConfirmPassword(password: String, confirm: String): String? {
    if (confirm.isBlank()) return "Confirma tu contraseña"
    if (password != confirm) return "Las contraseñas no coinciden"
    return null
}

// ─── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit
) {
    var fullName        by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible  by remember { mutableStateOf(false) }

    var fullNameError       by remember { mutableStateOf<String?>(null) }
    var emailError          by remember { mutableStateOf<String?>(null) }
    var phoneError          by remember { mutableStateOf<String?>(null) }
    var passwordError       by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var submitted           by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val isFormValid = validateFullName(fullName) == null &&
            validateEmail(email) == null &&
            validatePhone(phone) == null &&
            validatePassword(password) == null &&
            validateConfirmPassword(password, confirmPassword) == null

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            Text(text = "ChambaYa", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = ChambaBlue)
            Spacer(Modifier.height(12.dp))
            Text(text = "Regístrate para empezar a trabajar hoy mismo.", fontSize = 15.sp, color = Color(0xFF6B6B6B), textAlign = TextAlign.Center)

            Spacer(Modifier.height(28.dp))

            // ── Google + Apple ─────────────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { }, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color(0xFFDDDDDD))) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("G", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4285F4))
                        Spacer(Modifier.size(8.dp))
                        Text("Google", fontSize = 14.sp, color = Color(0xFF0D0D0D))
                    }
                }
                OutlinedButton(onClick = { }, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color(0xFFDDDDDD))) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⌘", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D0D0D))
                        Spacer(Modifier.size(8.dp))
                        Text("Apple", fontSize = 14.sp, color = Color(0xFF0D0D0D))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                Text("  o regístrate con  ", fontSize = 13.sp, color = Color(0xFF9E9E9E))
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
            }

            Spacer(Modifier.height(24.dp))

            // ── Nombre ─────────────────────────────────────────────────────────
            RegisterField(
                label         = "Nombre completo",
                value         = fullName,
                onValueChange = {
                    fullName = it
                    if (submitted) fullNameError = validateFullName(it)
                },
                placeholder   = "Ej. Juan Pérez",
                leadingIcon   = { Icon(Icons.Outlined.Person, null, tint = Color(0xFFAAAAAA)) },
                isError       = fullNameError != null,
                errorMessage  = fullNameError,
                imeAction     = ImeAction.Next,
                onNext        = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(Modifier.height(14.dp))

            // ── Email ──────────────────────────────────────────────────────────
            RegisterField(
                label         = "Correo electrónico",
                value         = email,
                onValueChange = {
                    email = it
                    if (submitted) emailError = validateEmail(it)
                },
                placeholder   = "nombre@ejemplo.com",
                leadingIcon   = { Icon(Icons.Outlined.Email, null, tint = Color(0xFFAAAAAA)) },
                isError       = emailError != null,
                errorMessage  = emailError,
                keyboardType  = KeyboardType.Email,
                imeAction     = ImeAction.Next,
                onNext        = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(Modifier.height(14.dp))

            // ── Teléfono ───────────────────────────────────────────────────────
            RegisterField(
                label         = "Teléfono",
                value         = phone,
                onValueChange = {
                    phone = it
                    if (submitted) phoneError = validatePhone(it)
                },
                placeholder   = "+51 987 654 321",
                leadingIcon   = { Icon(Icons.Outlined.Phone, null, tint = Color(0xFFAAAAAA)) },
                isError       = phoneError != null,
                errorMessage  = phoneError,
                keyboardType  = KeyboardType.Phone,
                imeAction     = ImeAction.Next,
                onNext        = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(Modifier.height(14.dp))

            // ── Contraseña ─────────────────────────────────────────────────────
            Text(text = "Contraseña", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D0D0D), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = password,
                onValueChange = {
                    password = it
                    if (submitted) passwordError = validatePassword(it)
                },
                modifier      = Modifier.fillMaxWidth(),
                placeholder   = { Text("••••••••", color = Color(0xFFAAAAAA)) },
                leadingIcon   = { Icon(Icons.Outlined.Lock, null, tint = Color(0xFFAAAAAA)) },
                trailingIcon  = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff, null, tint = Color(0xFFAAAAAA))
                    }
                },
                isError              = passwordError != null,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                keyboardActions      = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine           = true,
                shape                = RoundedCornerShape(12.dp),
                colors               = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChambaBlue, unfocusedBorderColor = Color(0xFFDDDDDD), errorBorderColor = ErrorRed
                )
            )
            if (passwordError != null) {
                Text(text = passwordError!!, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 4.dp))
            }

            Spacer(Modifier.height(14.dp))

            // ── Confirmar contraseña ───────────────────────────────────────────
            Text(text = "Confirmar contraseña", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D0D0D), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (submitted) confirmPasswordError = validateConfirmPassword(password, it)
                },
                modifier      = Modifier.fillMaxWidth(),
                placeholder   = { Text("••••••••", color = Color(0xFFAAAAAA)) },
                leadingIcon   = { Icon(Icons.Outlined.Lock, null, tint = Color(0xFFAAAAAA)) },
                trailingIcon  = {
                    IconButton(onClick = { confirmVisible = !confirmVisible }) {
                        Icon(if (confirmVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff, null, tint = Color(0xFFAAAAAA))
                    }
                },
                isError              = confirmPasswordError != null,
                visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions      = KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine           = true,
                shape                = RoundedCornerShape(12.dp),
                colors               = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChambaBlue, unfocusedBorderColor = Color(0xFFDDDDDD), errorBorderColor = ErrorRed
                )
            )
            if (confirmPasswordError != null) {
                Text(text = confirmPasswordError!!, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 4.dp))
            }

            Spacer(Modifier.height(28.dp))

            // ── Botón Sign Up ──────────────────────────────────────────────────
            Button(
                onClick = {
                    submitted            = true
                    fullNameError        = validateFullName(fullName)
                    emailError           = validateEmail(email)
                    phoneError           = validatePhone(phone)
                    passwordError        = validatePassword(password)
                    confirmPasswordError = validateConfirmPassword(password, confirmPassword)
                    if (isFormValid) onRegisterSuccess()
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = ChambaBlue)
            ) {
                Text(text = "Sign Up", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Spacer(Modifier.height(20.dp))

            // ── ¿Ya tienes cuenta? ─────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth().padding(bottom = 28.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("¿Ya tienes una cuenta?", fontSize = 14.sp, color = Color(0xFF6B6B6B))
                TextButton(
                    onClick        = { onGoToLogin() },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 4.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
                ) {
                    Text(text = "Inicia sesión", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ChambaBlue)
                }
            }
        }
    }
}

// ─── Campo reutilizable ───────────────────────────────────────────────────────

@Composable
private fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    isError: Boolean,
    errorMessage: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction       = ImeAction.Next,
    onNext: () -> Unit         = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D0D0D))
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value           = value,
            onValueChange   = onValueChange,
            modifier        = Modifier.fillMaxWidth(),
            placeholder     = { Text(placeholder, color = Color(0xFFAAAAAA)) },
            leadingIcon     = leadingIcon,
            isError         = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(onNext = { onNext() }),
            singleLine      = true,
            shape           = RoundedCornerShape(12.dp),
            colors          = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Color(0xFF1A3FD8),
                unfocusedBorderColor = Color(0xFFDDDDDD),
                errorBorderColor     = Color(0xFFD93025)
            )
        )
        if (errorMessage != null) {
            Text(text = errorMessage, color = Color(0xFFD93025), fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onRegisterSuccess = { }, onGoToLogin = { })
}