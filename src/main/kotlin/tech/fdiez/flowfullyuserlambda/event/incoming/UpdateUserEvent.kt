package tech.fdiez.flowfullyuserlambda.event.incoming

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class UpdateUserEvent(
    val username: String,
    val todoistApiToken: String?,
    val todoistWebhookUrl: String?,
    val todoistUserId: String?,
) {

    companion object {
        fun from(payload: String): UpdateUserEvent {
            return jacksonObjectMapper().readValue(payload, UpdateUserEvent::class.java)
        }
    }
}