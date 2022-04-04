data class Message(
    var messageId: Int = 0,
    var text: String = "",
    val fromId: String = "",
    val toId: String = "",
    var isRead: Boolean = false,
) {
    override fun toString(): String {
        return "Message id: $messageId; from: $fromId; to: $toId; text: $text"
    }
}