package ru.hse.ticketsservice.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    private val signingKey: Key
        get() = getSecretKey()


    private fun getSecretKey(): SecretKey {
        val bytes = Decoders.BASE64.decode(jwtSecret)
        return Keys.hmacShaKeyFor(bytes)
    }

    fun extractUsername(token: String): String {
        return getSubjectFromToken(token) ?: ""
    }

    fun getSubjectFromToken(token: String): String? {
        return try {
            val claims = extractAllClaims(token)
            claims.subject
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).body
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = extractAllClaims(token)
            !claims.expiration.before(Date())
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}