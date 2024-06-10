package ru.hse.authenticationservice.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.hse.authenticationservice.service.JwtService
import ru.hse.authenticationservice.service.interfaces.AuthenticationService

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val authenticationService: AuthenticationService
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
        val username = jwtService.getEmailFromToken(jwt)

        if (SecurityContextHolder.getContext().authentication == null) {
            if (jwtService.validateToken(jwt) && authenticationService.validateTokenAndSession(jwt)) {
                val authenticationToken = UsernamePasswordAuthenticationToken(
                    username, null, emptyList()
                )
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken
            }
        }
        filterChain.doFilter(request, response)
    }
}
