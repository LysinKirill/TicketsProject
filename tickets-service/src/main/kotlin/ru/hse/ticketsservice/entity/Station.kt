package ru.hse.ticketsservice.entity


import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "station")
data class Station(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @field:NotBlank(message = "Station name cannot be blank")
    @Column(nullable = false, unique = true)
    val station: String
)
