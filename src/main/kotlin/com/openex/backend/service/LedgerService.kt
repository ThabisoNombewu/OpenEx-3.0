package com.openex.backend.service

import com.openex.backend.entity.Account
import com.openex.backend.entity.EntryDirection
import com.openex.backend.entity.LedgerEntry
import com.openex.backend.repository.AccountRepository
import com.openex.backend.repository.LedgerEntryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.UUID

@Service
class LedgerService(
    private val accountRepository: AccountRepository,
    private val ledgerEntryRepository: LedgerEntryRepository
) {

    @Transactional
    fun createTransaction(
        fromAccountId: UUID,
        toAccountId: UUID,
        amount: BigDecimal,
        description: String = ""
    ): UUID {
        require(amount > BigDecimal.ZERO) { "Amount must be positive" }
        require(fromAccountId != toAccountId) { "Cannot transfer to same account" }

        val transactionId = UUID.randomUUID()

        val fromAccount = accountRepository.findById(fromAccountId)
            .orElseThrow { IllegalArgumentException("From account not found: $fromAccountId") }

        val toAccount = accountRepository.findById(toAccountId)
            .orElseThrow { IllegalArgumentException("To account not found: $toAccountId") }

        require(fromAccount.balance >= amount) {
            "Insufficient balance. Available: ${fromAccount.balance}, Required: $amount"
        }

        val debitEntry = LedgerEntry(
            transactionId = transactionId,
            accountId = fromAccountId,
            amount = amount,
            direction = EntryDirection.DEBIT,
            description = description
        )

        val creditEntry = LedgerEntry(
            transactionId = transactionId,
            accountId = toAccountId,
            amount = amount,
            direction = EntryDirection.CREDIT,
            description = description
        )

        fromAccount.balance = fromAccount.balance.subtract(amount)
        toAccount.balance = toAccount.balance.add(amount)

        accountRepository.save(fromAccount)
        accountRepository.save(toAccount)
        ledgerEntryRepository.save(debitEntry)
        ledgerEntryRepository.save(creditEntry)

        return transactionId
    }

    fun getBalance(accountId: UUID): BigDecimal {
        val account = accountRepository.findById(accountId)
            .orElseThrow { IllegalArgumentException("Account not found: $accountId") }
        return account.balance
    }

    fun getUserBalances(userId: UUID): Map<String, BigDecimal> {
        val accounts = accountRepository.findAllByUserId(userId)
        return accounts.associate { it.currency to it.balance }
    }

    fun validateTransaction(transactionId: UUID): Boolean {
        val entries = ledgerEntryRepository.findByTransactionId(transactionId)

        val debitTotal = entries
            .filter { it.direction == EntryDirection.DEBIT }
            .sumOf { it.amount }

        val creditTotal = entries
            .filter { it.direction == EntryDirection.CREDIT }
            .sumOf { it.amount }

        return debitTotal == creditTotal
    }
}