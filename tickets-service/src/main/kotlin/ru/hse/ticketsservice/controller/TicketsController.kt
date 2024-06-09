package ru.hse.ticketsservice.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.ticketsservice.dto.requests.CreateOrderRequest
import ru.hse.ticketsservice.dto.responses.OrderResponse
import ru.hse.ticketsservice.service.interfaces.TicketService

@RestController
@RequestMapping("/api/tickets")
class TicketsController(private val ticketService: TicketService) {

    @Operation(summary = "Create a new order", description = "Creates a new order with the given details.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Order created successfully", content = [Content(mediaType = "application/json", schema = Schema(implementation = OrderResponse::class))]),
            ApiResponse(responseCode = "400", description = "Invalid input data")
        ]
    )
    @PostMapping("/create-order")
    fun createOrder(@RequestBody request: CreateOrderRequest): ResponseEntity<OrderResponse> {
        val orderResponse = ticketService.createOrder(request)
        return ResponseEntity.ok(orderResponse)
    }

    @Operation(summary = "Get order by ID", description = "Fetches the order details for the given order ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Order found", content = [Content(mediaType = "application/json", schema = Schema(implementation = OrderResponse::class))]),
            ApiResponse(responseCode = "404", description = "Order not found")
        ]
    )
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
