package com.openex.backend.repository

import com.openex.backend.entity.Trade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TradeRepository : JpaRepository<Trade, UUID> {

    // Find trades by order
    fun findAllByBuyOrderIdOrSellOrderId(buyOrderId: UUID, sellOrderId: UUID): List<Trade>

    // Find trades for a user (via order)
    // This requires a custom query since user_id is not directly in trades
}