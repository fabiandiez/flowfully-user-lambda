package tech.fdiez.flowfullybackend.event.incoming

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javax.validation.constraints.NotBlank

data class CreateUserEvent(
    @get:NotBlank val username: String,
    @get:NotBlank val password: String,
    @get:NotBlank val todoistApiToken: String,
    @get:NotBlank val todoistWebhookUrl: String,
    @get:NotBlank val todoistUserId: String,
) {
    companion object {
        fun from(payload: String): CreateUserEvent {
            return jacksonObjectMapper().readValue(payload, CreateUserEvent::class.java)
        }
    }
}