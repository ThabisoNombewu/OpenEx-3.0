package com.openex.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OpenexBackendApplication

fun main(args: Array<String>) {
	runApplication<OpenexBackendApplication>(*args)
}
