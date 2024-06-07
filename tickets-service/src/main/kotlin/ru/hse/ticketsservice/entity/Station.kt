package ru.hse.ticketsservice.entity


import jakarta.persistence.*

@Entity
@Table(name = "station")
data class Station(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, unique = true)
    val station: String
)
