package com.example.chambape.presentation.messages

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * Almacén de chats en memoria.
 *
 * Mantiene la lista de conversaciones y los mensajes de cada una usando estado
 * observable de Compose, por lo que cualquier pantalla que lo lea se recompone
 * automáticamente cuando se envía un mensaje o cambia el estado de "no leídos".
 *
 * Es un singleton (object): los datos viven mientras la app esté abierta. Para
 * persistencia real se reemplazaría por Room o un backend, pero esto deja toda
 * la funcionalidad operativa sin necesidad de servidor.
 */
object ChatStore {

    // Lista de previews que se muestra en la pantalla de Mensajes.
    val chats: SnapshotStateList<ChatPreview> = mutableStateListOf(
        ChatPreview("cafe_central",     "Café Central",     "¡Hola! ¿A qué hora llegas?",   "10:42 AM", unread = 1, emoji = "🏪"),
        ChatPreview("almacenes_global", "Almacenes Global", "Turno confirmado para mañana", "Ayer",     unread = 0, emoji = "📦"),
        ChatPreview("catering_luxury",  "Catering Luxury",  "¿Puedes quedarte una hora más?", "Lun",    unread = 2, emoji = "🍽️"),
        ChatPreview("fastexpress",      "FastExpress S.L.", "Gracias por tu trabajo hoy",   "Dom",      unread = 0, emoji = "🚚"),
    )

    // Conversación (lista de mensajes) por cada chatId.
    private val conversations = mutableStateMapOf<String, SnapshotStateList<ChatMessage>>()

    // Semillas iniciales por chat para que cada conversación sea distinta.
    private val seeds: Map<String, List<ChatMessage>> = mapOf(
        "cafe_central" to listOf(
            ChatMessage("c1", "¡Hola! ¿A qué hora llegas?", "10:42 AM", isFromMe = false),
            ChatMessage("c2", "Llego en 10 minutos.",        "10:45 AM", isFromMe = true, isRead = true),
        ),
        "almacenes_global" to listOf(
            ChatMessage("a1", "Hola, tu turno quedó confirmado para mañana 8:00 AM.", "Ayer", isFromMe = false),
            ChatMessage("a2", "Perfecto, ahí estaré. Gracias.",                       "Ayer", isFromMe = true, isRead = true),
        ),
        "catering_luxury" to listOf(
            ChatMessage("l1", "El evento se extendió un poco.",     "Lun", isFromMe = false),
            ChatMessage("l2", "¿Puedes quedarte una hora más?",     "Lun", isFromMe = false),
        ),
        "fastexpress" to listOf(
            ChatMessage("f1", "Entrega completada con éxito.",  "Dom", isFromMe = true, isRead = true),
            ChatMessage("f2", "Gracias por tu trabajo hoy 👍",  "Dom", isFromMe = false),
        ),
    )

    /** Devuelve (creando si hace falta) la lista observable de mensajes de un chat. */
    fun messagesFor(chatId: String): SnapshotStateList<ChatMessage> =
        conversations.getOrPut(chatId) {
            mutableStateListOf<ChatMessage>().apply {
                addAll(seeds[chatId] ?: emptyList())
            }
        }

    /** Nombre legible de un chat a partir de su id. */
    fun nameOf(chatId: String): String =
        chats.firstOrNull { it.id == chatId }?.name ?: chatId

    /** Envía un mensaje del usuario y actualiza el preview de la lista. */
    fun send(chatId: String, text: String, time: String = "Ahora") {
        val clean = text.trim()
        if (clean.isEmpty()) return

        val list = messagesFor(chatId)
        list.add(
            ChatMessage(
                id       = "m${list.size + 1}",
                text     = clean,
                time     = time,
                isFromMe = true,
                isRead   = false
            )
        )
        updatePreview(chatId, lastMessage = clean, time = time)
    }

    /** Marca un chat como leído (borra el contador de no leídos). */
    fun markAsRead(chatId: String) {
        val index = chats.indexOfFirst { it.id == chatId }
        if (index != -1 && chats[index].unread > 0) {
            chats[index] = chats[index].copy(unread = 0)
        }
    }

    private fun updatePreview(chatId: String, lastMessage: String, time: String) {
        val index = chats.indexOfFirst { it.id == chatId }
        if (index != -1) {
            chats[index] = chats[index].copy(lastMessage = lastMessage, time = time)
        }
    }
}
