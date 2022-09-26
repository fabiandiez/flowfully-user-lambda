package tech.fdiez.flowfullybackend.event.incoming

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import tech.fdiez.flowfullybackend.event.EventType
import tech.fdiez.flowfullybackend.event.FlowfullyEvent

data class UpdateUserEvent(
    val username: String,
    val todoistApiToken: String?,
    val todoistWebhookUrl: String?,
    val todoistUserId: String?,
) : FlowfullyEvent {

    override val eventType = EventType.UPDATE_USER

    companion object {
        fun from(payload: String): UpdateUserEvent {
            return jacksonObjectMapper().readValue(payload, UpdateUserEvent::class.java)
        }
    }
}