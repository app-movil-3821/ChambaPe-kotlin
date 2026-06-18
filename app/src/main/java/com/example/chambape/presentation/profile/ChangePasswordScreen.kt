package com.example.chambape.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

private val ChambaBlue  = Color(0xFF1A3FD8)
private val TextPrimary = Color(0xFF0D0D0D)
private val ErrorRed    = Color(0xFFD93025)

@Composable
fun ChangePasswordScreen(onBack: () -> Unit) {

    val viewModel: ChangePasswordViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var currentPassword    by remember { mutableStateOf("") }
    var newPassword        by remember { mutableStateOf("") }
    var confirmPassword    by remember { mutableStateOf("") }
    var currentVisible     by remember { mutableStateOf(false) }
    var newVisible         by remember { mutableStateOf(false) }
    var confirmVisible     by remember { mutableStateOf(false) }
    var submitted          by remember { mutableStateOf(false) }

    var currentError  by remember { mutableStateOf<String?>(null) }
    var newError      by remember { mutableStateOf<String?>(null) }
    var confirmError  by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    fun validateNew(p: String): String? {
        if (p.isBlank()) return "La nueva contraseña es requerida"
        if (p.length < 8) return "Mínimo 8 caracteres"
        if (!p.any { it.isUpperCase() }) return "Debe tener al menos una mayúscula"
        if (!p.any { it.isDigit() }) return "Debe tener al menos un número"
        return null
    }

    fun validateConfirm(p: String, c: String): String? {
        if (c.isBlank()) return "Confirma tu nueva contraseña"
        if (p != c) return "Las contraseñas no coinciden"
        return null
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) onBack()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF7F8FC)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── TopBar ────────────────────────────────────────────────────────
            Row(
                modifier          = Modifier.fillMaxWidth().background(Color.White).statusBarsPadding().padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Atrás", tint = TextPrimary)
                }
                Text("Cambiar contraseña", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(28.dp))

                // Error del servidor
                if (uiState.errorMessage != null) {
                    Text(
                        text     = uiState.errorMessage!!,
                        color    = ErrorRed,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )
                }

                // ── Contraseña actual ─────────────────────────────────────────
                PasswordField(
                    label         = "Contraseña actual",
                    value         = currentPassword,
                    onValueChange = { currentPassword = it; if (submitted) currentError = if (it.isBlank()) "Requerida" else null },
                    visible       = currentVisible,
                    onToggle      = { currentVisible = !currentVisible },
                    isError       = currentError != null,
                    errorMessage  = currentError,
                    imeAction     = ImeAction.Next,
                    onNext        = { focusManager.moveFocus(FocusDirection.Down) }
                )

                Spacer(Modifier.height(16.dp))

                // ── Nueva contraseña ──────────────────────────────────────────
                PasswordField(
                    label         = "Nueva contraseña",
                    value         = newPassword,
                    onValueChange = { newPassword = it; if (submitted) newError = validateNew(it) },
                    visible       = newVisible,
                    onToggle      = { newVisible = !newVisible },
                    isError       = newError != null,
                    errorMessage  = newError,
                    imeAction     = ImeAction.Next,
                    onNext        = { focusManager.moveFocus(FocusDirection.Down) }
                )

                Spacer(Modifier.height(16.dp))

                // ── Confirmar nueva contraseña ────────────────────────────────
                PasswordField(
                    label         = "Confirmar nueva contraseña",
                    value         = confirmPassword,
                    onValueChange = { confirmPassword = it; if (submitted) confirmError = validateConfirm(newPassword, it) },
                    visible       = confirmVisible,
                    onToggle      = { confirmVisible = !confirmVisible },
                    isError       = confirmError != null,
                    errorMessage  = confirmError,
                    imeAction     = ImeAction.Done,
                    onNext        = { focusManager.clearFocus() }
                )

                Spacer(Modifier.height(32.dp))
            }

            // ── Botón guardar ─────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = {
                        submitted    = true
                        currentError = if (currentPassword.isBlank()) "Requerida" else null
                        newError     = validateNew(newPassword)
                        confirmError = validateConfirm(newPassword, confirmPassword)
                        if (currentError == null && newError == null && confirmError == null && !uiState.isSaving) {
                            viewModel.changePassword(currentPassword, newPassword)
                        }
                    },
                    enabled  = !uiState.isSaving,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = ChambaBlue)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Guardar nueva contraseña", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun PasswordField(
    label        : String,
    value        : String,
    onValueChange: (String) -> Unit,
    visible      : Boolean,
    onToggle     : () -> Unit,
    isError      : Boolean,
    errorMessage : String?,
    imeAction    : ImeAction,
    onNext       : () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            modifier      = Modifier.fillMaxWidth(),
            placeholder   = { Text("••••••••", color = Color(0xFFAAAAAA)) },
            leadingIcon   = { Icon(Icons.Outlined.Lock, null, tint = Color(0xFFAAAAAA)) },
            trailingIcon  = {
                IconButton(onClick = onToggle) {
                    Icon(
                        if (visible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        null, tint = Color(0xFFAAAAAA)
                    )
                }
            },
            isError              = isError,
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
            keyboardActions      = KeyboardActions(onNext = { onNext() }, onDone = { onNext() }),
            singleLine           = true,
            shape                = RoundedCornerShape(12.dp),
            colors               = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = ChambaBlue,
                unfocusedBorderColor = Color(0xFFDDDDDD),
                errorBorderColor     = ErrorRed,
                focusedContainerColor   = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
        if (errorMessage != null) {
            Text(errorMessage, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangePasswordScreenPreview() {
    ChangePasswordScreen(onBack = {})
}