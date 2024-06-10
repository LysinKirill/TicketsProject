package ru.hse.ticketsservice.dto.requests

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.util.Date

data class CreateOrderRequest(
    @field:NotNull(message = "User ID cannot be null")
    @field:Positive(message = "User ID must be a positive number")
    val userId: Long,
    val timestamp: Date,

    @field:NotNull(message = "From Station ID cannot be null")
    @field:Positive(message = "From Station ID must be a positive number")
    val fromStationId: Long,

    @field:NotNull(message = "To Station ID cannot be null")
    @field:Positive(message = "To Station ID must be a positive number")
    val toStationId: Long,
)
