package com.openex.backend.repository

import com.openex.backend.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountRepository : JpaRepository<Account, UUID> {

    // Find account by user and currency
    fun findByUserIdAndCurrency(userId: UUID, currency: String): Account?

    // Find all accounts for a user
    fun findAllByUserId(userId: UUID): List<Account>

    // Check if account exists for user and currency
    fun existsByUserIdAndCurrency(userId: UUID, currency: String): Boolean
}