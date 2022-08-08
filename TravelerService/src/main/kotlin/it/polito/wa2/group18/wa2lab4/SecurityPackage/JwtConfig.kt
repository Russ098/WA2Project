package it.polito.wa2.group18.wa2lab4.SecurityPackage

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
data class JwtConfig (
    @Value("\${application.jwt.jwtSecret}")
    val key : String,
    @Value("\${application.jwt.headerName}")
    val headerName : String,
    @Value("\${application.jwt.prefix}")
    val headerPrefix : String
)