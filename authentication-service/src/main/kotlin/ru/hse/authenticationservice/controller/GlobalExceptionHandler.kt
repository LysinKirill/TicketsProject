package ru.hse.authenticationservice.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import ru.hse.authenticationservice.exceptions.NotFoundException
import ru.hse.authenticationservice.exceptions.UserAlreadyExistsException
import java.net.URI


@RestControllerAdvice
class GlobalExceptionHandler {
    val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)


    @ExceptionHandler(IllegalArgumentException::class)
    fun handleInvalidCredentialsException(e: IllegalArgumentException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Invalid email or password.")
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.2")

        return problemDetail
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidCredentialsException(e: HttpMessageNotReadableException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to process the query. Check the format of your query.")
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.2")

        return problemDetail
    }


    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message ?: "Resource not found")
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.5")

        return problemDetail
    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleWebExchangeBindException(
        ex: WebExchangeBindException,
        exchange: ServerWebExchange
    ): ProblemDetail {
        val fieldErrors = ex.fieldErrors.associate { error ->
            error.field to error.defaultMessage
        }

        val globalErrors = ex.globalErrors.associate { error ->
            error.objectName to error.defaultMessage
        }

        val errors = fieldErrors + globalErrors

        val problemDetail =
            ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Your request parameters didn't validate.")
        problemDetail.properties = mapOf("errors" to errors)
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.1")

        return problemDetail
    }


    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleDuplicateEmailException(e: UserAlreadyExistsException): ProblemDetail {
        val problemDetail =
            ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "${e.message}")
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.10")

        return problemDetail
    }


    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ProblemDetail {
        logger.error("Internal server error", e)
        val problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error occurred: ${e.message}"
        )
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.6.1")

        return problemDetail
    }
}