package ru.hse.authenticationservice.service


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.hse.authenticationservice.dto.requests.AuthenticationRequest
import ru.hse.authenticationservice.dto.requests.AuthenticationResponse
import ru.hse.authenticationservice.entity.User
import ru.hse.authenticationservice.repository.UserRepository


@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun registerUser(request: AuthenticationRequest): AuthenticationResponse {
        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser.isPresent) {
            throw IllegalArgumentException("User with email ${request.email} already exists")
        }

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
}
