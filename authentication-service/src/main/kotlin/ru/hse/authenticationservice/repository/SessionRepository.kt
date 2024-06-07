package ru.hse.authenticationservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.hse.authenticationservice.entity.Session
import java.util.*

interface SessionRepository : JpaRepository<Session, Long> {
    fun findByToken(token: String): Optional<Session>
    fun findByUserId(userId: Long): Optional<Session>
}
