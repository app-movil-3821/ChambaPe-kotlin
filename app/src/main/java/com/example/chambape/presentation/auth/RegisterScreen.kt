package com.example.chambape.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chambape.di.AppModule

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
    onRegisterSuccess           : () -> Unit,  // Contratante → Main
    onRegisterSuccessChambeador : () -> Unit = onRegisterSuccess, // Chambeador → Skills
    onGoToLogin                 : () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(AppModule.authRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    var fullName        by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible  by remember { mutableStateOf(false) }
    var selectedRole    by remember { mutableStateOf("") } // "CHAMBEADOR" o "CONTRATANTE"

    var fullNameError        by remember { mutableStateOf<String?>(null) }
    var emailError           by remember { mutableStateOf<String?>(null) }
    var phoneError           by remember { mutableStateOf<String?>(null) }
    var passwordError        by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var roleError            by remember { mutableStateOf(false) }
    var submitted            by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val isLoading    = uiState is RegisterUiState.Loading

    val isFormValid = validateFullName(fullName) == null &&
            validateEmail(email) == null &&
            validatePhone(phone) == null &&
            validatePassword(password) == null &&
            validateConfirmPassword(password, confirmPassword) == null &&
            selectedRole.isNotBlank()

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is RegisterUiState.Success -> {
                // Contratante → directo a Main (no necesita skills)
                // Chambeador → pasa por Skills para completar perfil
                if (state.role == "CONTRATANTE") {
                    onRegisterSuccess()
                } else {
                    onRegisterSuccessChambeador()
                }
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            Text("ChambaYa", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = ChambaBlue)
            Spacer(Modifier.height(8.dp))
            Text(
                text      = "Regístrate para empezar a trabajar hoy mismo.",
                fontSize  = 15.sp,
                color     = Color(0xFF6B6B6B),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // ── Error del servidor ─────────────────────────────────────────────
            if (uiState is RegisterUiState.Error) {
                Text(
                    text     = (uiState as RegisterUiState.Error).message,
                    color    = ErrorRed,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }

            // ── Selector de rol ───────────────────────────────────────────────
            Text(
                text      = "¿Cómo quieres usar ChambaYa?",
                fontSize  = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color     = Color(0xFF0D0D0D),
                modifier  = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text     = "Elige tu rol para personalizar tu experiencia.",
                fontSize = 13.sp,
                color    = Color(0xFF6B6B6B),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoleCard(
                    emoji       = "🛠️",
                    title       = "Chambeador",
                    description = "Busco trabajo temporal",
                    selected    = selectedRole == "CHAMBEADOR",
                    modifier    = Modifier.weight(1f),
                    onClick     = { selectedRole = "CHAMBEADOR"; roleError = false }
                )
                RoleCard(
                    emoji       = "🏢",
                    title       = "Contratante",
                    description = "Necesito contratar personal",
                    selected    = selectedRole == "CONTRATANTE",
                    modifier    = Modifier.weight(1f),
                    onClick     = { selectedRole = "CONTRATANTE"; roleError = false }
                )
            }

            if (roleError) {
                Text(
                    text     = "Selecciona un rol para continuar",
                    color    = ErrorRed,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(Modifier.height(24.dp))

            // ── Nombre ────────────────────────────────────────────────────────
            RegisterField(
                label         = "Nombre completo",
                value         = fullName,
                onValueChange = { fullName = it; if (submitted) fullNameError = validateFullName(it) },
                placeholder   = "Ej. Juan Pérez",
                leadingIcon   = { Icon(Icons.Outlined.Person, null, tint = Color(0xFFAAAAAA)) },
                isError       = fullNameError != null,
                errorMessage  = fullNameError,
                imeAction     = ImeAction.Next,
                onNext        = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(Modifier.height(14.dp))

            // ── Email ─────────────────────────────────────────────────────────
            RegisterField(
                label         = "Correo electrónico",
                value         = email,
                onValueChange = { email = it; if (submitted) emailError = validateEmail(it) },
                placeholder   = "nombre@ejemplo.com",
                leadingIcon   = { Icon(Icons.Outlined.Email, null, tint = Color(0xFFAAAAAA)) },
                isError       = emailError != null,
                errorMessage  = emailError,
                keyboardType  = KeyboardType.Email,
                imeAction     = ImeAction.Next,
                onNext        = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(Modifier.height(14.dp))

            // ── Teléfono ──────────────────────────────────────────────────────
            RegisterField(
                label         = "Teléfono",
                value         = phone,
                onValueChange = { phone = it; if (submitted) phoneError = validatePhone(it) },
                placeholder   = "+51 987 654 321",
                leadingIcon   = { Icon(Icons.Outlined.Phone, null, tint = Color(0xFFAAAAAA)) },
                isError       = phoneError != null,
                errorMessage  = phoneError,
                keyboardType  = KeyboardType.Phone,
                imeAction     = ImeAction.Next,
                onNext        = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(Modifier.height(14.dp))

            // ── Contraseña ────────────────────────────────────────────────────
            Text("Contraseña", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D0D0D), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = password,
                onValueChange = { password = it; if (submitted) passwordError = validatePassword(it) },
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
                Text(passwordError!!, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 4.dp))
            }

            Spacer(Modifier.height(14.dp))

            // ── Confirmar contraseña ──────────────────────────────────────────
            Text("Confirmar contraseña", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D0D0D), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = confirmPassword,
                onValueChange = { confirmPassword = it; if (submitted) confirmPasswordError = validateConfirmPassword(password, it) },
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
                Text(confirmPasswordError!!, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 4.dp))
            }

            Spacer(Modifier.height(28.dp))

            // ── Botón Registrarse ─────────────────────────────────────────────
            Button(
                onClick = {
                    submitted            = true
                    fullNameError        = validateFullName(fullName)
                    emailError           = validateEmail(email)
                    phoneError           = validatePhone(phone)
                    passwordError        = validatePassword(password)
                    confirmPasswordError = validateConfirmPassword(password, confirmPassword)
                    roleError            = selectedRole.isBlank()
                    if (isFormValid && !isLoading) {
                        viewModel.register(fullName, email, phone, password, selectedRole)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape    = RoundedCornerShape(14.dp),
                enabled  = !isLoading,
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = ChambaBlue,
                    disabledContainerColor = Color(0xFFBBCCF5)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── ¿Ya tienes cuenta? ────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth().padding(bottom = 28.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("¿Ya tienes una cuenta?", fontSize = 14.sp, color = Color(0xFF6B6B6B))
                TextButton(
                    onClick        = { onGoToLogin() },
                    contentPadding = PaddingValues(start = 4.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
                ) {
                    Text("Inicia sesión", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ChambaBlue)
                }
            }
        }
    }
}

// ─── Tarjeta de rol seleccionable ─────────────────────────────────────────────

@Composable
private fun RoleCard(
    emoji      : String,
    title      : String,
    description: String,
    selected   : Boolean,
    modifier   : Modifier = Modifier,
    onClick    : () -> Unit
) {
    val borderColor = if (selected) ChambaBlue else Color(0xFFDDDDDD)
    val bgColor     = if (selected) Color(0xFFEEF2FF) else Color.White

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 28.sp)
        Spacer(Modifier.height(8.dp))
        Text(
            text       = title,
            fontSize   = 14.sp,
            fontWeight = FontWeight.Bold,
            color      = if (selected) ChambaBlue else Color(0xFF0D0D0D),
            textAlign  = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text      = description,
            fontSize  = 12.sp,
            color     = Color(0xFF6B6B6B),
            textAlign = TextAlign.Center
        )
    }
}

// ─── Campo reutilizable ───────────────────────────────────────────────────────

@Composable
private fun RegisterField(
    label        : String,
    value        : String,
    onValueChange: (String) -> Unit,
    placeholder  : String,
    leadingIcon  : @Composable () -> Unit,
    isError      : Boolean,
    errorMessage : String?,
    keyboardType : KeyboardType = KeyboardType.Text,
    imeAction    : ImeAction    = ImeAction.Next,
    onNext       : () -> Unit   = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D0D0D))
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
                focusedBorderColor   = ChambaBlue,
                unfocusedBorderColor = Color(0xFFDDDDDD),
                errorBorderColor     = ErrorRed
            )
        )
        if (errorMessage != null) {
            Text(errorMessage, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onRegisterSuccess = { }, onGoToLogin = { })
}