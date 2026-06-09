package com.example.chambape.presentation.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chambape.domain.model.Notification

private val ChambaBlue     = Color(0xFF1A3FD8)
private val BackgroundGray = Color(0xFFF7F8FC)
private val TextPrimary    = Color(0xFF0D0D0D)
private val TextSecondary  = Color(0xFF6B6B6B)

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val viewModel: NotificationsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.load() }

    Surface(modifier = Modifier.fillMaxSize(), color = BackgroundGray) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier          = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Volver", tint = TextPrimary)
                }
                Text(
                    text       = "Notificaciones",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                    modifier   = Modifier.weight(1f)
                )
                val hasUnread = uiState.notifications.any { !it.read }
                if (hasUnread) {
                    TextButton(onClick = { viewModel.markAllAsRead() }) {
                        Text("Marcar todas", fontSize = 13.sp, color = ChambaBlue)
                    }
                }
            }

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = ChambaBlue)
                            Spacer(Modifier.height(16.dp))
                            Text("Cargando notificaciones...", fontSize = 14.sp, color = TextSecondary)
                        }
                    }
                }

                uiState.errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text      = uiState.errorMessage!!,
                            fontSize  = 15.sp,
                            color     = Color(0xFFD93025),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                uiState.notifications.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Outlined.Notifications,
                                null,
                                tint     = Color(0xFFCCCCCC),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text("No tienes notificaciones.", fontSize = 15.sp, color = TextSecondary)
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier       = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.notifications) { notification ->
                            NotificationRow(
                                notification = notification,
                                onClick      = {
                                    if (!notification.read) viewModel.markAsRead(notification.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(notification: Notification, onClick: () -> Unit) {
    val (icon, iconBg) = notificationStyle(notification.type)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (notification.read) Color.White else Color(0xFFF0F4FF))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier         = Modifier.size(44.dp).clip(CircleShape).background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = ChambaBlue, modifier = Modifier.size(22.dp))
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text       = notification.title,
                    fontSize   = 14.sp,
                    fontWeight = if (notification.read) FontWeight.Normal else FontWeight.SemiBold,
                    color      = TextPrimary,
                    modifier   = Modifier.weight(1f)
                )
                if (!notification.read) {
                    Box(
                        modifier = Modifier.size(8.dp).clip(CircleShape).background(ChambaBlue)
                    )
                }
            }
            Spacer(Modifier.height(3.dp))
            Text(
                text     = notification.message,
                fontSize = 13.sp,
                color    = TextSecondary,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text     = formatNotificationDate(notification.createdAt),
                fontSize = 11.sp,
                color    = Color(0xFFAAAAAA)
            )
        }
    }

    Box(modifier = Modifier.fillMaxWidth().height(1.dp).padding(horizontal = 74.dp).background(Color(0xFFF0F0F0)))
}

private fun notificationStyle(type: String): Pair<ImageVector, Color> = when (type) {
    "JOB_MATCH"       -> Pair(Icons.Outlined.Work,          Color(0xFFE8EDFF))
    "SHIFT_REMINDER"  -> Pair(Icons.Outlined.Schedule,      Color(0xFFE8F5E9))
    "PAYMENT"         -> Pair(Icons.Outlined.CreditCard,    Color(0xFFFFF3CD))
    else              -> Pair(Icons.Outlined.Info,           Color(0xFFF0F0F0))
}

private fun formatNotificationDate(isoDate: String): String {
    return if (isoDate.length >= 16) isoDate.substring(0, 10) else isoDate
}
