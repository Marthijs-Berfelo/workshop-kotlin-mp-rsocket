package nl.hybrit.workshop.mp.api.common.message

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface MessageService {

    suspend fun create(message: NewMessageTO): MessageTO
    fun conversation(userId: String, peerId: String): Flow<MessageTO>
    fun updates(userId: String): SharedFlow<MessageTO>
}
