package nl.hybrit.workshop.mp.api.common.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface UserService {

    suspend fun validateName(name: String): Boolean
    suspend fun create(user: UserRegistrationTO): UserTO
    fun deleteById(id: String)
    suspend fun getById(id: String): UserTO?
    fun getAll(): Flow<UserUpdateTO>
    fun updates(): SharedFlow<UserUpdateTO>

}
