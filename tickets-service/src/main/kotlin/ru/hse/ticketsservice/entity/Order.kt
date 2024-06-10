package ru.hse.ticketsservice.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.*


@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:Min(value = 0, message = "User ID must be greater than or equal to 1")
    @field:NotNull(message = "user ID is required")
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @field:NotNull(message = "from station id cannot be null")
    @Column(name = "from_station_id", nullable = false)
    val fromStationId: Long,

    @field:NotNull(message = "to station id cannot be null")
    @Column(name = "to_station_id", nullable = false)
    val toStationId: Long,

    @Convert(converter = OrderStatusConverter::class)
    @Column(name = "status", nullable = false)
    var status: OrderStatus = OrderStatus.CHECK,


    @Column(name = "created", nullable = false)
    val created: Date = Date.from(Instant.now())
)