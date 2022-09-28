package tech.fdiez.flowfullybackend.exception

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import mu.KotlinLogging
import org.springframework.http.HttpStatus


private val logger = KotlinLogging.logger {}

fun withExceptionHandling(function: ((APIGatewayV2HTTPEvent) -> APIGatewayV2HTTPResponse)): (APIGatewayV2HTTPEvent) -> APIGatewayV2HTTPResponse {
    return {
        try {
            function.invoke(it)
        } catch (e: UsernameNotFoundException) {
            logger.info { e.message }
            APIGatewayV2HTTPResponse.builder()
                .withStatusCode(HttpStatus.NOT_FOUND.value())
                .withBody(e.message)
                .build()
        } catch (e: UsernameAlreadyExists) {
            logger.info { e.message }
            APIGatewayV2HTTPResponse.builder()
                .withStatusCode(HttpStatus.CONFLICT.value())
                .withBody(e.message)
                .build()
        } catch (e: MissingKotlinParameterException) {
            logger.info { "Unable to process event, missing mandatory field" }
            APIGatewayV2HTTPResponse.builder()
                .withStatusCode(HttpStatus.BAD_REQUEST.value())
                .withBody("Unable to process event, missing mandatory field")
                .build()
        } catch (e: Exception) {
            logger.error(e) { "Unknown error occurred while processing event" }
            APIGatewayV2HTTPResponse.builder()
                .withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .withBody(e.message)
                .build()
        }
    }
}
