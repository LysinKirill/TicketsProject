package ru.hse.ticketsservice.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.hse.ticketsservice.entity.Order
import ru.hse.ticketsservice.entity.OrderStatus
import ru.hse.ticketsservice.repository.OrderRepository
import ru.hse.ticketsservice.service.interfaces.OrderProcessor
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
class OrderProcessorImpl(
    private val orderRepository: OrderRepository,
    @Value("\${orderProcessor.batchSize:3}") private val batchSize: Int = 3,
    @Value("\${orderProcessor.timeoutInMs:20000}") private val timeoutInMs: Long = 5000,

    ) : OrderProcessor {
    private val orderQueue: Queue<Order> = ConcurrentLinkedQueue()
    private val logger: Logger = LoggerFactory.getLogger(OrderProcessorImpl::class.java)


    private val executor = Executors.newSingleThreadScheduledExecutor()

    init {
        executor.scheduleAtFixedRate({
            processOrders()
        }, 0, timeoutInMs, TimeUnit.MILLISECONDS)
    }

    private fun processOrders() {
        val currentBatch = mutableListOf<Order>()
        while (orderQueue.isNotEmpty() && currentBatch.size < batchSize) {
            orderQueue.poll()?.let {
                currentBatch.add(it)
            }
        }

        for (order in currentBatch) {
            order.status = if (Math.random() < 0.9) OrderStatus.SUCCESS else OrderStatus.REJECTION
            orderRepository.save(order)
            logger.info("Order ${order.id} processed with status ${order.status}")
        }
    }

    override fun addOrderToProcessing(order: Order) {
        when (order.status) {
            OrderStatus.SUCCESS -> {
                logger.info("The order with id ${order.id} has already been processed successfully")
            }

            OrderStatus.REJECTION -> {
                logger.info("The order with id ${order.id} has already been rejected")
            }

            else -> {
                logger.info("Adding order ${order.id} to the processing queue")
                orderQueue.add(order)
            }
        }
    }
}
