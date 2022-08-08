package it.polito.wa2.group18.wa2lab3.services.userService

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class JwtConfiguration(
    @Value("\${application.jwt.jwtSecret}")
    val key : String = ""
)