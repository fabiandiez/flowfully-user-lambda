package tech.fdiez.flowfullybackend

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import tech.fdiez.flowfullybackend.event.BaseEvent

private val logger = KotlinLogging.logger { }

@SpringBootApplication
class FlowfullyBackendApplication {

    @Bean
    fun handle(
        objectMapper: ObjectMapper,
    ): (SQSEvent) -> SQSBatchResponse {
        val errors = mutableListOf<SQSBatchResponse.BatchItemFailure>()
        return { sqsEvent ->
            sqsEvent.records.forEach { sqsMessage ->
                try {
                    val event = objectMapper.readValue<BaseEvent>(sqsMessage.body)
                    val flowfullyEvent = event.convert()
                    logger.info { "Received event: $flowfullyEvent" }

                } catch (e: Exception) {
                    logger.error(e) { "Error processing event: ${sqsMessage.body}" }
                    errors.add(SQSBatchResponse.BatchItemFailure(sqsMessage.messageId))
                }
            }
            SQSBatchResponse(errors)
        }
    }
}