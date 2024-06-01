package ru.hse.authenticationservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/api/auth/register").permitAll()
            }
            .build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}