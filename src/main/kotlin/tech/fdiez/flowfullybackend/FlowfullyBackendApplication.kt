package tech.fdiez.flowfullybackend

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import com.fasterxml.jackson.module.kotlin.jsonMapper
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import tech.fdiez.flowfullybackend.data.UserData
import tech.fdiez.flowfullybackend.event.incoming.CreateUserEvent
import tech.fdiez.flowfullybackend.event.incoming.GetUserEvent
import tech.fdiez.flowfullybackend.event.incoming.UpdateUserEvent
import tech.fdiez.flowfullybackend.event.outgoing.UserDataEvent
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
            logger.info { "Received event for username: ${event.username}" }
            userService.createUser(event)
            APIGatewayV2HTTPResponse.builder().withStatusCode(200).build()
        }
    }

    @Bean
    fun updateUser(
        userService: UserService
    ): (APIGatewayV2HTTPEvent) -> APIGatewayV2HTTPResponse {
        return withExceptionHandling { apiGatewayEvent ->
            val event = UpdateUserEvent.from(apiGatewayEvent.body)
            logger.info { "Received event for username: ${event.username}" }
            userService.updateUser(event)
            APIGatewayV2HTTPResponse.builder().withStatusCode(200).build()
        }
    }

    @Bean
    fun getUser(
        userService: UserService
    ): (APIGatewayV2HTTPEvent) -> APIGatewayV2HTTPResponse {
        return withExceptionHandling { apiGatewayEvent ->
            val event = GetUserEvent.from(apiGatewayEvent.body)
            logger.info { "Received event for username: ${event.username}" }
            val userData: UserData? = userService.getUser(event.username)
            if (userData != null) {
                logger.info { "Getting user: ${event.username}" }
                val userDataEvent = UserDataEvent.from(userData)
                APIGatewayV2HTTPResponse.builder().withStatusCode(200)
                    .withBody(jsonMapper().writeValueAsString(userDataEvent)).build()
            } else {
                logger.info { "User ${event.username} not found" }
                APIGatewayV2HTTPResponse.builder().withStatusCode(404).build()
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<FlowfullyBackendApplication>(*args)
}