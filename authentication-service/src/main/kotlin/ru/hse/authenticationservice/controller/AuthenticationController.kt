package ru.hse.authenticationservice.controller



import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.authenticationservice.dto.requests.AuthenticationRequest
import ru.hse.authenticationservice.dto.requests.AuthenticationResponse
import ru.hse.authenticationservice.exceptions.UserAlreadyExistsException
import ru.hse.authenticationservice.service.AuthenticationServiceImpl

@RestController
@RequestMapping("/api/auth")
class UserController(private val authenticationService: AuthenticationServiceImpl) {

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


    @PostMapping("/login")
    fun loginUser(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return try {
            val result = authenticationService.loginUser(request)
            ResponseEntity(result, HttpStatus.OK)
        }
        catch(e: UserAlreadyExistsException){
            logger.info("Failed to register the user: user with email ${request.email} already exists")
            ResponseEntity(HttpStatus.CONFLICT)
        }
        catch (e: Exception) {
            logger.error("Error occurred while logging in user", e)
            ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }
}
