package com.example.chambape.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaPeBlue = Color(0xFF1A3FD8)

data class Skill(
    val id: Int,
    val name: String,
    val emoji: String
)

private val skillList = listOf(
    Skill(1, "Mesero",   "🍽️"),
    Skill(2, "Cajero",   "🏧"),
    Skill(3, "Cocina",   "👨‍🍳"),
    Skill(4, "Reparto",  "🚴"),
    Skill(5, "Almacén",  "🏭"),
    Skill(6, "Limpieza", "🧹")
)

@Composable
fun SkillsScreen(
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    var selectedSkills by remember { mutableStateOf(setOf<Int>()) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color(0xFFF7F8FC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(52.dp))

            // Back + progress indicator
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Atrás",
                        tint               = Color(0xFF0D0D0D)
                    )
                }

                // Progress dots (paso 2 de 3)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(3) { index ->
                        androidx.compose.foundation.Canvas(
                            modifier = Modifier.size(
                                width  = if (index == 1) 24.dp else 8.dp,
                                height = 8.dp
                            )
                        ) {
                            drawRoundRect(
                                color       = if (index == 1) ChambaPeBlue
                                else Color(0xFFDDDDDD),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // Título
            Text(
                text       = "¿En qué eres bueno?",
                fontSize   = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color(0xFF0D0D0D)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text     = "Selecciona tus habilidades para encontrar los mejores turnos.",
                fontSize = 14.sp,
                color    = Color(0xFF6B6B6B),
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(28.dp))

            // Grid de habilidades
            LazyVerticalGrid(
                columns         = GridCells.Fixed(2),
                modifier        = Modifier.weight(1f),
                contentPadding  = PaddingValues(bottom = 16.dp),
                verticalArrangement   = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(skillList) { skill ->
                    val isSelected = skill.id in selectedSkills
                    SkillCard(
                        skill      = skill,
                        isSelected = isSelected,
                        onClick    = {
                            selectedSkills = if (isSelected) {
                                selectedSkills - skill.id
                            } else {
                                selectedSkills + skill.id
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Botón Continuar
            Button(
                onClick  = { onContinue() },
                enabled  = selectedSkills.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(bottom = 0.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor         = ChambaPeBlue,
                    disabledContainerColor = Color(0xFFBBCCF5)
                )
            ) {
                Text(
                    text       = "Continuar",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun SkillCard(
    skill: Skill,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ChambaPeBlue else Color.White
        ),
        border = if (isSelected) null
        else BorderStroke(1.dp, Color(0xFFE8E8E8)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 0.dp else 2.dp
        )
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            Text(
                text     = skill.emoji,
                fontSize = 36.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text       = skill.name,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color      = if (isSelected) Color.White else Color(0xFF0D0D0D)
            )
        }
    }
}