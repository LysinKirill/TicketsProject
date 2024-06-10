package ru.hse.authenticationservice.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.hse.authenticationservice.entity.User
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.expiration}") val jwtExpirationInMs: Long
) {

    private fun getSecretKey(): SecretKey {
        val bytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(bytes)
    }


    fun generateToken(claims: Map<String, Any>, subject: User): String {
        return Jwts.builder()
            .claims(claims)
            .subject(subject.email)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + jwtExpirationInMs))
            .signWith(getSecretKey(), Jwts.SIG.HS256)
            .compact()
    }

    fun generateToken(user: User): String {
        return generateToken(HashMap(), user)
    }

    fun validateToken(token: String): Boolean {
        val claims = getClaimsFromToken(token)
        return !isTokenExpired(claims)
    }

    fun getEmailFromToken(token: String): String {
        val claims = getClaimsFromToken(token)
        return claims.subject
    }

    private fun getClaimsFromToken(token: String): Claims {
        return Jwts
            .parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }


    private fun isTokenExpired(claims: Claims): Boolean {
        return claims.expiration.before(Date())
    }
}
