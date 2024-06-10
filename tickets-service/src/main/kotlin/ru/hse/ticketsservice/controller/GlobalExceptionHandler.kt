package ru.hse.ticketsservice.controller

import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.hse.ticketsservice.exceptions.OrderNotFoundException
import ru.hse.ticketsservice.exceptions.InvalidOrderException
import ru.hse.ticketsservice.exceptions.StationNotFoundException
import java.net.URI

@RestControllerAdvice
class GlobalExceptionHandler {

    val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFoundException(e: OrderNotFoundException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message ?: "Order not found")
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.5")
        return problemDetail
    }

    @ExceptionHandler(StationNotFoundException::class)
    fun handleOrderNotFoundException(e: StationNotFoundException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message ?: "Station not found")
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.5")
        return problemDetail
    }

    @ExceptionHandler(InvalidOrderException::class)
    fun handleInvalidOrderException(e: InvalidOrderException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message ?: "Invalid order details")
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.1")
        return problemDetail
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ProblemDetail {
        val fieldErrors = e.bindingResult.allErrors.associate { error ->
            val fieldName = if (error is FieldError) error.field else "object"
            fieldName to error.defaultMessage
        }

        val fieldErrorsStr = e.fieldErrors.joinToString { error ->
            "${error.field}: ${error.defaultMessage}"
        }

        val problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Validation failed for request fields: $fieldErrorsStr"
        )
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.1")
        problemDetail.properties?.set("fieldErrors", fieldErrors)
        return problemDetail
    }


    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Malformed JSON request")
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.1")
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

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ProblemDetail {
        val violations = e.constraintViolations.associate {
            it.propertyPath.toString() to it.message
        }

        val fieldErrorsStr = e.constraintViolations.joinToString { error -> error.message }

        val problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Constraint violations: $fieldErrorsStr"
        )
        problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.1")
        problemDetail.properties?.set("violations", violations)

        return problemDetail
    }
}
