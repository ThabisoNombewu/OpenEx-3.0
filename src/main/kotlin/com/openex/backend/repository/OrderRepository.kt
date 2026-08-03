package com.openex.backend.repository

import com.openex.backend.entity.Order
import com.openex.backend.entity.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OrderRepository : JpaRepository<Order, UUID> {

    // Find orders by user
    fun findAllByUserId(userId: UUID): List<Order>

    // Find orders by status
    fun findAllByStatus(status: OrderStatus): List<Order>

    // Find orders by user and status
    fun findAllByUserIdAndStatus(userId: UUID, status: OrderStatus): List<Order>

    // Find order by idempotency key
    fun findByIdempotencyKey(idempotencyKey: UUID): Order?

    // Find pending orders (for matching engine)
    fun findAllByStatusIn(statuses: List<OrderStatus>): List<Order>
}