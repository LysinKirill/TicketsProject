package ru.hse.authenticationservice.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.Date

@Entity
@Table(name = "session")
data class Session(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long = 0,

    @Column(name = "token", unique = true, nullable = false)
    val token: String = "",

    @Column(name = "expires", nullable = false)
    val expires: Date = Date.from(Instant.now())
)
