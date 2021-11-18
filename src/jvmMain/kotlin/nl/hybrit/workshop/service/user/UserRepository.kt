package nl.hybrit.workshop.service.user

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.firstOrNull
import nl.hybrit.workshop.mp.api.common.randomDelay
import nl.hybrit.workshop.mp.api.common.user.UserRegistrationTO
import nl.hybrit.workshop.mp.api.common.user.UserTO
import nl.hybrit.workshop.mp.api.common.user.toUser
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Repository
class UserRepository {
    private val users = ConcurrentHashMap<String, UserTO>()

    suspend fun save(user: UserRegistrationTO): UserTO {
        randomDelay()
        return user.toUser(uuid4().toString())
            .also { users[it.id] = it }
    }

    fun getAll(): Flow<UserTO> =
        users.values.asFlow()

    suspend fun findUserById(id: String): UserTO? {
        randomDelay()
        return users[id]
    }

    suspend fun deleteById(id: String): UserTO? {
        randomDelay()
        return users.remove(id)
    }

    suspend fun existsWithName(name: String): Boolean =
        users.values.asFlow()
            .firstOrNull { it.name == name }
            ?.let { true }
            ?: false

}
