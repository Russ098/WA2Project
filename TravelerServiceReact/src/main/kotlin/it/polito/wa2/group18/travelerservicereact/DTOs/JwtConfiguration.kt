package it.polito.wa2.group18.travelerservicereact.userService

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class JwtConfiguration(
    @Value("\${application.jwt.jwtSecret}")
    val key : String = ""
)