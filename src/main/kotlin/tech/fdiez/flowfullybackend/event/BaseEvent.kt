package tech.fdiez.flowfullybackend.event

import tech.fdiez.flowfullybackend.event.incoming.CreateUserEvent
import tech.fdiez.flowfullybackend.event.incoming.UpdateUserEvent


data class BaseEvent(override val eventType: EventType, val payload: String): FlowfullyEvent {
    fun convert() : FlowfullyEvent {
        return when (eventType) {
            EventType.CREATE_USER -> CreateUserEvent.from(payload)
            EventType.UPDATE_USER -> UpdateUserEvent.from(payload)
        }
    }
}
