package com.openex.backend.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.openex.backend.dto.CreateOrderRequest
import com.openex.backend.entity.OrderSide
import com.openex.backend.entity.OrderType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should create a limit order successfully`() {
        val idempotencyKey = UUID.randomUUID()
        val request = CreateOrderRequest(
            side = OrderSide.BUY,
            orderType = OrderType.LIMIT,
            price = BigDecimal("50000"),
            quantity = BigDecimal("1.5")
        )

        mockMvc.perform(
            post("/api/orders")
                .header("Idempotency-Key", idempotencyKey.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.side").value("BUY"))
            .andExpect(jsonPath("$.orderType").value("LIMIT"))
            .andExpect(jsonPath("$.price").value(50000))
            .andExpect(jsonPath("$.quantity").value(1.5))
            .andExpect(jsonPath("$.status").value("PENDING"))
    }

    @Test
    fun `should return cached response for duplicate idempotency key`() {
        val idempotencyKey = UUID.randomUUID()
        val request = CreateOrderRequest(
            side = OrderSide.BUY,
            orderType = OrderType.LIMIT,
            price = BigDecimal("50000"),
            quantity = BigDecimal("1.5")
        )

        // First request - should create
        val firstResult = mockMvc.perform(
            post("/api/orders")
                .header("Idempotency-Key", idempotencyKey.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val firstResponse = firstResult.response.contentAsString
        val firstOrderId = objectMapper.readTree(firstResponse).get("id").asText()

        // Second request with same key - should return cached (200 OK)
        val secondResult = mockMvc.perform(
            post("/api/orders")
                .header("Idempotency-Key", idempotencyKey.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.side").value("BUY"))
            .andReturn()

        val secondResponse = secondResult.response.contentAsString
        val secondOrderId = objectMapper.readTree(secondResponse).get("id").asText()

        // Verify both responses have the same ID (cached)
        assert(firstOrderId == secondOrderId) { "Cached response should return the same order ID" }
    }

    @Test
    fun `should reject limit order without price`() {
        val idempotencyKey = UUID.randomUUID()
        val request = CreateOrderRequest(
            side = OrderSide.BUY,
            orderType = OrderType.LIMIT,
            price = null,
            quantity = BigDecimal("1.5")
        )

        mockMvc.perform(
            post("/api/orders")
                .header("Idempotency-Key", idempotencyKey.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject market order with price`() {
        val idempotencyKey = UUID.randomUUID()
        val request = CreateOrderRequest(
            side = OrderSide.BUY,
            orderType = OrderType.MARKET,
            price = BigDecimal("50000"),
            quantity = BigDecimal("1.5")
        )

        mockMvc.perform(
            post("/api/orders")
                .header("Idempotency-Key", idempotencyKey.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject invalid idempotency key format`() {
        val request = CreateOrderRequest(
            side = OrderSide.BUY,
            orderType = OrderType.LIMIT,
            price = BigDecimal("50000"),
            quantity = BigDecimal("1.5")
        )

        mockMvc.perform(
            post("/api/orders")
                .header("Idempotency-Key", "invalid-uuid-format")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject zero quantity`() {
        val idempotencyKey = UUID.randomUUID()
        val request = CreateOrderRequest(
            side = OrderSide.BUY,
            orderType = OrderType.LIMIT,
            price = BigDecimal("50000"),
            quantity = BigDecimal.ZERO
        )

        mockMvc.perform(
            post("/api/orders")
                .header("Idempotency-Key", idempotencyKey.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject negative quantity`() {
        val idempotencyKey = UUID.randomUUID()
        val request = CreateOrderRequest(
            side = OrderSide.BUY,
            orderType = OrderType.LIMIT,
            price = BigDecimal("50000"),
            quantity = BigDecimal("-1.5")
        )

        mockMvc.perform(
            post("/api/orders")
                .header("Idempotency-Key", idempotencyKey.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }
}
