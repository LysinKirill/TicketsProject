package ru.hse.authenticationservice.service.interfaces

import ru.hse.authenticationservice.dto.UserCredentials

interface UserCredentialsService {
    fun getUserCredentialsByEmail(email: String): UserCredentials
}