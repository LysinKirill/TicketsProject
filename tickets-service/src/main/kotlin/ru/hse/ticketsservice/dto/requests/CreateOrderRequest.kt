package ru.hse.ticketsservice.dto.requests

import java.util.Date

data class CreateOrderRequest(
    val userId: Long,
    val timestamp: Date,
    val fromStationId: Long,
    val toStationId: Long,
)
