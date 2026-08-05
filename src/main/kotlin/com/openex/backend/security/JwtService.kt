package com.openex.backend.security

import org.springframework.stereotype.Service

@Service
class JwtService {

    fun generateToken(username: String): String {

        // TODO: Generate JWT
        return ""
    }

    fun extractUsername(token: String): String? {

        // TODO
        return null
    }

    fun isTokenValid(token: String): Boolean {

        // TODO
        return true
    }
}