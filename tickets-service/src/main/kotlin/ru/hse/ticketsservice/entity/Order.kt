package ru.hse.ticketsservice.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*


@Entity
@Table(name = "order")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @ManyToOne
    @JoinColumn(name = "from_station_id", nullable = false)
    val fromStation: Station,

    @ManyToOne
    @JoinColumn(name = "to_station_id", nullable = false)
    val toStation: Station,

    @Convert(converter = OrderStatusConverter::class)
    @Column(name = "status", nullable = false)
    val status: OrderStatus = OrderStatus.CHECK,

    @Column(name = "created", nullable = false)
    val created: Date = Date.from(Instant.now())
)