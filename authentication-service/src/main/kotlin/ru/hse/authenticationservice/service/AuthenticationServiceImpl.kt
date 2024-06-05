package ru.hse.authenticationservice.service


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.hse.authenticationservice.dto.requests.AuthenticationRequest
import ru.hse.authenticationservice.dto.requests.AuthenticationResponse
import ru.hse.authenticationservice.entity.User
import ru.hse.authenticationservice.exceptions.UserAlreadyExistsException
import ru.hse.authenticationservice.repository.UserRepository
import ru.hse.authenticationservice.service.interfaces.AuthenticationService


@Service
class AuthenticationServiceImpl(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) : AuthenticationService {
    private val passwordEncoder = BCryptPasswordEncoder()

    override fun registerUser(request: AuthenticationRequest): AuthenticationResponse {
        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser.isPresent)
            throw UserAlreadyExistsException("User with email ${request.email} already exists")

        val encodedPassword = passwordEncoder.encode(request.password)
        val user = User(
            nickname = request.nickname,
            email = request.email,
            password = encodedPassword
        )
        userRepository.save(user)

        val token = jwtService.generateToken(user)

        return AuthenticationResponse(token)
    }

    override fun loginUser(request: AuthenticationRequest): AuthenticationResponse {
        val user = userRepository.findByEmail(request.email).orElseThrow {
            IllegalArgumentException("Invalid email or password")
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid email or password")
        }

        val token = jwtService.generateToken(user)

        return AuthenticationResponse(token)
    }
}
