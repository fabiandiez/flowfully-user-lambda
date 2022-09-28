package tech.fdiez.flowfullyuserlambda.event.incoming

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javax.validation.constraints.NotBlank

data class GetUserEvent(
    @get:NotBlank val username: String
) {
    companion object {
        fun from(payload: String): GetUserEvent {
            return jacksonObjectMapper().readValue(payload, GetUserEvent::class.java)
        }
    }
}