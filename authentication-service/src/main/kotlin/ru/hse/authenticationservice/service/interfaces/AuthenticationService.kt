package ru.hse.authenticationservice.service.interfaces

import ru.hse.authenticationservice.dto.requests.GetUserInfoRequest
import ru.hse.authenticationservice.dto.requests.LoginRequest
import ru.hse.authenticationservice.dto.requests.RegisterRequest
import ru.hse.authenticationservice.dto.responses.AuthenticationResponse
import ru.hse.authenticationservice.dto.responses.GetUserInfoResponse

interface AuthenticationService {
    fun registerUser(request: RegisterRequest): AuthenticationResponse
    fun loginUser(request: LoginRequest): AuthenticationResponse
    fun validateTokenAndSession(token: String): Boolean
    fun getUserInfo(request: GetUserInfoRequest): GetUserInfoResponse
    fun logoutUser(token: String)
}