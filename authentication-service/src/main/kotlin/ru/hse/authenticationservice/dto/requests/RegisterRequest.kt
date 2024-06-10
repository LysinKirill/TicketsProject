package ru.hse.authenticationservice.dto.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class RegisterRequest(
    @field: NotBlank(message = "Nickname cannot be empty")
    val nickname: String,

    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Email should be valid")
    val email: String,

    @field:NotBlank(message = "Password cannot be blank")
    @field:Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$",
        message = "Password must be at least 8 characters long and include uppercase, lowercase, digit, and special character."
    )
    val password: String
)