package nl.hybrit.workshop.mp.api.common.chat

import nl.hybrit.workshop.mp.api.common.message.ConversationTO
import nl.hybrit.workshop.mp.api.common.message.MessageService
import nl.hybrit.workshop.mp.api.common.message.MessageTO
import nl.hybrit.workshop.mp.api.common.message.NewMessageTO
import nl.hybrit.workshop.mp.api.common.user.UserRegistrationTO
import nl.hybrit.workshop.mp.api.common.user.UserService
import nl.hybrit.workshop.mp.api.common.user.UserTO

interface ChatServiceHandler {
    val userService: UserService
    val messageService: MessageService

    suspend fun register(user: UserRegistrationTO): UserTO
    suspend fun getConversation(peerId: String): ConversationTO
    suspend fun receive(message: NewMessageTO): MessageTO
    fun logout(userId: String)
}
