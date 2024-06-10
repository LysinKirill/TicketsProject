package ru.hse.ticketsservice.service.interfaces

import ru.hse.ticketsservice.dto.requests.CreateOrderRequest
import ru.hse.ticketsservice.dto.responses.OrderResponse
import java.util.*

interface TicketService {
    fun createOrder(request: CreateOrderRequest) : OrderResponse
    fun getOrderById(orderId: Long) : Optional<OrderResponse>
}