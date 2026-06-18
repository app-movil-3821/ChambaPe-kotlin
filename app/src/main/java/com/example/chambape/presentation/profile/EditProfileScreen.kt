package com.example.chambape.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val ChambaBlue    = Color(0xFF1A3FD8)
private val TextPrimary   = Color(0xFF0D0D0D)
private val TextSecondary = Color(0xFF6B6B6B)

@Composable
fun EditProfileScreen(
    onBack: () -> Unit
) {
    val viewModel: EditProfileViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.load() }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) onBack()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF7F8FC)) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier          = Modifier.fillMaxWidth().background(Color.White).statusBarsPadding().padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Atrás", tint = TextPrimary)
                }
                Text("Editar perfil", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            Column(
                modifier            = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(28.dp))

                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier         = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFFE8EDFB)).border(2.dp, Color(0xFFDDDDDD), CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Text("😊", fontSize = 48.sp) }
                    Box(
                        modifier         = Modifier.size(30.dp).clip(CircleShape).background(ChambaBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.AddAPhoto, "Cambiar foto", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { }) {
                    Text("Cambiar foto", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = ChambaBlue)
                }
                Spacer(Modifier.height(24.dp))

                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(color = ChambaBlue)
                    }

                    else -> {
                        EditProfileField(
                            label         = "Nombre completo",
                            value         = uiState.name,
                            onValueChange = { viewModel.onNameChange(it) },
                            leadingIcon   = { Icon(Icons.Outlined.Person, null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(20.dp)) }
                        )

                        Spacer(Modifier.height(16.dp))

                        EditProfileField(
                            label         = "Correo electrónico",
                            value         = uiState.email,
                            onValueChange = { },
                            enabled       = false,
                            leadingIcon   = { Icon(Icons.Outlined.Email, null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(20.dp)) }
                        )

                        Spacer(Modifier.height(16.dp))

                        EditProfileField(
                            label         = "Teléfono",
                            value         = uiState.phone,
                            onValueChange = { viewModel.onPhoneChange(it) },
                            leadingIcon   = { Icon(Icons.Outlined.Phone, null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(20.dp)) }
                        )

                        Spacer(Modifier.height(16.dp))

                        EditProfileField(
                            label         = "Distrito",
                            value         = uiState.district,
                            onValueChange = { viewModel.onDistrictChange(it) },
                            leadingIcon   = { Icon(Icons.Outlined.LocationOn, null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(20.dp)) }
                        )

                        Spacer(Modifier.height(16.dp))

                        EditProfileField(
                            label         = "Experiencia",
                            value         = uiState.experience,
                            onValueChange = { viewModel.onExperienceChange(it) },
                            leadingIcon   = { Icon(Icons.Outlined.Work, null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(20.dp)) }
                        )

                        Spacer(Modifier.height(20.dp))

                        // ── Skills ────────────────────────────────────────────
                        Text(
                            text       = "Habilidades",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color      = TextPrimary,
                            modifier   = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(10.dp))
                        SkillsSelector(
                            selected = uiState.skills,
                            onToggle = { viewModel.onSkillToggle(it) }
                        )

                        if (uiState.errorMessage != null) {
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text       = uiState.errorMessage!!,
                                fontSize   = 14.sp,
                                color      = Color(0xFFD93025),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }

            Box(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 20.dp, vertical = 16.dp).navigationBarsPadding()
            ) {
                Button(
                    onClick  = { viewModel.save() },
                    enabled  = !uiState.isSaving && !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = ChambaBlue)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Guardar cambios", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}

private val editableSkills = listOf(
    "Mesero", "Cajero", "Cocina", "Reparto", "Almacén", "Limpieza"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillsSelector(
    selected: List<String>,
    onToggle: (String) -> Unit
) {
    FlowRow(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement   = Arrangement.spacedBy(8.dp)
    ) {
        editableSkills.forEach { skill ->
            val isSelected = skill in selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) ChambaBlue else Color.White)
                    .border(1.dp, if (isSelected) ChambaBlue else Color(0xFFDDDDDD), RoundedCornerShape(50))
                    .clickable { onToggle(skill) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text       = skill,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color      = if (isSelected) Color.White else TextPrimary
                )
            }
        }
    }
}

@Composable
private fun EditProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    leadingIcon: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            enabled       = enabled,
            modifier      = Modifier.fillMaxWidth(),
            leadingIcon   = leadingIcon,
            singleLine    = true,
            shape         = RoundedCornerShape(12.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor  = Color.White,
                focusedContainerColor    = Color.White,
                disabledContainerColor   = Color(0xFFF5F5F5),
                unfocusedBorderColor     = Color(0xFFDDDDDD),
                focusedBorderColor       = ChambaBlue,
                disabledBorderColor      = Color(0xFFEEEEEE),
                disabledTextColor        = TextSecondary
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(onBack = { })
}