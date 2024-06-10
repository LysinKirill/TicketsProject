package ru.hse.ticketsservice.dto.responses

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import ru.hse.ticketsservice.entity.OrderStatus
import java.util.Date

data class OrderResponse(
    @field:NotNull(message = "Order ID cannot be null")
    @field:Positive(message = "Order ID must be a positive number")
    val orderId: Long,

    @field:NotNull(message = "User ID cannot be null")
    @field:Positive(message = "User ID must be a positive number")
    val userId: Long,

    @field:NotNull(message = "From Station ID cannot be null")
    @field:Positive(message = "From Station ID must be a positive number")
    val fromStationId: Long,

    @field:NotNull(message = "To Station ID cannot be null")
    @field:Positive(message = "To Station ID must be a positive number")
    val toStationId: Long,
    val createdAt: Date,
    val orderStatus: OrderStatus
)
