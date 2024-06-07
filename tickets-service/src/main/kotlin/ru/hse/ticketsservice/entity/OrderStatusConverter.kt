package ru.hse.ticketsservice.entity

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter


@Converter(autoApply = true)
class OrderStatusConverter : AttributeConverter<OrderStatus, Int> {

    override fun convertToDatabaseColumn(status: OrderStatus?): Int? {
        return status?.value
    }

    override fun convertToEntityAttribute(value: Int?): OrderStatus? {
        return value?.let {
            OrderStatus.entries.firstOrNull { it.value == value }
        }
    }
}
