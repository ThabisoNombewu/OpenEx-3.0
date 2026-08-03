package com.openex.backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @Column(columnDefinition = "UUID")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    val userId: UUID,

    @Column(nullable = false, length = 10)
    var currency: String,

    @Column(nullable = false, precision = 38, scale = 18)
    var balance: BigDecimal = BigDecimal.ZERO,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // Pre-update hook to automatically update timestamp
    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}