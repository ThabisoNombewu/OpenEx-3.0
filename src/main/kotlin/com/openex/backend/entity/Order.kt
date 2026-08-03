package com.openex.backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @Column(columnDefinition = "UUID")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    val userId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 4)
    val side: OrderSide,

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false, length = 10)
    val orderType: OrderType,

    @Column(precision = 38, scale = 18)
    val price: BigDecimal? = null,  // Null for MARKET orders

    @Column(nullable = false, precision = 38, scale = 18)
    val quantity: BigDecimal,

    @Column(name = "filled_quantity", nullable = false, precision = 38, scale = 18)
    var filledQuantity: BigDecimal = BigDecimal.ZERO,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: OrderStatus = OrderStatus.PENDING,

    @Column(name = "idempotency_key", unique = true, columnDefinition = "UUID")
    val idempotencyKey: UUID? = null,

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

enum class OrderSide {
    BUY, SELL
}

enum class OrderType {
    LIMIT, MARKET
}

enum class OrderStatus {
    PENDING, FILLED, PARTIALLY_FILLED, CANCELLED
}