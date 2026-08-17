package com.openex.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = [
    "com.openex.backend",
    "com.openex.backend.controller",
    "com.openex.backend.service",
    "com.openex.backend.repository",
    "com.openex.backend.config",
    "com.openex.backend.security"
])
@EnableJpaRepositories(basePackages = ["com.openex.backend.repository"])
class OpenexBackendApplication

fun main(args: Array<String>) {
    runApplication<OpenexBackendApplication>(*args)
}
