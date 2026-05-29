package com.example.chambape.presentation.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChambaBlue    = Color(0xFF1A3FD8)
private val BackgroundGray = Color(0xFFF7F8FC)
private val GreenIn        = Color(0xFF2E7D32)
private val RedOut         = Color(0xFFE53935)

private data class Transaction(
    val title: String,
    val date: String,
    val amount: Double,
    val isIncome: Boolean
)

private val sampleTransactions = listOf(
    Transaction("Pago — Asistente de Logística", "15 Oct, 2023", 1200.00, true),
    Transaction("Retiro a cuenta bancaria",       "13 Oct, 2023",  800.00, false),
    Transaction("Pago — Mesero de Eventos",       "12 Oct, 2023",  950.00, true),
    Transaction("Pago — Repartidor",              "10 Oct, 2023",  780.00, true),
)

@Composable
fun WalletScreen(
    onBack: () -> Unit = {}
) {
    var showWithdraw by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = BackgroundGray
    ) {
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
                            text  = "Saldo disponible",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text       = "$2,330.00",
                            fontSize   = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = Color.White
                        )
                        Spacer(Modifier.height(18.dp))
                        Button(
                            onClick = { showWithdraw = true },
                            shape   = RoundedCornerShape(12.dp),
                            colors  = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text("Retirar dinero", color = ChambaBlue, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    text       = "Movimientos",
                    fontSize   = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF0D0D0D),
                    modifier   = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))
            }

            items(sampleTransactions) { tx ->
                TransactionRow(tx)
            }
        }
    }

    if (showWithdraw) {
        AlertDialog(
            onDismissRequest = { showWithdraw = false },
            title = { Text("Retirar dinero") },
            text  = { Text("Se enviará tu saldo disponible a tu cuenta bancaria registrada en 1-2 días hábiles.") },
            confirmButton = {
                TextButton(onClick = { showWithdraw = false }) {
                    Text("Confirmar", color = ChambaBlue, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdraw = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun TransactionRow(tx: Transaction) {
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
                Text(tx.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0D0D0D), maxLines = 1)
                Text(tx.date, fontSize = 12.sp, color = Color(0xFF9E9E9E))
            }
        }
        Text(
            text       = (if (tx.isIncome) "+" else "-") + "$%,.2f".format(tx.amount),
            fontSize   = 15.sp,
            fontWeight = FontWeight.Bold,
            color      = if (tx.isIncome) GreenIn else RedOut
        )
    }
}


