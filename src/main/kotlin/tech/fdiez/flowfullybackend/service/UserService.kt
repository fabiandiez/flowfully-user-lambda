package tech.fdiez.flowfullybackend.service

import org.springframework.stereotype.Service
import mu.KotlinLogging
import tech.fdiez.flowfullybackend.exception.UsernameAlreadyExists
import tech.fdiez.flowfullybackend.exception.UsernameNotFoundException
import tech.fdiez.flowfullybackend.data.UserData
import tech.fdiez.flowfullybackend.event.incoming.CreateUserEvent
import tech.fdiez.flowfullybackend.event.incoming.UpdateUserEvent
import tech.fdiez.flowfullybackend.event.outgoing.UserDataEvent
import tech.fdiez.flowfullybackend.repository.UserRepository

@Service
class UserService(private val userRepository: UserRepository) {

    private val logger = KotlinLogging.logger {}

    private fun getUserData(username: String) =
        userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)

    fun getUser(username: String) = UserDataEvent.from(getUserData(username))

    fun updateUser(event: UpdateUserEvent): UserDataEvent {
        val user = getUserData(event.username)
        logger.info { "Updating user ${event.username}" }
        user.todoistUserId = event.todoistUserId ?: user.todoistUserId
        user.todoistApiToken = event.todoistApiToken ?: user.todoistApiToken
        user.todoistWebhookUrl = event.todoistWebhookUrl ?: user.todoistWebhookUrl
        val updatedUser = userRepository.save(user)
        return UserDataEvent.from(updatedUser)
    }

    fun createUser(event: CreateUserEvent): UserDataEvent {
        if (!userRepository.existsByUsername(event.username)) {
            logger.info { "Creating user ${event.username}" }
            val createdUser = userRepository.save(
                UserData(
                    username = event.username,
                    password = event.password,
                    todoistUserId = event.todoistUserId,
                    todoistApiToken = event.todoistApiToken,
                    todoistWebhookUrl = event.todoistWebhookUrl
                )
            )
            return UserDataEvent.from(createdUser)
        } else {
            throw UsernameAlreadyExists(event.username)
        }
    }
}