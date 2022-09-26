package tech.fdiez.flowfullybackend.event.incoming

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import tech.fdiez.flowfullybackend.event.FlowfullyEvent
import tech.fdiez.flowfullybackend.event.EventType

data class CreateUserEvent(val username: String, val password: String): FlowfullyEvent {


    override val eventType = EventType.CREATE_USER

    companion object {
        fun from(payload: String): CreateUserEvent {
            return jacksonObjectMapper().readValue(payload, CreateUserEvent::class.java)
        }
    }
}