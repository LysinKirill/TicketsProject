package ru.hse.authenticationservice.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.hse.authenticationservice.dto.UserCredentials
import ru.hse.authenticationservice.service.JwtService
import ru.hse.authenticationservice.service.interfaces.UserCredentialsService


@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userCredentialsService: UserCredentialsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)
        val username = jwtService.getUsernameFromToken(jwt)

        if (SecurityContextHolder.getContext().authentication == null) {
            val userCredentials: UserCredentials = userCredentialsService.getUserCredentialsByEmail(username)
            if (jwtService.validateToken(jwt, userCredentials)) {
                val authenticationToken = UsernamePasswordAuthenticationToken(
                    userCredentials.email, null, emptyList()
                )
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken
            }
        }
        filterChain.doFilter(request, response)
    }
}
