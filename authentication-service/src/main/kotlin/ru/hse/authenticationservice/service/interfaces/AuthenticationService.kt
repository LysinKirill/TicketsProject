package ru.hse.authenticationservice.service.interfaces

import ru.hse.authenticationservice.dto.requests.LoginRequest
import ru.hse.authenticationservice.dto.requests.RegisterRequest
import ru.hse.authenticationservice.dto.responses.AuthenticationResponse

interface AuthenticationService {
    fun registerUser(request: RegisterRequest): AuthenticationResponse
    fun loginUser(request: LoginRequest): AuthenticationResponse
    fun validateTokenAndSession(token: String): Boolean
    fun logoutUser(token: String)
}