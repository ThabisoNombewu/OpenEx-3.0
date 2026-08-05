package com.openex.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
data class User(

    @Id
    @Column(columnDefinition = "UUID")
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true, length = 100)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var role: String = "USER",

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}