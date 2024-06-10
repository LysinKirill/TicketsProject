package ru.hse.authenticationservice.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.authenticationservice.dto.requests.LoginRequest
import ru.hse.authenticationservice.dto.requests.RegisterRequest
import ru.hse.authenticationservice.dto.responses.AuthenticationResponse
import ru.hse.authenticationservice.service.AuthenticationServiceImpl

@RestController
@RequestMapping("/api/auth")
class UserController(private val authenticationService: AuthenticationServiceImpl) {

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided details.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User registered successfully", content = [Content(mediaType = "application/json", schema = Schema(implementation = AuthenticationResponse::class))]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "409", description = "User already exists", content = [Content()])
        ]
    )
    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody request: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        val result = authenticationService.registerUser(request)
        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @Operation(summary = "Login a user", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User authenticated successfully", content = [Content(mediaType = "application/json", schema = Schema(implementation = AuthenticationResponse::class))]),
            ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ]
    )
    @PostMapping("/login")
    fun loginUser(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthenticationResponse> {
        val result = authenticationService.loginUser(request)
        return ResponseEntity(result, HttpStatus.OK)
    }
}