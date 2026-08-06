package com.openex.backend.service

import com.openex.backend.config.SystemAccounts
import com.openex.backend.dto.DepositRequest
import com.openex.backend.dto.DepositResponse
import com.openex.backend.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class WalletService(
    private val accountRepository: AccountRepository,
    private val ledgerService: LedgerService
) {

    fun deposit(request: DepositRequest): DepositResponse {
        val userAccount = accountRepository.findById(request.accountId)
            .orElseThrow { IllegalArgumentException("Account not found: ${request.accountId}") }

        val reserveAccount = accountRepository.findByUserIdAndCurrency(
            SystemAccounts.RESERVE_USER_ID,
            userAccount.currency
        ) ?: throw IllegalStateException(
            "No reserve account configured for currency: ${userAccount.currency}"
        )

        val transactionId = ledgerService.createTransaction(
            fromAccountId = reserveAccount.id,
            toAccountId = userAccount.id,
            amount = request.amount,
            description = "Faucet deposit"
        )

        val updatedBalance = ledgerService.getBalance(userAccount.id)

        return DepositResponse(
            transactionId = transactionId,
            newBalance = updatedBalance,
            message = "Deposit successful"
        )
    }
}