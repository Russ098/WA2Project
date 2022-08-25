package it.polito.wa2.group18.reportservice.SecurityPackage

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class JwtConfig (
    @Value("\${application.jwt.jwtSecret}")
    val key : String,
    @Value("\${application.jwt.expiration}")
    val expiration : Int, // In days
    @Value("\${application.jwt.headerName}")
    val headerName : String,
    @Value("\${application.jwt.prefix}")
    val headerPrefix : String
)