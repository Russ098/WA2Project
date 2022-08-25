package it.polito.wa2.group18.reportservice.SecurityPackage

import io.jsonwebtoken.Jwts
import it.polito.wa2.group18.reportservice.DTOs.UserDetailsDTO
import it.polito.wa2.group18.reportservice.Entities.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JwtUtils {

    @Autowired
    lateinit var jwtConfig: JwtConfig

    fun validateJwt(authToken: String): Boolean {
        val key = jwtConfig.key.toByteArray()
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }

    fun getDetailsJwtFilter(authToken: String): UserDetailsDTO? {
        val key = jwtConfig.key.toByteArray() // secret in application.properties
        try {
            val jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
            val roles = jws.body["roles"].toString()
            val rolesList = roles.split(",")
            val rolesEnum: MutableList<Role> = mutableListOf()
            rolesList.forEach { role ->
                rolesEnum.add(Role.valueOf(role))
            }

            val username = jws.body["credentials"].toString()

            return UserDetailsDTO(null, rolesEnum, username)
        } catch (e: Exception) {
            println(e); return null
        }
    }
}