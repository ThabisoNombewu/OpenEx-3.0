package com.openex.backend.service

import com.openex.backend.entity.Order
import com.openex.backend.entity.OrderSide
import com.openex.backend.entity.OrderStatus
import com.openex.backend.entity.OrderType
import com.openex.backend.entity.Trade
import com.openex.backend.repository.OrderRepository
import com.openex.backend.repository.TradeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList

@Service
class MatchingEngineService(
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val ledgerService: LedgerService
) {

    // In-memory order books - price -> list of orders
    private val buyOrders = ConcurrentHashMap<BigDecimal, MutableList<Order>>()
    private val sellOrders = ConcurrentHashMap<BigDecimal, MutableList<Order>>()
    
    // Lock for thread safety
    private val engineLock = ReentrantLock()

    @Transactional
    fun processOrder(order: Order): List<Trade> {
        engineLock.lock()
        try {
            val trades = ArrayList<Trade>()
            
            when (order.orderType) {
                OrderType.MARKET -> {
                    trades.addAll(matchMarketOrder(order))
                }
                OrderType.LIMIT -> {
                    trades.addAll(matchLimitOrder(order))
                    
                    if (order.status != OrderStatus.FILLED && order.filledQuantity < order.quantity) {
                        addToOrderBook(order)
                    }
                }
            }
            
            if (order.filledQuantity == order.quantity) {
                order.status = OrderStatus.FILLED
            } else if (order.filledQuantity > BigDecimal.ZERO && order.filledQuantity < order.quantity) {
                order.status = OrderStatus.PARTIALLY_FILLED
            }
            orderRepository.save(order)
            
            return trades
        } finally {
            engineLock.unlock()
        }
    }

    private fun matchMarketOrder(marketOrder: Order): List<Trade> {
        val trades = ArrayList<Trade>()
        var remainingQuantity = marketOrder.quantity

        val oppositeOrders = if (marketOrder.side == OrderSide.BUY) {
            sellOrders
        } else {
            buyOrders
        }

        val sortedPrices = if (marketOrder.side == OrderSide.BUY) {
            oppositeOrders.keys.sorted()
        } else {
            oppositeOrders.keys.sortedDescending()
        }

        for (price in sortedPrices) {
            if (remainingQuantity <= BigDecimal.ZERO) break
            
            val ordersAtPrice = oppositeOrders[price] ?: continue
            val iterator = ordersAtPrice.iterator()
            
            while (iterator.hasNext() && remainingQuantity > BigDecimal.ZERO) {
                val counterOrder = iterator.next()
                
                if (counterOrder.status == OrderStatus.FILLED || counterOrder.status == OrderStatus.CANCELLED) {
                    iterator.remove()
                    continue
                }
                
                val availableQuantity = counterOrder.quantity.subtract(counterOrder.filledQuantity)
                val matchQuantity = if (remainingQuantity <= availableQuantity) {
                    remainingQuantity
                } else {
                    availableQuantity
                }
                
                val trade = executeTrade(marketOrder, counterOrder, price, matchQuantity)
                trades.add(trade)
                
                remainingQuantity = remainingQuantity.subtract(matchQuantity)
                
                counterOrder.filledQuantity = counterOrder.filledQuantity.add(matchQuantity)
                if (counterOrder.filledQuantity == counterOrder.quantity) {
                    counterOrder.status = OrderStatus.FILLED
                    iterator.remove()
                } else {
                    counterOrder.status = OrderStatus.PARTIALLY_FILLED
                }
                orderRepository.save(counterOrder)
            }
            
            if (ordersAtPrice.isEmpty()) {
                oppositeOrders.remove(price)
            }
        }

        marketOrder.filledQuantity = marketOrder.quantity.subtract(remainingQuantity)
        
        if (remainingQuantity > BigDecimal.ZERO) {
            marketOrder.status = OrderStatus.CANCELLED
            marketOrder.filledQuantity = marketOrder.quantity.subtract(remainingQuantity)
        }

        return trades
    }

    private fun matchLimitOrder(limitOrder: Order): List<Trade> {
        val trades = ArrayList<Trade>()
        var remainingQuantity = limitOrder.quantity
        
        val limitPrice = limitOrder.price ?: return trades
        
        val oppositeOrders = if (limitOrder.side == OrderSide.BUY) {
            sellOrders.filterKeys { it <= limitPrice }.toSortedMap()
        } else {
            buyOrders.filterKeys { it >= limitPrice }.toSortedMap(Comparator.reverseOrder())
        }

        for ((price, ordersAtPrice) in oppositeOrders) {
            if (remainingQuantity <= BigDecimal.ZERO) break
            
            val iterator = ordersAtPrice.iterator()
            
            while (iterator.hasNext() && remainingQuantity > BigDecimal.ZERO) {
                val counterOrder = iterator.next()
                
                if (counterOrder.status == OrderStatus.FILLED || counterOrder.status == OrderStatus.CANCELLED) {
                    iterator.remove()
                    continue
                }
                
                val availableQuantity = counterOrder.quantity.subtract(counterOrder.filledQuantity)
                val matchQuantity = if (remainingQuantity <= availableQuantity) {
                    remainingQuantity
                } else {
                    availableQuantity
                }
                
                val trade = executeTrade(limitOrder, counterOrder, price, matchQuantity)
                trades.add(trade)
                
                remainingQuantity = remainingQuantity.subtract(matchQuantity)
                
                counterOrder.filledQuantity = counterOrder.filledQuantity.add(matchQuantity)
                if (counterOrder.filledQuantity == counterOrder.quantity) {
                    counterOrder.status = OrderStatus.FILLED
                    iterator.remove()
                } else {
                    counterOrder.status = OrderStatus.PARTIALLY_FILLED
                }
                orderRepository.save(counterOrder)
            }
            
            if (ordersAtPrice.isEmpty()) {
                val oppositeMap = if (limitOrder.side == OrderSide.BUY) sellOrders else buyOrders
                oppositeMap.remove(price)
            }
        }

        limitOrder.filledQuantity = limitOrder.quantity.subtract(remainingQuantity)
        return trades
    }

    private fun executeTrade(buyOrder: Order, sellOrder: Order, price: BigDecimal, quantity: BigDecimal): Trade {
        // Determine which is buy and which is sell
        val (buyOrderId, sellOrderId) = if (buyOrder.side == OrderSide.BUY) {
            Pair(buyOrder.id, sellOrder.id)
        } else {
            Pair(sellOrder.id, buyOrder.id)
        }

        // Create trade record
        val trade = Trade(
            buyOrderId = buyOrderId,
            sellOrderId = sellOrderId,
            price = price,
            quantity = quantity
        )
        val savedTrade = tradeRepository.save(trade)

        // Use the fixed account IDs that exist in the database
        // BUYER account: 11111111-1111-1111-1111-111111111111
        // SELLER account: 22222222-2222-2222-2222-222222222222
        val buyerAccountId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val sellerAccountId = UUID.fromString("22222222-2222-2222-2222-222222222222")
        val amount = price.multiply(quantity)
        
        try {
            ledgerService.createTransaction(
                fromAccountId = buyerAccountId,  // Buyer pays
                toAccountId = sellerAccountId,   // Seller receives
                amount = amount,
                description = "Trade execution: Order ${buyOrder.id} x ${sellOrder.id}"
            )
        } catch (e: Exception) {
            println("Ledger transaction failed: ${e.message}")
            throw e
        }

        return savedTrade
    }

    private fun addToOrderBook(order: Order) {
        val price = order.price ?: return
        
        val ordersMap = if (order.side == OrderSide.BUY) {
            buyOrders.computeIfAbsent(price) { Collections.synchronizedList(ArrayList()) }
        } else {
            sellOrders.computeIfAbsent(price) { Collections.synchronizedList(ArrayList()) }
        }
        
        ordersMap.add(order)
    }

    fun cancelOrder(orderId: UUID): Boolean {
        engineLock.lock()
        try {
            for ((price, orders) in buyOrders) {
                val removed = orders.removeIf { it.id == orderId }
                if (removed) {
                    if (orders.isEmpty()) buyOrders.remove(price)
                    return true
                }
            }
            
            for ((price, orders) in sellOrders) {
                val removed = orders.removeIf { it.id == orderId }
                if (removed) {
                    if (orders.isEmpty()) sellOrders.remove(price)
                    return true
                }
            }
            
            return false
        } finally {
            engineLock.unlock()
        }
    }

    fun getOrderBook(): OrderBookSnapshot {
        engineLock.lock()
        try {
            val bids = buyOrders.mapValues { it.value.sumOf { o -> o.quantity.subtract(o.filledQuantity) } }
            val asks = sellOrders.mapValues { it.value.sumOf { o -> o.quantity.subtract(o.filledQuantity) } }
            
            return OrderBookSnapshot(
                bids = bids.toSortedMap(Comparator.reverseOrder()),
                asks = asks.toSortedMap()
            )
        } finally {
            engineLock.unlock()
        }
    }
}

data class OrderBookSnapshot(
    val bids: SortedMap<BigDecimal, BigDecimal>,
    val asks: SortedMap<BigDecimal, BigDecimal>
)
