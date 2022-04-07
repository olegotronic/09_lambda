data class Message(
    val messageId: Int = 0,
    val fromId: String = "",
    val toId: String = "",
    var text: String = "",
    var isRead: Boolean = false,
) {
    override fun toString(): String {
        return "Message id: $messageId; from: $fromId; to: $toId; text: $text"
    }
}