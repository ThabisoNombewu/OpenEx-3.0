package com.openex.backend.controller

import com.openex.backend.dto.LoginRequest
import com.openex.backend.dto.LoginResponse
import com.openex.backend.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(

    private val authService: AuthService

) {

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> {

        return ResponseEntity.ok(
            authService.login(request)
        )
    }
}