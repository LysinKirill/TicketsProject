package ru.hse.authenticationservice.service

import jakarta.validation.Valid
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.hse.authenticationservice.dto.requests.LoginRequest
import ru.hse.authenticationservice.dto.requests.RegisterRequest
import ru.hse.authenticationservice.dto.responses.AuthenticationResponse
import ru.hse.authenticationservice.entity.Session
import ru.hse.authenticationservice.entity.User
import ru.hse.authenticationservice.exceptions.InvalidUserCredentialsException
import ru.hse.authenticationservice.exceptions.UserAlreadyExistsException
import ru.hse.authenticationservice.repository.SessionRepository
import ru.hse.authenticationservice.repository.UserRepository
import ru.hse.authenticationservice.service.interfaces.AuthenticationService
import java.util.*


@Service
class AuthenticationServiceImpl(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val jwtService: JwtService
) : AuthenticationService
{
    private val passwordEncoder = BCryptPasswordEncoder()

    override fun registerUser(@Valid request: RegisterRequest): AuthenticationResponse {
        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser.isPresent) {
            throw UserAlreadyExistsException("User with email ${request.email} already exists")
        }


        val encodedPassword = passwordEncoder.encode(request.password)
        val user = User(
            nickname = request.nickname,
            email = request.email,
            password = encodedPassword
        )
        userRepository.save(user)

        val token = jwtService.generateToken(user)

        createSession(user, token)

        return AuthenticationResponse(token)
    }

    override fun loginUser(request: LoginRequest): AuthenticationResponse {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { InvalidUserCredentialsException("Invalid email or password") }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw InvalidUserCredentialsException("Invalid email or password")
        }

        val token = jwtService.generateToken(user)

        sessionRepository.findByUserId(user.id).ifPresent { sessionRepository.delete(it) }
        createSession(user, token)

        return AuthenticationResponse(token)
    }

    private fun createSession(user: User, token: String) {
        val expirationDate = Date(System.currentTimeMillis() + jwtService.jwtExpirationInMs)
        val session = Session(
            userId = user.id,
            token = token,
            expires = expirationDate
        )
        sessionRepository.save(session)
    }

    override fun validateTokenAndSession(token: String): Boolean {
        if (!jwtService.validateToken(token)) {
            return false
        }

        val session = sessionRepository.findByToken(token).orElse(null) ?: return false
        return session.expires.after(Date())
    }

    override fun logoutUser(token: String) {
        val session = sessionRepository.findByToken(token)
        session.ifPresent {
            sessionRepository.delete(it)
        }
    }
}
