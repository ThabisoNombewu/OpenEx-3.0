package com.openex.backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "trades")
data class Trade(
    @Id
    @Column(columnDefinition = "UUID")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "buy_order_id", nullable = false, columnDefinition = "UUID")
    val buyOrderId: UUID,

    @Column(name = "sell_order_id", nullable = false, columnDefinition = "UUID")
    val sellOrderId: UUID,

    @Column(nullable = false, precision = 38, scale = 18)
    val price: BigDecimal,

    @Column(nullable = false, precision = 38, scale = 18)
    val quantity: BigDecimal,

    @Column(name = "executed_at", updatable = false)
    val executedAt: LocalDateTime = LocalDateTime.now()
)