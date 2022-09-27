package tech.fdiez.flowfullybackend.service

import org.springframework.stereotype.Service
import mu.KotlinLogging
import tech.fdiez.flowfullybackend.data.UserData
import tech.fdiez.flowfullybackend.event.incoming.CreateUserEvent
import tech.fdiez.flowfullybackend.event.incoming.UpdateUserEvent
import tech.fdiez.flowfullybackend.repository.UserRepository

@Service
class UserService(private val userRepository: UserRepository) {

    private val logger = KotlinLogging.logger {}

    fun getUser(username: String): UserData? {
        return userRepository.findByUsername(username)
    }

    fun updateUser(event: UpdateUserEvent) {
        val user = getUser(event.username)

        if (user != null) {
            logger.info { "Updating user ${event.username}" }
            user.todoistUserId = event.todoistUserId ?: user.todoistUserId
            user.todoistApiToken = event.todoistApiToken ?: user.todoistApiToken
            user.todoistWebhookUrl = event.todoistWebhookUrl ?: user.todoistWebhookUrl
            userRepository.save(user)
        } else {
            logger.error { "User ${event.username} not found" }
        }
    }

    fun createUser(event: CreateUserEvent) {
        val user = getUser(event.username)

        if (user == null) {
            logger.info { "Creating user ${event.username}" }
            userRepository.save(
                UserData(
                    username = event.username,
                    password = event.password,
                    todoistUserId = event.todoistUserId,
                    todoistApiToken = event.todoistApiToken,
                    todoistWebhookUrl = event.todoistWebhookUrl
                )
            )
        } else {
            logger.error { "User ${event.username} already exists" }
        }
    }
}