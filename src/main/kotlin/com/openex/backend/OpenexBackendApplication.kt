package com.openex.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.openex.backend.repository"])
class OpenexBackendApplication

fun main(args: Array<String>) {
	runApplication<OpenexBackendApplication>(*args)
}