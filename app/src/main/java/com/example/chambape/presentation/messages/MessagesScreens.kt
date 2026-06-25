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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chambape.domain.model.Message

private val ChambaBlue     = Color(0xFF1A3FD8)
private val BackgroundGray = Color(0xFFF7F8FC)
private val BubbleIncoming = Color(0xFFE9EAEF)
private val BubbleOutgoing = ChambaBlue

@Composable
fun MessagesScreen(
    onChatClick: (conversationId: String, jobId: String) -> Unit
) {
    val viewModel: MessagesViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.load() }

    Surface(modifier = Modifier.fillMaxSize(), color = BackgroundGray) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier              = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mensajes", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0D0D0D))
                Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = Color(0xFF9E9E9E))
            }

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = ChambaBlue)
                            Spacer(Modifier.height(16.dp))
                            Text("Cargando mensajes...", fontSize = 14.sp, color = Color(0xFF6B6B6B))
                        }
                    }
                }

                uiState.errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text      = uiState.errorMessage ?: "Error al cargar mensajes.",
                            fontSize  = 15.sp,
                            color     = Color(0xFFD93025),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                uiState.conversations.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text      = "No tienes conversaciones aún.",
                            fontSize  = 15.sp,
                            color     = Color(0xFF6B6B6B),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 16.dp)) {
                        items(uiState.conversations) { conv ->
                            ConversationRow(
                                item    = conv,
                                onClick = { onChatClick(conv.conversationId, conv.jobId) }
                            )
                            Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 72.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationRow(item: ConversationItem, onClick: () -> Unit) {
    Row(
        modifier          = Modifier.fillMaxWidth().clickable { onClick() }.background(Color.White).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier         = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFFE8EEFF)),
            contentAlignment = Alignment.Center
        ) {
            Text("🏪", fontSize = 22.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(item.jobTitle, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0D0D0D))
                Text(
                    text     = if (item.createdAt.length >= 10) item.createdAt.substring(0, 10) else item.createdAt,
                    fontSize = 12.sp,
                    color    = Color(0xFF9E9E9E)
                )
            }
            Spacer(Modifier.height(3.dp))
            Text("Toca para ver mensajes", fontSize = 13.sp, color = Color(0xFF6B6B6B), maxLines = 1)
        }
    }
}

@Composable
fun ChatScreen(
    conversationId: String,
    jobId: String,
    onBack: () -> Unit
) {
    val viewModel: ChatViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(conversationId, jobId) { viewModel.load(conversationId, jobId) }

    // Detiene el polling cuando el usuario sale de la pantalla
    DisposableEffect(conversationId) {
        onDispose { viewModel.stopPolling() }
    }

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) listState.animateScrollToItem(uiState.messages.size - 1)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = BackgroundGray) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier              = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, "Volver", tint = ChambaBlue)
                }
                Text(
                    text       = if (uiState.jobTitle.isNotBlank()) uiState.jobTitle else "Chat",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = ChambaBlue
                )
                IconButton(onClick = {}) {
                    Icon(Icons.Outlined.MoreVert, "Más opciones", tint = Color(0xFF9E9E9E))
                }
            }

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ChambaBlue)
                    }
                }

                else -> {
                    LazyColumn(
                        modifier            = Modifier.weight(1f).fillMaxWidth(),
                        state               = listState,
                        contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.messages) { msg ->
                            MessageBubble(message = msg, isFromMe = msg.senderId == viewModel.currentUserId)
                        }
                    }
                }
            }

            if (uiState.sendError != null) {
                Text(
                    text     = uiState.sendError!!,
                    fontSize = 12.sp,
                    color    = Color(0xFFD93025),
                    modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .navigationBarsPadding()
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier         = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFF0F0F0)).clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Add, "Adjuntar", tint = Color(0xFF6B6B6B), modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value         = inputText,
                    onValueChange = { inputText = it },
                    modifier      = Modifier.weight(1f),
                    placeholder   = { Text("Escribe un mensaje...", color = Color(0xFFAAAAAA), fontSize = 14.sp) },
                    singleLine    = true,
                    shape         = RoundedCornerShape(24.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor   = Color(0xFFF5F5F5),
                        unfocusedBorderColor    = Color.Transparent,
                        focusedBorderColor      = ChambaBlue,
                        focusedTextColor        = Color(0xFF0D0D0D),
                        unfocusedTextColor      = Color(0xFF0D0D0D)
                    )
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (inputText.isNotBlank()) ChambaBlue else Color(0xFFCCCCCC))
                        .clickable {
                            val text = inputText.trim()
                            if (text.isNotBlank()) {
                                inputText = ""
                                viewModel.send(conversationId, text)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Send, "Enviar", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message, isFromMe: Boolean) {
    Column(
        modifier            = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
    ) {
        if (!isFromMe) {
            Row(verticalAlignment = Alignment.Bottom) {
                Box(
                    modifier         = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE8EEFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Store, null, tint = ChambaBlue, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .widthIn(max = 260.dp)
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomEnd = 18.dp, bottomStart = 4.dp))
                        .background(BubbleIncoming)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(message.content, fontSize = 14.sp, color = Color(0xFF0D0D0D))
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomEnd = 4.dp, bottomStart = 18.dp))
                    .background(BubbleOutgoing)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(message.content, fontSize = 14.sp, color = Color.White)
            }
        }

        Spacer(Modifier.height(2.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier          = if (!isFromMe) Modifier.padding(start = 40.dp) else Modifier
        ) {
            Text(
                text     = if (message.sentAt.length >= 16) message.sentAt.substring(11, 16) else message.sentAt,
                fontSize = 11.sp,
                color    = Color(0xFF9E9E9E)
            )
            if (isFromMe) {
                Spacer(Modifier.width(3.dp))
                Icon(
                    imageVector        = if (message.read) Icons.Outlined.DoneAll else Icons.Outlined.Done,
                    contentDescription = null,
                    tint               = if (message.read) ChambaBlue else Color(0xFF9E9E9E),
                    modifier           = Modifier.size(14.dp)
                )
            }
        }
    }
}