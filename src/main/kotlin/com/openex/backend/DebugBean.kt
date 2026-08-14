package com.openex.backend

import jakarta.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class DebugBean(
    private val applicationContext: ApplicationContext
) {

    @PostConstruct
    fun debug() {
        println("=== All Bean Names ===")
        applicationContext.beanDefinitionNames.forEach { println(it) }
        println("=== End of Beans ===")
        
        // Check specifically for controllers
        println("=== Controllers ===")
        applicationContext.getBeansWithAnnotation(org.springframework.stereotype.Controller::class.java)
            .forEach { (name, bean) -> println("Controller: $name - $bean") }
        println("=== RestControllers ===")
        applicationContext.getBeansWithAnnotation(org.springframework.web.bind.annotation.RestController::class.java)
            .forEach { (name, bean) -> println("RestController: $name - $bean") }
        println("=== End ===")
    }
}
