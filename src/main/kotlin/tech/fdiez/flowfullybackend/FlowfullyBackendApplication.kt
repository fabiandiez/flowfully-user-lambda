package tech.fdiez.flowfullybackend

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import com.fasterxml.jackson.module.kotlin.jsonMapper
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import tech.fdiez.flowfullybackend.event.incoming.CreateUserEvent
import tech.fdiez.flowfullybackend.event.incoming.GetUserEvent
import tech.fdiez.flowfullybackend.event.incoming.UpdateUserEvent
import tech.fdiez.flowfullybackend.exception.withExceptionHandling
import tech.fdiez.flowfullybackend.service.UserService

@SpringBootApplication
class FlowfullyBackendApplication {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun createUser(
        userService: UserService
    ): (APIGatewayV2HTTPEvent) -> APIGatewayV2HTTPResponse {
        return withExceptionHandling { apiGatewayEvent ->
            val event = CreateUserEvent.from(apiGatewayEvent.body)
            logger.info { "Received CreateUserEvent for username: ${event.username}" }
            val userDataEvent = userService.createUser(event)
            buildSuccessfulResponse(userDataEvent)
        }
    }

    @Bean
    fun updateUser(
        userService: UserService
    ): (APIGatewayV2HTTPEvent) -> APIGatewayV2HTTPResponse {
        return withExceptionHandling { apiGatewayEvent ->
            val event = UpdateUserEvent.from(apiGatewayEvent.body)
            logger.info { "Received UpdateUserEvent for username: ${event.username}" }
            val userDataEvent = userService.updateUser(event)
            buildSuccessfulResponse(userDataEvent)
        }
    }

    @Bean
    fun getUser(
        userService: UserService
    ): (APIGatewayV2HTTPEvent) -> APIGatewayV2HTTPResponse {
        return withExceptionHandling { apiGatewayEvent ->
            val event = GetUserEvent.from(apiGatewayEvent.body)
            logger.info { "Received GetUserEvent for username: ${event.username}" }
            val userDataEvent = userService.getUser(event.username)
            buildSuccessfulResponse(userDataEvent)
        }
    }
}

private fun buildSuccessfulResponse(): APIGatewayV2HTTPResponse =
    APIGatewayV2HTTPResponse.builder()
        .withStatusCode(200)
        .build()

private fun buildSuccessfulResponse(body: Any): APIGatewayV2HTTPResponse =
    APIGatewayV2HTTPResponse.builder()
        .withBody(jsonMapper().writeValueAsString(body))
        .withStatusCode(200)
        .build()
//
fun main(args: Array<String>) {
    runApplication<FlowfullyBackendApplication>(*args)
}