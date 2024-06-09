package ru.hse.ticketsservice.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*


@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "from_station_id", nullable = false)
    val fromStationId: Long,

    @Column(name = "to_station_id", nullable = false)
    val toStationId: Long,

    @Convert(converter = OrderStatusConverter::class)
    @Column(name = "status", nullable = false)
    val status: OrderStatus = OrderStatus.CHECK,

    @Column(name = "created", nullable = false)
    val created: Date = Date.from(Instant.now())
)