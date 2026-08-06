package com.openex.backend.service

import com.openex.backend.dto.LoginRequest
import com.openex.backend.dto.LoginResponse
import com.openex.backend.repository.UserRepository
import com.openex.backend.security.JwtService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(request: LoginRequest): LoginResponse {

        val user = userRepository.findByUsername(request.username)
            ?: throw RuntimeException("Invalid username or password")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw RuntimeException("Invalid username or password")
        }

        val token = jwtService.generateToken(user.username)

        return LoginResponse(
            token = token,
            username = user.username,
            role = user.role
        )
    }
}