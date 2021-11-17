package nl.hybrit.workshop.mp.api.common.message

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import nl.hybrit.workshop.mp.api.common.user.UserTO

@Serializable
data class MessageTO(
    val id: String,
    val fromUserId: String,
    val toUserId: String,
    val message: String,
    val receivedAt: Instant
)

@Serializable
data class NewMessageTO(
    val toUserId: String,
    val message: String
)

@Serializable
data class ConversationTO(
    val peer: UserTO,
    val messages: List<MessageTO>
)

fun NewMessageTO.toMessage(id: String, fromUser: UserTO): MessageTO =
    MessageTO(
        id = id,
        fromUserId = fromUser.id,
        toUserId = toUserId,
        message = message,
        receivedAt = Clock.System.now()
    )
