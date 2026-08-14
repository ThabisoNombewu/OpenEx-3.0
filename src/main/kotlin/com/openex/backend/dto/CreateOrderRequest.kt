package com.openex.backend.dto

import com.openex.backend.entity.OrderSide
import com.openex.backend.entity.OrderType
import java.math.BigDecimal

data class CreateOrderRequest(
    val side: OrderSide,
    val orderType: OrderType,
    val price: BigDecimal?,
    val quantity: BigDecimal
)