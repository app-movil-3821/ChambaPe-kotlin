package com.example.chambape.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val ChambaBlue    = Color(0xFF1A3FD8)
private val BackgroundGray = Color(0xFFF7F8FC)
private val GreenIn        = Color(0xFF2E7D32)
private val RedOut         = Color(0xFFE53935)

@Composable
fun WalletScreen(onBack: () -> Unit = {}) {

    val viewModel: WalletViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.load() }

    Surface(modifier = Modifier.fillMaxSize(), color = BackgroundGray) {
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ChambaBlue)
                }
            }

            uiState.errorMessage != null -> {
                Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(uiState.errorMessage!!, color = Color(0xFFD93025), fontSize = 15.sp)
                }
            }

            else -> {
                LazyColumn(
                    modifier       = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    // Top bar
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .statusBarsPadding()
                                .padding(horizontal = 8.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onBack) {
                                Icon(Icons.Outlined.ArrowBack, "Volver", tint = ChambaBlue)
                            }
                            Text(
                                text       = "Billetera",
                                fontSize   = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color      = Color(0xFF0D0D0D)
                            )
                        }
                    }

                    // Balance card
                    item {
                        Spacer(Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.linearGradient(listOf(Color(0xFF1A3FD8), Color(0xFF2D5BE3)))
                                )
                                .padding(24.dp)
                        ) {
                            Column {
                                Text(
                                    text     = "Ganancias totales",
                                    fontSize = 13.sp,
                                    color    = Color.White.copy(alpha = 0.8f)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text       = "S/ ${"%.2f".format(uiState.balance)}",
                                    fontSize   = 34.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color      = Color.White
                                )
                            }
                        }
                        Spacer(Modifier.height(24.dp))

                        if (uiState.transactions.isEmpty()) {
                            Box(
                                modifier            = Modifier.fillMaxWidth().padding(32.dp),
                                contentAlignment    = Alignment.Center
                            ) {
                                Text(
                                    text      = "Aún no tienes movimientos.\nCompleta tu primer turno para ver tus ganancias aquí.",
                                    fontSize  = 14.sp,
                                    color     = Color(0xFF9E9E9E),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        } else {
                            Text(
                                text       = "Movimientos",
                                fontSize   = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color      = Color(0xFF0D0D0D),
                                modifier   = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    items(uiState.transactions) { tx ->
                        TransactionRow(tx)
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionRow(tx: WalletTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(14.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (tx.isIncome) Color(0xFFE8F5E9) else Color(0xFFFFEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = if (tx.isIncome) Icons.Outlined.ArrowDownward else Icons.Outlined.ArrowUpward,
                    contentDescription = null,
                    tint               = if (tx.isIncome) GreenIn else RedOut,
                    modifier           = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    tx.title,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color(0xFF0D0D0D),
                    maxLines   = 1
                )
                Text(tx.date, fontSize = 12.sp, color = Color(0xFF9E9E9E))
            }
        }
        Text(
            text       = (if (tx.isIncome) "+" else "-") + "S/ ${"%.2f".format(tx.amount)}",
            fontSize   = 15.sp,
            fontWeight = FontWeight.Bold,
            color      = if (tx.isIncome) GreenIn else RedOut
        )
    }
}