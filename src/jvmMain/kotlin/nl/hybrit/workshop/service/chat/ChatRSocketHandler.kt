package nl.hybrit.workshop.service.chat

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import nl.hybrit.workshop.mp.api.common.chat.ChatServiceHandler
import nl.hybrit.workshop.mp.api.common.message.ConversationTO
import nl.hybrit.workshop.mp.api.common.message.MessageService
import nl.hybrit.workshop.mp.api.common.message.MessageTO
import nl.hybrit.workshop.mp.api.common.message.NewMessageTO
import nl.hybrit.workshop.mp.api.common.user.UserRegistrationTO
import nl.hybrit.workshop.mp.api.common.user.UserService
import nl.hybrit.workshop.mp.api.common.user.UserTO
import nl.hybrit.workshop.mp.api.common.user.UserUpdateTO
import org.springframework.http.HttpStatus
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Controller
import org.springframework.web.server.ResponseStatusException
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct

@Controller
class ChatRSocketHandler(
    override val userService: UserService,
    override val messageService: MessageService
) : ChatServiceHandler {
    private val clients = ConcurrentHashMap<String, RSocketRequester>()
    private val userStreams = ConcurrentHashMap<RSocketRequester, MutableSharedFlow<UserUpdateTO>>()
    private val messageStreams = ConcurrentHashMap<RSocketRequester, MutableSharedFlow<MessageTO>>()

    @PostConstruct
    fun init() {
        userService.updates()
            .onEach { update -> userStreams.forEach { it.value.emit(update) } }
            .launchIn(MainScope())
    }

    @MessageMapping("register")
    suspend fun register(requester: RSocketRequester, @Payload registration: UserRegistrationTO): UserTO =
        register(registration)
            .also { clients[it.id] = requester }
            .also { userStreams[requester] = MutableSharedFlow() }
            .also {
                val stream = MutableSharedFlow<MessageTO>()
                messageService.updates(it.id)
                    .onEach { update -> stream.emit(update) }
                    .launchIn(MainScope())
                messageStreams[requester] = stream
            }
            .also { user ->
                requester.rsocket()!!.onClose().subscribe {
                    clients.remove(user.id)
                    userStreams.remove(requester)
                    messageStreams.remove(requester)
                }
            }

    @MessageMapping("users")
    suspend fun streamUsers(requester: RSocketRequester): Flow<UserUpdateTO> =
        userService.getAll()
            .onCompletion { emitAll(userStreams[requester]!!) }

    @MessageMapping("conversation.{peerId}")
    suspend fun getConversation(
        requester: RSocketRequester,
        @DestinationVariable("peerId") peerId: String
    ): ConversationTO =
        getConversation(userId = findUserId(requester), peerId = peerId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer user not found: $peerId")

    @MessageMapping("messages.new")
    suspend fun onMessage(requester: RSocketRequester, @Payload message: NewMessageTO): MessageTO =
        receive(message = message, senderId = findUserId(requester))

    @MessageMapping("messages")
    fun streamMessages(requester: RSocketRequester): Flow<MessageTO> =
        messageStreams[requester]!!

    private fun findUserId(requester: RSocketRequester): String =
        clients.entries
            .firstOrNull { it.value == requester }
            ?.key
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Requester not registered")
}
