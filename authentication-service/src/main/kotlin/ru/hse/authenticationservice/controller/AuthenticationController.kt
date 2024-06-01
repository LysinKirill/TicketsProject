package ru.hse.authenticationservice.controller



import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.authenticationservice.dto.requests.AuthenticationRequest
import ru.hse.authenticationservice.dto.requests.AuthenticationResponse
import ru.hse.authenticationservice.service.AuthenticationService

@RestController
@RequestMapping("/api/auth")
class UserController(private val authenticationService: AuthenticationService) {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/register")
    fun registerUser(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return try {
            val result = authenticationService.registerUser(request)
            ResponseEntity(result, HttpStatus.CREATED)
        } catch (e: Exception) {
            logger.error("Error occurred while registering user", e)
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}
