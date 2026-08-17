package com.openex.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Invalid request",
            path = ""
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.CONFLICT.value(),
            error = "Conflict",
            message = ex.message ?: "Resource conflict",
            path = ""
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error)
    }
}

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)