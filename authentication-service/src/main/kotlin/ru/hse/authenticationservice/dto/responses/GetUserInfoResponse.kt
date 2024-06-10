package ru.hse.authenticationservice.dto.responses

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class GetUserInfoResponse(
    @field: NotNull(message = "User id cannot be null")
    @field: Positive(message = "User id must be positive")
    val userId: Long,

    @field: NotBlank(message = "Nickname cannot be empty")
    val nickname: String,

    @field: NotBlank(message = "Email cannot be empty")
    val email: String,
)