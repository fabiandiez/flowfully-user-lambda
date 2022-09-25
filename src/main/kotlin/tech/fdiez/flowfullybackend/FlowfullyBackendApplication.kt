package tech.fdiez.flowfullybackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class FlowfullyBackendApplication {

    @Bean
    fun greet(): (String) -> String {
        return { input -> "Hello from kotlin $input" }
    }

}

fun main(args: Array<String>) {
    runApplication<FlowfullyBackendApplication>(*args)
}
