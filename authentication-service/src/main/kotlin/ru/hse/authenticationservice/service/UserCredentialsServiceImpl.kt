package ru.hse.authenticationservice.service

import org.springframework.stereotype.Service
import ru.hse.authenticationservice.dto.UserCredentials
import ru.hse.authenticationservice.repository.UserRepository
import ru.hse.authenticationservice.service.interfaces.UserCredentialsService

@Service
class UserCredentialsServiceImpl(private val userRepository: UserRepository) : UserCredentialsService{

    override fun getUserCredentialsByEmail(email: String): UserCredentials {
        val user = userRepository.findByEmail(email).orElseThrow {
            throw RuntimeException("User not found with email: $email")
        }
        return UserCredentials(email = user.email, password = user.password)
    }
}
