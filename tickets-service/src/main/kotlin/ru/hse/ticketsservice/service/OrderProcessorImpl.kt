package ru.hse.ticketsservice.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.hse.ticketsservice.entity.Order
import ru.hse.ticketsservice.service.interfaces.OrderProcessor

@Service
class OrderProcessorImpl : OrderProcessor {
    private val logger = LoggerFactory.getLogger(OrderProcessorImpl::class.java)
    override fun addOrderToProcessing(order: Order) {
        logger.info("Adding order ${order.id}")
    }
}