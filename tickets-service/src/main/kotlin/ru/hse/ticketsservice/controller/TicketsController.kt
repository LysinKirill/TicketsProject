package ru.hse.ticketsservice.controller

import ru.hse.ticketsservice.dto.requests.CreateOrderRequest
import ru.hse.ticketsservice.service.interfaces.TicketService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.ticketsservice.dto.responses.OrderResponse


@RestController
@RequestMapping("/api/tickets")
class TicketsController(private val ticketService: TicketService) {

    @PostMapping("/create-order")
    fun createOrder(@RequestBody request: CreateOrderRequest): ResponseEntity<OrderResponse> {
        val orderResponse = ticketService.createOrder(request)
        return ResponseEntity.ok(orderResponse)
    }

    @GetMapping("/orders/{orderId}")
    fun getOrderById(@PathVariable orderId: Long): ResponseEntity<OrderResponse> {
        val orderResponse = ticketService.getOrderById(orderId)
        return if (orderResponse.isPresent) {
            ResponseEntity.ok(orderResponse.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
