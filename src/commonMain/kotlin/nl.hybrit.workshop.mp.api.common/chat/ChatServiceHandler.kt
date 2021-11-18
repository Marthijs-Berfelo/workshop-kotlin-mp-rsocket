package nl.hybrit.workshop.mp.api.common.chat

import kotlinx.coroutines.flow.toList
import nl.hybrit.workshop.mp.api.common.message.*
import nl.hybrit.workshop.mp.api.common.user.UserRegistrationTO
import nl.hybrit.workshop.mp.api.common.user.UserService
import nl.hybrit.workshop.mp.api.common.user.UserTO

interface ChatServiceHandler {
    val userService: UserService
    val messageService: MessageService

    suspend fun register(user: UserRegistrationTO): UserTO =
        userService.create(user)

    suspend fun getConversation(userId: String, peerId: String): ConversationTO? =
        userService.getById(peerId)
            ?.let {
                it to messageService.conversation(userId, peerId).toList()
            }
            ?.toConversation()

    suspend fun receive(message: NewMessageTO, senderId: String): MessageTO =
        messageService.create(message, senderId)

    fun logout(userId: String) = userService.deleteById(userId)
}
