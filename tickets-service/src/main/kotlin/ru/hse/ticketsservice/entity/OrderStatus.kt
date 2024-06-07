package ru.hse.ticketsservice.entity

enum class OrderStatus(val value: Int) {
    CHECK(1),
    SUCCESS(2),
    REJECTION(3);
}