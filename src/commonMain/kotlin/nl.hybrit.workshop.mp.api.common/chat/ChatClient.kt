package nl.hybrit.workshop.mp.api.common.chat

import nl.hybrit.workshop.mp.api.common.message.ConversationTO
import nl.hybrit.workshop.mp.api.common.message.MessageTO
import nl.hybrit.workshop.mp.api.common.message.NewMessageTO
import nl.hybrit.workshop.mp.api.common.user.UserRegistrationTO
import nl.hybrit.workshop.mp.api.common.user.UserTO
import nl.hybrit.workshop.mp.api.common.user.UserUpdateTO

interface ChatClient {
    suspend fun register(registration: UserRegistrationTO): UserTO
    fun handleUserUpdate(handler: (UserUpdateTO) -> Unit)
    suspend fun getConversation(peerId: String): ConversationTO
    suspend fun send(message: NewMessageTO): MessageTO
    fun handleMessage(handler: (MessageTO) -> Unit)
    fun exit()
}
