package com.openex.backend

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class ComponentChecker {

    @PostConstruct
    fun check() {
        println("✅ ComponentChecker is loaded by Spring!")
    }
}
