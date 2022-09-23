package tech.fdiez.flowfullybackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlowfullyBackendApplication

fun main(args: Array<String>) {
	runApplication<FlowfullyBackendApplication>(*args)
}
