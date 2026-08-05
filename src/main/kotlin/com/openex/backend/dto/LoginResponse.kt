package com.openex.backend.dto

data class LoginResponse(
    val token: String,
    val username: String,
    val role: String
)