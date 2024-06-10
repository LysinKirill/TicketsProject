package ru.hse.authenticationservice.repository


import org.springframework.data.jpa.repository.JpaRepository
import ru.hse.authenticationservice.entity.User
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}