package ru.hse.authenticationservice.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.hse.authenticationservice.entity.User
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.expiration}") val jwtExpirationInMs: Long
) {

    fun generateToken(claims: Map<String, Any>, subject: User): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject.email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationInMs))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun generateToken(user: User): String {
        return generateToken(HashMap(), user)
    }

    fun validateToken(token: String): Boolean {
        val claims = getClaimsFromToken(token)
        return !isTokenExpired(claims)
    }

    fun getUsernameFromToken(token: String): String {
        val claims = getClaimsFromToken(token)
        return claims.subject
    }

    private fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(claims: Claims): Boolean {
        return claims.expiration.before(Date())
    }
}
