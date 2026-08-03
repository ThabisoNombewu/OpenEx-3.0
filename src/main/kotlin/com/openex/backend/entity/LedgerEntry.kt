package com.openex.backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "ledger_entries")
data class LedgerEntry(
    @Id
    @Column(columnDefinition = "UUID")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "transaction_id", nullable = false, columnDefinition = "UUID")
    val transactionId: UUID,

    @Column(name = "account_id", nullable = false, columnDefinition = "UUID")
    val accountId: UUID,

    @Column(nullable = false, precision = 38, scale = 18)
    val amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    val direction: EntryDirection,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class EntryDirection {
    CREDIT, DEBIT
}