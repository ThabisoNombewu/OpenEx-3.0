package com.openex.backend.repository

import com.openex.backend.entity.LedgerEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LedgerEntryRepository : JpaRepository<LedgerEntry, UUID> {

    // Find all entries for a transaction
    fun findByTransactionId(transactionId: UUID): List<LedgerEntry>

    // Find all entries for an account
    fun findAllByAccountId(accountId: UUID): List<LedgerEntry>

    // Find all entries for an account ordered by time
    fun findAllByAccountIdOrderByCreatedAtDesc(accountId: UUID): List<LedgerEntry>
}