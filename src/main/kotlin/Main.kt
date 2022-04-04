fun readString(title: String): String {
    print(title)
    return readln()
}

fun readInt(title: String): Int? {
    print(title)
    return readln().toIntOrNull()
}

fun main() {

    val chatService = ChatService()

    chatService.login("Ivan")

    chatService.sendMessage("Max", "Test to Max")
    chatService.sendMessage("Masha", "Test to Masha")

    val listOfCommands = "Available commands:\n" +
            "\"SEND MESSAGE\", \"EDIT MESSAGE\", \"DELETE MESSAGE\", \"VIEW CHAT\", \"DELETE CHAT\", \"LOGIN\", \"LOGOFF\", \"QUIT\""

    println("\nCHAT SERVICE 1.0")
    println(listOfCommands)

    while (true) {

        if (chatService.getCurrentUserId() == "") {
            chatService.login(readString("Enter your user name: "))
            continue
        }

        println(
            "\nChats for ${chatService.getCurrentUserId()} " +
                    "(unread: ${chatService.getUnreadChatsCount(chatService.getCurrentUserId())}):"
        )

        chatService.getChatsByUserId(chatService.getCurrentUserId())
            .forEach { println(chatService.getChatPresentation(it)) }

        when (readln().uppercase()) {
            "SEND MESSAGE" -> {
                val toId = readString("Recipient id: ")
                val text = readString("Text: ")
                chatService.sendMessage(toId, text)
            }
            "EDIT MESSAGE" -> {
                val chatId = readInt("Chat id: ") ?: 0
                val messageId = readInt("Message id: ") ?: 0
                val text = readString("New text: ")
                chatService.editMessage(chatId, messageId, text)
            }
            "DELETE MESSAGE" -> {
                val chatId = readInt("Chat id: ") ?: 0
                val messageId = readInt("Message id: ") ?: 0
                chatService.deleteMessage(chatId, messageId)
            }
            "VIEW CHAT" -> {
                val chatId = readInt("Chat id: ") ?: 0
                val messageId = readInt("Last message id: ") ?: 0
                val count = readInt("Messages count: ") ?: 0
                println("List of messages for chat id $chatId:")
                chatService.getMessages(chatId, messageId, count).forEach { println(it) }
            }
            "DELETE CHAT" -> {
                val chatId = readInt("Chat id: ") ?: 0
                chatService.deleteChat(chatId)
            }
            "LOGIN" -> chatService.login(readString("Enter your user id: "))
            "LOGOFF" -> chatService.logoff()
            "QUIT", "Q" -> break
            else -> print("Incorrect command!")
        }

        println(listOfCommands)

    }
}

