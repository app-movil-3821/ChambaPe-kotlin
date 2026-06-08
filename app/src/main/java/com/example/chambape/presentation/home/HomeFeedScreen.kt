package com.example.chambape.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val BackgroundGray  = Color(0xFFF7F8FC)
private val ChambaPeBlue    = Color(0xFF0B57D0)
private val TextPrimary     = Color(0xFF1D1B20)
private val TextSecondary   = Color(0xFF49454F)

@Composable
fun HomeFeedScreen(
    viewModel: HomeFeedViewModel = viewModel(),
    onJobClick: (jobId: String) -> Unit
) {
    val jobs by viewModel.jobs.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = BackgroundGray
    ) {
        LazyColumn(
            contentPadding      = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(jobs) { job ->
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(16.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.Top
                        ) {
                            Text(
                                text       = job.title,
                                fontSize   = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color      = TextPrimary,
                                modifier   = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector        = Icons.Outlined.FavoriteBorder,
                                contentDescription = "Guardar",
                                tint               = TextSecondary,
                                modifier           = Modifier
                                    .padding(start = 8.dp)
                                    .clickable { }
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector        = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint               = TextSecondary,
                                modifier           = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text     = job.district,
                                fontSize = 14.sp,
                                color    = TextSecondary
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                text       = "S/ ${job.paymentAmount}",
                                fontSize   = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = TextPrimary
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFFE8F5E9))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text       = "● Available",
                                    fontSize   = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color      = Color(0xFF43A047)
                                )
                            }

                            Button(
                                onClick        = { onJobClick(job.id) },
                                shape          = RoundedCornerShape(10.dp),
                                colors         = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text       = "Aplicar",
                                    fontSize   = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color      = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}