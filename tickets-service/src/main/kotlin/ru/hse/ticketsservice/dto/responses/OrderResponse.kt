package ru.hse.ticketsservice.dto.responses

import java.util.Date

data class OrderResponse(
    val orderId: Long,
    val userId: Long,
    val fromStationId: Long,
    val toStationId: Long,
    val createdAt: Date
)
