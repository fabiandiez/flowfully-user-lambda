package tech.fdiez.flowfullybackend

import com.amazonaws.services.lambda.runtime.events.SQSEvent
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class FlowfullyBackendApplication {

    @Bean
    fun getUser(): (SQSEvent) -> String {
        return { input ->
            for (record in input.records) {
                println(record.body)
            }
            "Hello from kotlin $input"
        }
    }

}

fun main(args: Array<String>) {
    runApplication<FlowfullyBackendApplication>(*args)
}
