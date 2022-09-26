package tech.fdiez.flowfullybackend.event.incoming

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import tech.fdiez.flowfullybackend.event.FlowfullyEvent
import tech.fdiez.flowfullybackend.event.EventType
import javax.validation.constraints.NotBlank

data class CreateUserEvent(
    @get:NotBlank val username: String,
    @get:NotBlank val password: String,
    @get:NotBlank val todoistApiToken: String,
    @get:NotBlank val todoistWebhookUrl: String,
    @get:NotBlank val todoistUserId: String,
) : FlowfullyEvent {


    override val eventType = EventType.CREATE_USER

    companion object {
        fun from(payload: String): CreateUserEvent {
            return jacksonObjectMapper().readValue(payload, CreateUserEvent::class.java)
        }
    }
}