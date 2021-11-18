package nl.hybrit.workshop.mp.api.common.message

import kotlinx.coroutines.flow.Flow

interface MessageService {

    suspend fun create(message: NewMessageTO, fromUserId: String): MessageTO
    fun conversation(userId: String, peerId: String): Flow<MessageTO>
    fun updates(userId: String): Flow<MessageTO>
}
