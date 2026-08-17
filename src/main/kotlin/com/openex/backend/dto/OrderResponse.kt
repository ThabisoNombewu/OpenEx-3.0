package com.openex.backend.dto

import com.openex.backend.entity.Order
import com.openex.backend.entity.OrderSide
import com.openex.backend.entity.OrderStatus
import com.openex.backend.entity.OrderType
import java.math.BigDecimal
import java.util.UUID

data class OrderResponse(
    val id: UUID,
    val userId: UUID,
    val side: OrderSide,
    val orderType: OrderType,
    val price: BigDecimal?,
    val quantity: BigDecimal,
    val filledQuantity: BigDecimal,
    val status: OrderStatus,
    val idempotencyKey: UUID?,
    val createdAt: String
) {
    companion object {
        fun fromOrder(order: Order): OrderResponse {
            return OrderResponse(
                id = order.id,
                userId = order.userId,
                side = order.side,
                orderType = order.orderType,
                price = order.price,
                quantity = order.quantity,
                filledQuantity = order.filledQuantity,
                status = order.status,
                idempotencyKey = order.idempotencyKey,
                createdAt = order.createdAt.toString()
            )
        }
    }
}