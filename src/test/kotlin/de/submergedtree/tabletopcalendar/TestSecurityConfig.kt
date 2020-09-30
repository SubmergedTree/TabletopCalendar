package de.submergedtree.tabletopcalendar

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import java.time.Instant

@TestConfiguration
class TestSecurityConfig {

    @Bean
    fun jwtDecoder(): JwtDecoder? {
        return JwtDecoder { jwt() }
    }

    fun jwt(): Jwt {
        val claims = mapOf("sub" to "sms|12345678")
        return Jwt("token",
            Instant.now(),
            Instant.now().plusSeconds(30),
            mapOf("alg" to "none"),
            claims)
    }
}