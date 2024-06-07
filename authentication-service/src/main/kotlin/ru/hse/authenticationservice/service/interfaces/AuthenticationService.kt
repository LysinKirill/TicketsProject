package ru.hse.authenticationservice.service.interfaces

import ru.hse.authenticationservice.dto.requests.AuthenticationRequest
import ru.hse.authenticationservice.dto.requests.AuthenticationResponse

interface AuthenticationService {
    fun registerUser(request: AuthenticationRequest): AuthenticationResponse
    fun loginUser(request: AuthenticationRequest): AuthenticationResponse
    fun validateTokenAndSession(token: String): Boolean
    fun logoutUser(token: String)
}