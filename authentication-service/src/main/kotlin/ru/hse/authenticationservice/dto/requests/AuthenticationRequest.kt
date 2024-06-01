package ru.hse.authenticationservice.dto.requests

data class AuthenticationRequest(
    val nickname: String,
    val email: String,
    val password: String
)
