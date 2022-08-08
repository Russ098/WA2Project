package it.polito.wa2.group18.wa2lab4.SecurityPackage

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
data class JwtTicketConfig (
    @Value("\${application.jwt.jwtTicketSecret}")
    val key : String,
    @Value("\${application.jwt.expiration}")
    val expirationOffset : Int
)