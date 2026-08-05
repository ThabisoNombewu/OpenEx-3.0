package com.openex.backend.dto

import java.math.BigDecimal
import java.util.UUID

data class DepositRequest(
    val accountId: UUID,
    val amount: BigDecimal
)
