package ru.hse.authenticationservice.entity
import jakarta.persistence.*


@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nickname: String = "",
    val email: String = "",
    val password: String = ""
)
