package com.openex.backend.controller

import com.openex.backend.entity.Order
import com.openex.backend.entity.OrderSide
import com.openex.backend.entity.OrderStatus
import com.openex.backend.entity.OrderType
import com.openex.backend.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(
        @RequestHeader(value = "Idempotency-Key", required = false) idempotencyKey: String?,
        @RequestBody request: CreateOrderRequest
    ): ResponseEntity<OrderResponse> {

        // Validate idempotency
        val idempotencyUUID = idempotencyKey?.let {
            try {
                UUID.fromString(it)
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
        }

        // Check if order already exists with this idempotency key
        if (idempotencyUUID != null) {
            val existingOrder = orderService.findByIdempotencyKey(idempotencyUUID)
            if (existingOrder != null) {
                return ResponseEntity.ok(OrderResponse.fromOrder(existingOrder))
            }
        }

        // Create new order
        // TODO: Get userId from JWT authentication context
        val userId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val order = orderService.createOrder(
            userId = userId,
            side = request.side,
            orderType = request.orderType,
            price = request.price,
            quantity = request.quantity,
            idempotencyKey = idempotencyUUID
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.fromOrder(order))
    }

    @GetMapping
    fun getOrders(): ResponseEntity<List<OrderResponse>> {
        // TODO: Get userId from JWT authentication context
        val userId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val orders = orderService.getOrdersByUser(userId)
        return ResponseEntity.ok(orders.map { OrderResponse.fromOrder(it) })
    }

    @GetMapping("/{orderId}")
    fun getOrder(@PathVariable orderId: UUID): ResponseEntity<OrderResponse> {
        val order = orderService.findById(orderId)
        return ResponseEntity.ok(OrderResponse.fromOrder(order))
    }
}

data class CreateOrderRequest(
    val side: OrderSide,
    val orderType: OrderType,
    val price: BigDecimal?,
    val quantity: BigDecimal
)

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