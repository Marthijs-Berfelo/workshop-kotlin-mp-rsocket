package nl.hybrit.workshop.service.message

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.flow.*
import nl.hybrit.workshop.mp.api.common.message.MessageService
import nl.hybrit.workshop.mp.api.common.message.MessageTO
import nl.hybrit.workshop.mp.api.common.message.NewMessageTO
import nl.hybrit.workshop.mp.api.common.message.toMessage
import nl.hybrit.workshop.mp.api.common.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class DefaultMessageService(
    private val repo: MessageRepository,
    private val users: UserService
) : MessageService {
    private val messageUpdates = MutableSharedFlow<MessageTO>()

    override suspend fun create(message: NewMessageTO, fromUserId: String): MessageTO =
        users.getById(fromUserId)
            ?.let { message.toMessage(id = uuid4().toString(), fromUser = it) }
            ?.let { repo.save(it) }
            ?.also { messageUpdates.emit(it) }
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not registered with id: $fromUserId")

    override fun conversation(userId: String, peerId: String): Flow<MessageTO> =
        repo.getMessages(userId = userId, peerId = peerId)

    override fun updates(userId: String): Flow<MessageTO> =
        messageUpdates
            .asSharedFlow()
            .filter { it.toUserId == userId }
}
