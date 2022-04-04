class ChatNotFoundException(message: String) : RuntimeException(message)
class MessageNotFoundException(message: String) : RuntimeException(message)

class ChatService {
    private val chats = mutableListOf<Chat>()
    private var currentUserId = ""
    private var currentChatId = 0
    private var currentMessageId = 0

    fun login(userId: String) {
        currentUserId = userId
    }

    fun logoff() {
        currentUserId = ""
    }

    fun getCurrentUserId(): String {
        return currentUserId
    }

    fun getChatPresentation(chat: Chat): String {
        return "Chat id: ${chat.chatId}; with ${
            if (!chat.userId1.equals(currentUserId, true)) chat.userId1 else chat.userId2
        }; last: " + "${getLastMessage(chat) ?: "нет сообщений"}"
    }

    fun createChatWithUserId(toId: String): Chat {
        currentChatId++
        val chat = Chat(currentChatId, currentUserId, toId)
        chats.add(chat)
        return chats.last()
    }

    fun getChatPeerToPeer(peerId1: String, peerId2: String): Chat? {
        return chats
            .find {
                (it.userId1.equals(peerId1, true) && it.userId2.equals(peerId2, true))
                        || (it.userId1.equals(peerId2, true) && it.userId2.equals(peerId1, true))
            }
    }

    fun getChatsByUserId(userId: String): List<Chat> {
        return chats
            .filter { (it.userId1.equals(userId, true) || it.userId2.equals(userId, true)) }
    }

    fun getUnreadChatsCount(userId: String): Int {
        return chats
            .filter {
                (it.userId1.equals(userId, true) || it.userId2.equals(userId, true))
                        && (it.messages.filter { it.toId.equals(userId, true) && !it.isRead }.isNotEmpty())
            }
            .size
    }

    fun deleteChat(chatId: Int) {
        chats.removeIf { it.chatId == chatId }
    }

    fun sendMessage(toId: String, text: String) {
        val chat = getChatPeerToPeer(currentUserId, toId) ?: createChatWithUserId(toId)
        currentMessageId++
        val message = Message(currentMessageId, text, currentUserId, toId)
        chat.messages.add(message)
    }

    fun editMessage(chatId: Int, messageId: Int, text: String) {
        val chat =
            chats.find { it.chatId == chatId }
                ?: throw ChatNotFoundException("Chat with id = $chatId not found!")
        val message =
            chat.messages.find { it.messageId == messageId }
                ?: throw MessageNotFoundException("Message with id = $messageId not found!")
        message.text = text
    }

    fun getMessageByMessageId(chatId: Int, messageId: Int): Message {
        val chat =
            chats.find { it.chatId == chatId } ?: throw ChatNotFoundException("Chat with id = $chatId not found!")
        val message =
            chat.messages.find { it.messageId == messageId }
                ?: throw MessageNotFoundException("Message with id = $messageId not found!")
        return message
    }

    fun getLastMessage(chat: Chat): Message? {
        return chat.messages.lastOrNull()
    }

    fun getMessages(chatId: Int, messageId: Int, count: Int): List<Message> {
        val chat =
            chats.find { it.chatId == chatId } ?: throw ChatNotFoundException("Chat with id = $chatId not found!")
        val messages = chat.messages
            .takeLastWhile { messageId != it.messageId }
            .take(count)
        messages.filter { it.toId.equals(currentUserId, true) }.forEach { it.isRead = true }

        return messages
    }

    fun deleteMessage(chatId: Int, messageId: Int) {
        val chat = chats
            .find { it.chatId == chatId } ?: throw ChatNotFoundException("Chat with id = $chatId not found!")
        if (!chat.messages.removeIf { it.messageId == messageId })
            throw MessageNotFoundException("Message with id = $messageId not found!")

        chats.removeIf { it.messages.isEmpty() }
    }

}