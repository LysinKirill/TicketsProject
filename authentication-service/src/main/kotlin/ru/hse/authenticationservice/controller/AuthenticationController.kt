package ru.hse.authenticationservice.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.authenticationservice.dto.requests.AuthenticationRequest
import ru.hse.authenticationservice.dto.responses.AuthenticationResponse
import ru.hse.authenticationservice.exceptions.UserAlreadyExistsException
import ru.hse.authenticationservice.service.AuthenticationServiceImpl

@RestController
@RequestMapping("/api/auth")
class UserController(private val authenticationService: AuthenticationServiceImpl) {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided details.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User registered successfully", content = [Content(mediaType = "application/json", schema = Schema(implementation = AuthenticationResponse::class))]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "409", description = "User already exists", content = [Content()])
        ]
    )
    @PostMapping("/register")
    fun registerUser(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return try {
            val result = authenticationService.registerUser(request)
            ResponseEntity(result, HttpStatus.CREATED)
        } catch (e: UserAlreadyExistsException) {
            logger.info("Failed to register the user: user with email ${request.email} already exists")
            ResponseEntity(HttpStatus.CONFLICT)
        } catch (e: Exception) {
            logger.error("Error occurred while registering user", e)
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @Operation(summary = "Login a user", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User authenticated successfully", content = [Content(mediaType = "application/json", schema = Schema(implementation = AuthenticationResponse::class))]),
            ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content()]),
        ]
    )
    @PostMapping("/login")
    fun loginUser(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return try {
            val result = authenticationService.loginUser(request)
            ResponseEntity(result, HttpStatus.OK)
        } catch (e: Exception) {
            logger.error("Error occurred while logging in user", e)
            ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @Operation(summary = "Validate JWT token", description = "Validates the given JWT token.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Token is valid", content = [Content(mediaType = "application/json", schema = Schema(implementation = Boolean::class))]),
            ApiResponse(responseCode = "401", description = "Unauthorized - Token is invalid", content = [Content()])
        ]
    )
    @PostMapping("/validate")
    fun validateToken(@RequestHeader("Authorization") token: String): ResponseEntity<Boolean> {
        return try {
            val isValid = authenticationService.validateTokenAndSession(token)
            ResponseEntity(isValid, HttpStatus.OK)
        } catch (e: Exception) {
            logger.error("Error occurred while validating token", e)
            ResponseEntity(false, HttpStatus.UNAUTHORIZED)
        }
    }
}