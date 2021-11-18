package nl.hybrit.workshop.service.message

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import nl.hybrit.workshop.mp.api.common.message.MessageTO
import nl.hybrit.workshop.mp.api.common.randomDelay
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class MessageRepository {
    private val messages = ConcurrentHashMap<String, MessageTO>()

    private fun messageFilter(userId: String, peerId: String): (MessageTO) -> Boolean {
        val ids = listOf(userId, peerId)
        return { message -> ids.contains(message.fromUserId) || ids.contains(message.toUserId) }
    }

    suspend fun save(message: MessageTO): MessageTO {
        randomDelay()
        return message
            .also { messages[it.id] = it }
    }

    fun getAll(): Flow<MessageTO> = messages.values.asFlow()

    fun getMessages(userId: String, peerId: String): Flow<MessageTO> =
        messages.values
            .filter(messageFilter(userId, peerId))
            .sortedBy { it.receivedAt }
            .asFlow()

}
