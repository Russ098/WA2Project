package it.polito.wa2.group18.wa2lab3.SecurityPackage

import io.jsonwebtoken.Jwts
import it.polito.wa2.group18.wa2lab3.dtos.UserDetailsDTO
import it.polito.wa2.group18.wa2lab3.entities.Role
import it.polito.wa2.group18.wa2lab3.services.userService.JwtConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {
    @Autowired
    lateinit var jwtConfig: JwtConfiguration

    fun validateJwt(authToken : String) : Boolean{
        val key = jwtConfig.key.toByteArray() // secret in application.properties
        try {
//            println(jwtConfig.key)
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken) //tira un'eccezione se non va bene
            return true
        }catch (e: Exception) {
            println(e)
            return false }
    }

    fun getDetailsJwtFilter(authToken: String) : UserDetailsDTO? {
        val key = jwtConfig.key.toByteArray() // secret in application.properties
        try {
            val jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)

            val roles = jws.body["roles"].toString()
            val rolesList = roles.split(",")
            val rolesEnum : MutableList<Role> = mutableListOf()
            rolesList.forEach { role ->
                rolesEnum.add(Role.valueOf(role))
            }

            val username = jws.body["credentials"].toString()

            return UserDetailsDTO(null, rolesEnum, username)
        } catch (e:Exception) { println(e); return null}
    }
}