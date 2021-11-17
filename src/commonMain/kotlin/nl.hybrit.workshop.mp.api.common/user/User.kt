package nl.hybrit.workshop.mp.api.common.user

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserTO(
    val id: String,
    val name: String,
    val createdAt: Instant
)

@Serializable
data class UserRegistrationTO(
    val name: String
)

enum class UserUpdateType {
    NEW,
    REMOVED,
    NONE
}

@Serializable
data class UserUpdateTO(
    val type: UserUpdateType,
    val user: UserTO,
    val changedAt: Instant = Clock.System.now()
)

fun UserRegistrationTO.toUser(id: String): UserTO =
    UserTO(
        id = id,
        name = name,
        createdAt = Clock.System.now()
    )
