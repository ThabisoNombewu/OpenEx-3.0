package com.openex.backend.service

import com.openex.backend.entity.Order
import com.openex.backend.entity.OrderSide
import com.openex.backend.entity.OrderStatus
import com.openex.backend.entity.OrderType
import com.openex.backend.repository.OrderRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.UUID

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {

    @Transactional
    fun createOrder(
        userId: UUID,
        side: OrderSide,
        orderType: OrderType,
        price: BigDecimal?,
        quantity: BigDecimal,
        idempotencyKey: UUID?
    ): Order {
        when (orderType) {
            OrderType.LIMIT -> {
                require(price != null && price > BigDecimal.ZERO) {
                    "Limit order requires a positive price"
                }
            }
            OrderType.MARKET -> {
                require(price == null) {
                    "Market order should not have a price"
                }
            }
        }
        require(quantity > BigDecimal.ZERO) {
            "Quantity must be positive"
        }

        if (idempotencyKey != null) {
            val existing = orderRepository.findByIdempotencyKey(idempotencyKey)
            if (existing != null) {
                throw IllegalStateException("Order already exists with idempotency key: $idempotencyKey")
            }
        }

        val order = Order(
            userId = userId,
            side = side,
            orderType = orderType,
            price = price,
            quantity = quantity,
            idempotencyKey = idempotencyKey,
            status = OrderStatus.PENDING
        )

        return orderRepository.save(order)
    }

    fun findById(orderId: UUID): Order {
        return orderRepository.findById(orderId)
            .orElseThrow { IllegalArgumentException("Order not found: $orderId") }
    }

    @Cacheable(value = ["idempotency"], key = "#key", unless = "#result == null")
    fun findByIdempotencyKey(key: UUID): Order? {
        return orderRepository.findByIdempotencyKey(key)
    }

    fun getOrdersByUser(userId: UUID): List<Order> {
        return orderRepository.findAllByUserId(userId)
    }

    fun getOrdersByStatus(status: OrderStatus): List<Order> {
        return orderRepository.findAllByStatus(status)
    }
}