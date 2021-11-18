package nl.hybrit.workshop.service.user

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.hybrit.workshop.extensions.info
import nl.hybrit.workshop.mp.api.common.user.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DefaultUserService(private val repo: UserRepository) : UserService {
    private val userUpdates = MutableSharedFlow<UserUpdateTO>()
    private val log: Logger by lazy { LoggerFactory.getLogger(this::class.java) }

    override suspend fun validateName(name: String): Boolean =
        repo.existsWithName(name).let { !it }

    override suspend fun create(user: UserRegistrationTO): UserTO =
        repo.save(user)
            .also { userUpdates.emit(UserUpdateTO(UserUpdateType.NEW, user = it)) }

    override fun deleteById(id: String) = runBlocking {
            repo.deleteById(id)
                ?.let { userUpdates.emit(UserUpdateTO(UserUpdateType.REMOVED, user = it)) }
                ?: log.info { "User already removed: $id" }
    }

    override suspend fun getById(id: String): UserTO? =
        repo.findUserById(id)

    override fun getAll(): Flow<UserUpdateTO> =
        repo.getAll()
            .map { UserUpdateTO(UserUpdateType.NONE, user = it) }

    override fun updates(): SharedFlow<UserUpdateTO> =
        userUpdates.asSharedFlow()
}
