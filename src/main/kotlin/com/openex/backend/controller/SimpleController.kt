package com.openex.backend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SimpleController {

    @GetMapping("/api/ping")
    fun ping(): String {
        return "pong"
    }
}
