package com.openex.backend.controller

import com.openex.backend.dto.DepositRequest
import com.openex.backend.dto.DepositResponse
import com.openex.backend.service.WalletService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/wallets")
class WalletController(
    private val walletService: WalletService
) {

    @PostMapping("/deposit")
    fun deposit(@RequestBody request: DepositRequest): ResponseEntity<DepositResponse> {
        return ResponseEntity.ok(walletService.deposit(request))
    }
}