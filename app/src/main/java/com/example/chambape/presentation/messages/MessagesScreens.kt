package com.example.chambape.presentation.messages

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Colors ───────────────────────────────────────────────────────────────────
private val ChambaBlue     = Color(0xFF1A3FD8)
private val BackgroundGray = Color(0xFFF7F8FC)
private val BubbleIncoming = Color(0xFFE9EAEF)
private val BubbleOutgoing = ChambaBlue

// ─── Domain models ────────────────────────────────────────────────────────────
data class ChatPreview(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unread: Int = 0,
    val emoji: String = "🏪"
)

data class ChatMessage(
    val id: String,
    val text: String,
    val time: String,
    val isFromMe: Boolean,
    val isRead: Boolean = true
)

private val sampleChats = listOf(
    ChatPreview("cafe_central",    "Café Central",       "¡Hola! ¿A qué hora llegas?", "10:42 AM", unread = 1, emoji = "🏪"),
    ChatPreview("almacenes_global","Almacenes Global",   "Turno confirmado para mañana","Ayer",     unread = 0, emoji = "📦"),
    ChatPreview("catering_luxury", "Catering Luxury",    "¿Puedes quedarte una hora más?","Lun",    unread = 2, emoji = "🍽️"),
    ChatPreview("fastexpress",     "FastExpress S.L.",   "Gracias por tu trabajo hoy",  "Dom",     unread = 0, emoji = "🚚"),
)

private val sampleMessages = mutableListOf(
    ChatMessage("1", "¡Hola! ¿A qué hora llegas?", "10:42 AM", isFromMe = false),
    ChatMessage("2", "Llego en 10 minutos.",         "10:45 AM", isFromMe = true,  isRead = true),
)

// ─── Messages list screen ─────────────────────────────────────────────────────
@Composable
fun MessagesScreen(
    onChatClick: (chatId: String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = BackgroundGray
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text       = "Mensajes",
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = Color(0xFF0D0D0D)
                )
                Icon(
                    imageVector        = Icons.Outlined.MoreVert,
                    contentDescription = null,
                    tint               = Color(0xFF9E9E9E)
                )
            }

            // Chat list
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(sampleChats) { chat ->
                    ChatPreviewRow(chat = chat, onClick = { onChatClick(chat.id) })
                    Divider(
                        color    = Color(0xFFF0F0F0),
                        modifier = Modifier.padding(horizontal = 72.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatPreviewRow(chat: ChatPreview, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8EEFF)),
            contentAlignment = Alignment.Center
        ) {
            Text(chat.emoji, fontSize = 22.sp)
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = chat.name,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color(0xFF0D0D0D)
                )
                Text(
                    text     = chat.time,
                    fontSize = 12.sp,
                    color    = if (chat.unread > 0) ChambaBlue else Color(0xFF9E9E9E)
                )
            }
            Spacer(Modifier.height(3.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text     = chat.lastMessage,
                    fontSize = 13.sp,
                    color    = Color(0xFF6B6B6B),
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                if (chat.unread > 0) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(ChambaBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text     = chat.unread.toString(),
                            fontSize = 11.sp,
                            color    = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ─── Chat screen ──────────────────────────────────────────────────────────────
@Composable
fun ChatScreen(
    chatId: String,
    onBack: () -> Unit
) {
    val chatName = sampleChats.firstOrNull { it.id == chatId }?.name ?: chatId
    val messages  = remember { mutableStateListOf(*sampleMessages.toTypedArray()) }
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = BackgroundGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // ── Top bar ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector        = Icons.Outlined.ArrowBack,
                        contentDescription = "Volver",
                        tint               = ChambaBlue
                    )
                }
                Text(
                    text       = "Chat con $chatName",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = ChambaBlue
                )
                IconButton(onClick = {}) {
                    Icon(
                        imageVector        = Icons.Outlined.MoreVert,
                        contentDescription = "Más opciones",
                        tint               = Color(0xFF9E9E9E)
                    )
                }
            }

            // ── Message list ─────────────────────────────────────────────────
            LazyColumn(
                modifier       = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state          = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Date separator
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text     = "Hoy, 10:42 AM",
                            fontSize = 12.sp,
                            color    = Color(0xFF9E9E9E)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }

                items(messages) { msg ->
                    MessageBubble(message = msg)
                }
            }

            // ── Input bar ────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // + button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F0F0))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Outlined.Add,
                        contentDescription = "Adjuntar",
                        tint               = Color(0xFF6B6B6B),
                        modifier           = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Text field
                OutlinedTextField(
                    value         = inputText,
                    onValueChange = { inputText = it },
                    modifier      = Modifier.weight(1f),
                    placeholder   = {
                        Text("Escribe un mensaje...", color = Color(0xFFAAAAAA), fontSize = 14.sp)
                    },
                    singleLine = true,
                    shape      = RoundedCornerShape(24.dp),
                    colors     = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor   = Color(0xFFF5F5F5),
                        unfocusedBorderColor    = Color.Transparent,
                        focusedBorderColor      = ChambaBlue
                    )
                )

                Spacer(Modifier.width(8.dp))

                // Send button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ChambaBlue)
                        .clickable {
                            if (inputText.isNotBlank()) {
                                messages.add(
                                    ChatMessage(
                                        id       = (messages.size + 1).toString(),
                                        text     = inputText.trim(),
                                        time     = "Ahora",
                                        isFromMe = true,
                                        isRead   = false
                                    )
                                )
                                inputText = ""
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Outlined.Send,
                        contentDescription = "Enviar",
                        tint               = Color.White,
                        modifier           = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ─── Message bubble ───────────────────────────────────────────────────────────
@Composable
private fun MessageBubble(message: ChatMessage) {
    Column(
        modifier            = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start
    ) {
        if (!message.isFromMe) {
            // Sender avatar for incoming
            Row(verticalAlignment = Alignment.Bottom) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8EEFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Outlined.Store,
                        contentDescription = null,
                        tint               = ChambaBlue,
                        modifier           = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .widthIn(max = 260.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart    = 18.dp,
                                topEnd      = 18.dp,
                                bottomEnd   = 18.dp,
                                bottomStart = 4.dp
                            )
                        )
                        .background(BubbleIncoming)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text     = message.text,
                        fontSize = 14.sp,
                        color    = Color(0xFF0D0D0D)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart    = 18.dp,
                            topEnd      = 18.dp,
                            bottomEnd   = 4.dp,
                            bottomStart = 18.dp
                        )
                    )
                    .background(BubbleOutgoing)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text     = message.text,
                    fontSize = 14.sp,
                    color    = Color.White
                )
            }
        }

        // Timestamp + read receipts
        Spacer(Modifier.height(2.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = if (!message.isFromMe) Modifier.padding(start = 40.dp) else Modifier
        ) {
            Text(
                text     = message.time,
                fontSize = 11.sp,
                color    = Color(0xFF9E9E9E)
            )
            if (message.isFromMe) {
                Spacer(Modifier.width(3.dp))
                Icon(
                    imageVector        = if (message.isRead) Icons.Outlined.DoneAll else Icons.Outlined.Done,
                    contentDescription = null,
                    tint               = if (message.isRead) ChambaBlue else Color(0xFF9E9E9E),
                    modifier           = Modifier.size(14.dp)
                )
            }
        }
    }
}
