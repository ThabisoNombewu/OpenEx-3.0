package com.openex.backend.dto

import java.math.BigDecimal
import java.util.UUID

data class DepositResponse(
    val transactionId: UUID,
    val newBalance: BigDecimal,
    val message: String
)