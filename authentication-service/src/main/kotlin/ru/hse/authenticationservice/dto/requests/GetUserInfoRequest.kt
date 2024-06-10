package ru.hse.authenticationservice.dto.requests

import jakarta.validation.constraints.NotBlank

data class GetUserInfoRequest(
    @field:NotBlank(message = "Password cannot be blank")
    val userToken: String
)