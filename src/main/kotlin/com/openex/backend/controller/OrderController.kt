package com.openex.backend.controller

import com.openex.backend.dto.CreateOrderRequest
import com.openex.backend.dto.OrderResponse
import com.openex.backend.entity.OrderSide
import com.openex.backend.entity.OrderType
import com.openex.backend.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(
        @RequestHeader(value = "Idempotency-Key", required = false) idempotencyKey: String?,
        @RequestBody request: CreateOrderRequest
    ): ResponseEntity<OrderResponse> {

        val idempotencyUUID = idempotencyKey?.let {
            try {
                UUID.fromString(it)
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
        }

        if (idempotencyUUID != null) {
            val existingOrder = orderService.findByIdempotencyKey(idempotencyUUID)
            if (existingOrder != null) {
                return ResponseEntity.ok(OrderResponse.fromOrder(existingOrder))
            }
        }

        when (request.orderType) {
            OrderType.LIMIT -> {
                if (request.price == null || request.price <= BigDecimal.ZERO) {
                    return ResponseEntity.badRequest().build()
                }
            }
            OrderType.MARKET -> {
                if (request.price != null) {
                    return ResponseEntity.badRequest().build()
                }
            }
        }

        if (request.quantity <= BigDecimal.ZERO) {
            return ResponseEntity.badRequest().build()
        }

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
