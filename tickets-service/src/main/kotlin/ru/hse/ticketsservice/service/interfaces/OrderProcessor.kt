package ru.hse.ticketsservice.service.interfaces

import ru.hse.ticketsservice.entity.Order

interface OrderProcessor {
    fun addOrderToProcessing(order: Order)
}