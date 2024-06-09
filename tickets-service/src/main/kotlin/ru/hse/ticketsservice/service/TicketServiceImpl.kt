package ru.hse.ticketsservice.service

import org.springframework.stereotype.Service
import ru.hse.ticketsservice.dto.requests.CreateOrderRequest
import ru.hse.ticketsservice.dto.responses.OrderResponse
import ru.hse.ticketsservice.entity.Order
import ru.hse.ticketsservice.entity.OrderStatus
import ru.hse.ticketsservice.exceptions.StationNotFoundException
import ru.hse.ticketsservice.repository.OrderRepository
import ru.hse.ticketsservice.repository.StationRepository
import ru.hse.ticketsservice.service.interfaces.OrderProcessor
import ru.hse.ticketsservice.service.interfaces.TicketService
import java.util.*

@Service
class TicketServiceImpl(
    val orderRepository: OrderRepository,
    val stationRepository: StationRepository,
    val orderProcessor: OrderProcessor
) : TicketService {
    override fun createOrder(request: CreateOrderRequest): OrderResponse {
        val (userId, timestamp, fromStationId, toStationId) = request
        if (!stationRepository.existsById(fromStationId) || !stationRepository.existsById(toStationId)) {
            throw StationNotFoundException()
        }

        val newOrder = Order(
            userId = userId,
            fromStationId = fromStationId,
            toStationId = toStationId,
            created = timestamp,
        )

        val newOrderId = orderRepository.save(newOrder).id
        orderProcessor.addOrderToProcessing(newOrder)
        return OrderResponse(
            orderId = newOrderId,
            userId = userId,
            fromStationId = fromStationId,
            toStationId = toStationId,
            createdAt = timestamp,
            orderStatus = OrderStatus.CHECK
        )
    }

    override fun getOrderById(orderId: Long): Optional<OrderResponse> {
        val order = orderRepository.findById(orderId)
        return if (order.isPresent) {
            val orderResult = order.get()
            Optional.of(
                OrderResponse(
                    orderId = orderResult.id,
                    userId = orderResult.userId,
                    fromStationId = orderResult.fromStationId,
                    toStationId = orderResult.toStationId,
                    createdAt = orderResult.created,
                    orderStatus = orderResult.status
                )
            )
        } else {
            Optional.empty()
        }
    }
}