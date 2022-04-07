import org.junit.Test
import org.junit.Assert.*

class ChatServiceTest {

    @Test
    fun basicFunctions() {

        // arrange
        val chatService = ChatService()

        chatService.login("Ivan")

        chatService.sendMessage("Max", "Test to Max")
        chatService.sendMessage("Masha", "Test to Masha")

        // act
        val resultLogin = chatService.getCurrentUserId()
        chatService.logoff()
        val resultLogoff = chatService.getCurrentUserId()

        // assert
        assertEquals("Ivan", resultLogin)
        assertEquals("", resultLogoff)

    }

    @Test
    fun getUnreadChatsCount() {

        // arrange
        val chatService = ChatService()

        chatService.login("Ivan")

        chatService.sendMessage("Max", "Test to Max")
        chatService.sendMessage("Masha", "Test to Masha")

        // act
        val result0 = chatService.getUnreadChatsCount(chatService.getCurrentUserId())

        chatService.login("Max")
        val result1 = chatService.getUnreadChatsCount(chatService.getCurrentUserId())

        // assert
        assertEquals(0, result0)
        assertEquals(1, result1)

    }

    @Test
    fun getMessages() {

        // arrange
        val chatService = ChatService()

        chatService.login("Ivan")

        chatService.sendMessage("Max", "Test to Max")
        chatService.sendMessage("Max", "Another test to Max")
        chatService.sendMessage("Masha", "Test to Masha")
        chatService.sendMessage("Masha", "Another test to Masha")

        val chatId = 1
        val messageId = 1
        val count = 1

        // act
        val messages = chatService.getMessages(chatId, messageId, count)
        val result = messages[0].text

        // assert
        assertEquals("Another test to Max", result)

    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessage() {

        // arrange
        val chatService = ChatService()

        chatService.login("Ivan")

        chatService.sendMessage("Max", "Test to Max")
        chatService.sendMessage("Max", "Another test to Max")
        chatService.sendMessage("Masha", "Test to Masha")
        chatService.sendMessage("Masha", "Another test to Masha")

        val chatId = 1
        val messageId = 1

        // act
        val messageToDelete = chatService.getMessageByMessageId(chatId, messageId)
        val result = messageToDelete.text

        // assert
        assertEquals("Test to Max", result)

        // act
        chatService.deleteMessage(chatId, messageId)
        val messageAfterDelete = chatService.getMessageByMessageId(chatId, messageId)

    }

    @Test
    fun deleteChat() {

        // arrange
        val chatService = ChatService()

        chatService.login("Ivan")

        chatService.sendMessage("Max", "Test to Max")
        chatService.sendMessage("Max", "Another test to Max")
        chatService.sendMessage("Masha", "Test to Masha")
        chatService.sendMessage("Masha", "Another test to Masha")

        val chatId = 1
        val messageId = 1

        // act
        val result2 = chatService.getChatsByUserId(chatService.getCurrentUserId()).size
        chatService.deleteChat(chatId)
        val result1 = chatService.getChatsByUserId(chatService.getCurrentUserId()).size

        // assert
        assertEquals(2, result2)
        assertEquals(1, result1)

    }

    @Test
    fun editMessage() {

        // arrange
        val chatService = ChatService()

        chatService.login("Ivan")

        chatService.sendMessage("Max", "Test to Max")
        chatService.sendMessage("Masha", "Test to Masha")

        val chatId = 1
        val messageId = 1
        val text = "New text"

        // act
        chatService.editMessage(chatId, messageId, text)

        val message = chatService.getMessageByMessageId(chatId, messageId)
        val result = message.text

        // assert
        assertEquals("New text", result)

    }

    @Test(expected = ChatNotFoundException::class)
    fun editMessage_ChatNotFoundException() {

        val chatService = ChatService()

        chatService.login("Ivan")

        chatService.sendMessage("Max", "Test to Max")
        chatService.sendMessage("Masha", "Test to Masha")

        val chatId = 1 + 2
        val messageId = 1
        val text = "New text"

        // act
        chatService.editMessage(chatId, messageId, text)

    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessage_MessageNotFoundException() {

        val chatService = ChatService()

        chatService.login("Ivan")

        chatService.sendMessage("Max", "Test to Max")
        chatService.sendMessage("Masha", "Test to Masha")

        val chatId = 1
        val messageId = 1 + 2
        val text = "New text"

        // act
        chatService.editMessage(chatId, messageId, text)

    }

    @Test
    fun chatPresentation() {

        // arrange
        val chatService = ChatService()

        chatService.login("Ivan")

        chatService.sendMessage("Max", "Test to Max")
        chatService.sendMessage("Masha", "Test to Masha")

        val chat = chatService.getChatsByUserId(chatService.getCurrentUserId())[0]

        // act
        val chatPresentation = chatService.getChatPresentation(chat)

        // assert
        assertEquals(
            "Chat id: 1; with Max; last message: <Message id: 1; from: Ivan; to: Max; text: Test to Max>",
            chatPresentation
        )

    }

}
